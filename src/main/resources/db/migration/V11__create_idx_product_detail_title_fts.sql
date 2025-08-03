create index concurrently if not exists idx_product_detail_title_fts
on product_details
using gin(to_tsvector('simple', title))