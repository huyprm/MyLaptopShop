alter table product_details
    add column active boolean not null default true;

create index idx_product_details_active on product_details (active);