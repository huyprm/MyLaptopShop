CREATE OR REPLACE FUNCTION calculate_gross_profit(year_input integer)
    RETURNS TABLE (
                      yearMonth TEXT,
                      monthlyRevenue BIGINT,
                      monthlyGrossProfit BIGINT,
                      monthlyTotalCost BIGINT
                  )
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
        WITH months AS (
            SELECT generate_series(
                           date_trunc('month', make_date(year_input, 1, 1)),
                           date_trunc('month', make_date(year_input, 12, 1)),
                           interval '1 month'
                   ) AS month_start
        )
        SELECT
            to_char(m.month_start, 'YYYY-MM') AS yearMonth,

            -- Doanh thu tháng
            COALESCE(SUM(
                             CASE WHEN o.status = 'COMPLETED' THEN o.total_price ELSE 0 END
                     ), 0)::BIGINT AS monthlyRevenue,

            -- Lợi nhuận gộp tháng
            COALESCE(SUM(
                             CASE WHEN o.status = 'COMPLETED' THEN sub.gross_profit ELSE 0 END
                     ), 0)::BIGINT AS monthlyGrossProfit,

            -- Tổng chi phí nhập hàng tháng
            (
                SELECT COALESCE(SUM(g.total_price), 0)::BIGINT
                FROM goods_receipt_notes g
                WHERE g.received_date >= m.month_start
                  AND g.received_date < (m.month_start + INTERVAL '1 month')
            ) AS monthly_total_cost

        FROM months m
                 LEFT JOIN orders o
                           ON date_trunc('month', o.created_date) = m.month_start
                               AND o.completed_at >= make_date(year_input, 1, 1)
                               AND o.completed_at < make_date(year_input + 1, 1, 1)
                 LEFT JOIN LATERAL (
            SELECT SUM(od.price - spi_grn.unit_price) AS gross_profit
            FROM order_details od
                     JOIN order_detail_serial_number odsn
                          ON odsn.order_detail_order_id = od.order_id
                              AND odsn.order_detail_product_detail_id = od.product_detail_id
                     JOIN serial_product_items spi
                          ON spi.serial_number = odsn.serial_number
                     JOIN grn_details spi_grn
                          ON spi.grn_detail_id = spi_grn.id
            WHERE od.order_id = o.id
            ) sub ON TRUE
        GROUP BY m.month_start
        ORDER BY yearMonth;
END;
$$;
