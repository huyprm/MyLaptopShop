create or replace function remove_expired_orders()
returns void as $$
declare
    expired_order_ids bigint[];
begin
    select array (
                   select id
                   from orders
                   where orders.created_date < now() - interval '24' hour and orders.status = 'PENDING'
           ) INTO expired_order_ids;

    IF array_length(expired_order_ids, 1) is null then
        raise notice 'No expired orders to remove.';
        return ;
    end if;

    update inventories i
    set quantity = quantity + od.quantity
    from order_details od
    join orders o on od.order_id = o.id
    where i.product_detail_id = od.product_detail_id
      and o.id = any(expired_order_ids);


    delete from order_details
    where order_id = any(expired_order_ids);

    delete from orders
    where id = any(expired_order_ids);
end;
$$ LANGUAGE plpgsql;
