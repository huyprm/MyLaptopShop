create or replace procedure sp_update_discount_price()
    language plpgsql
as $$
begin
    -- Cập nhật discount_price nếu có khuyến mãi còn hiệu lực
    update product_details pd
    set discount_price = (
        select least(
                       min(
                               case
                                   when pr.discount_unit = 'PERCENT'
                                       then (pd.original_price::numeric * (100 - pr.discount_value)::numeric) / 100
                                   when pr.discount_unit = 'AMOUNT'
                                       then pd.original_price::numeric - pr.discount_value::numeric
                                   else pd.original_price
                                   end
                       ),
                       pd.original_price
               )
        from promotions pr
                 left join product_promotions pp on pr.id = pp.promotion_id
        where (pr.promotion_type = 'PRODUCT_DISCOUNT' or pr.promotion_type = 'ALL_PRODUCT')
          and pr.start_date <= now()
          and (pr.end_date is null or pr.end_date >= now())
          and (pp.product_detail_id = pd.id or pp.product_detail_id is null)
    )
    where exists (
        select 1
        from promotions pr
                 left join product_promotions pp on pr.id = pp.promotion_id
        where (pr.promotion_type = 'PRODUCT_DISCOUNT' or pr.promotion_type = 'ALL_PRODUCT')
          and pr.start_date <= now()
          and (pr.end_date is null or pr.end_date >= now())
          and (pp.product_detail_id = pd.id or pp.product_detail_id is null)
    );

    -- Cập nhật lại discount_price = original_price nếu không có khuyến mãi hợp lệ
    update product_details pd
    set discount_price = pd.original_price
    where not exists (
        select 1
        from promotions pr
                 left join product_promotions pp on pr.id = pp.promotion_id
        where (pr.promotion_type = 'PRODUCT_DISCOUNT' or pr.promotion_type = 'ALL_PRODUCT')
          and pr.start_date <= now()
          and (pr.end_date is null or pr.end_date >= now())
          and (pp.product_detail_id = pd.id or pp.product_detail_id is null)
    );

    raise notice 'Discount prices updated successfully.';
end;
$$;
