alter table product_details
    add column if not exists active boolean not null default true;

create index if not exists idx_product_details_active on product_details (active);