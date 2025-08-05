alter table reviews
    add constraint uq_user_order_details unique (reviewer_id, order_id, product_detail_id);

alter table reviews
    add constraint check_review_rating
    check (rating >= 1 and rating <= 5);

alter table reviews
    add constraint check_rating_required_order
    check ((
        rating is not null and order_id is not null
    ) or (
        rating is null and order_id is null
    ));
