create or replace procedure sp_update_discount_price()
language plpgsql
as $$
begin
    -- cập nhật discount_price nếu có khuyến mãi còn hiệu lực
update product_details pd
set discount_price = (
    select least(
                   min(
                           case
                               when pr.discount_unit = 'PERCENT' then pd.original_price * (100 - pr.discount_value) / 100
                               when pr.discount_unit = 'AMOUNT' then pd.original_price - pr.discount_value
                               else pd.original_price
                               end
                   ),
                   pd.original_price
           )
    from product_promotions pp
             join promotions pr on pr.id = pp.promotion_id
    where pp.product_detail_id = pd.id
      and pr.start_date <= now()
      and (pr.end_date is null or pr.end_date > now())
)
where exists (
    select 1
    from product_promotions pp
             join promotions pr on pr.id = pp.promotion_id
    where pp.product_detail_id = pd.id
      and pr.start_date <= now()
      and (pr.end_date is null or pr.end_date > now())
);

-- cập nhật lại discount_price = original_price nếu không có khuyến mãi nào hợp lệ
update product_details pd
set discount_price = pd.original_price
where not exists (
    select 1
    from product_promotions pp
             join promotions pr on pr.id = pp.promotion_id
    where pp.product_detail_id = pd.id
      and pr.start_date <= now()
      and (pr.end_date is null or pr.end_date > now())
);

raise notice 'discount prices updated successfully.';
end;
$$;
