CREATE OR REPLACE PROCEDURE sp_update_discount_price()
    LANGUAGE plpgsql
AS $$
BEGIN
    CREATE TEMP TABLE tmp_best_promotion AS
    SELECT DISTINCT ON (pd.id)
        pd.id AS product_detail_id,
        LEAST(
                CASE
                    WHEN pr.discount_unit = 'PERCENT'
                        THEN (pd.original_price::numeric * (100 - pr.discount_value)::numeric) / 100
                    WHEN pr.discount_unit = 'AMOUNT'
                        THEN pd.original_price::numeric - pr.discount_value::numeric
                    ELSE pd.original_price
                    END,
                pd.original_price
        ) AS discount_price,
        pr.id::text AS promotion_id
    FROM product_details pd
             JOIN promotions pr
                  ON pr.promotion_type IN ('PRODUCT_DISCOUNT', 'ALL_PRODUCT')
                      AND pr.start_date <= NOW()
                      AND (pr.end_date IS NULL OR pr.end_date >= NOW())
             LEFT JOIN product_promotions pp
                       ON pr.id = pp.promotion_id
                           AND (pp.product_detail_id = pd.id OR pp.product_detail_id IS NULL)
    ORDER BY pd.id, discount_price ASC;

    UPDATE product_details pd
    SET discount_price = bp.discount_price,
        promotion_id_max_discount = bp.promotion_id
    FROM tmp_best_promotion bp
    WHERE pd.id = bp.product_detail_id;

    UPDATE product_details pd
    SET discount_price = pd.original_price,
        promotion_id_max_discount = NULL
    WHERE NOT EXISTS (
        SELECT 1 FROM tmp_best_promotion bp WHERE bp.product_detail_id = pd.id
    );

    DROP TABLE tmp_best_promotion;
END;
$$;
