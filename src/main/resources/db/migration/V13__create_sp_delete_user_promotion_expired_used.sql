create or replace procedure sp_delete_user_promotion_expired_used()
language plpgsql
as $$
begin
    -- Xoá các khuyến mãi đã hết hạn hoặc đã sử dụng
    delete from user_promotions up
    where up.id in (
        select up.id
        from user_promotions up
        join promotions p on p.id = up.promotion_id
        where p.end_date < now()
    );

    raise notice 'User promotions expired or used have been deleted successfully.';
end;
$$