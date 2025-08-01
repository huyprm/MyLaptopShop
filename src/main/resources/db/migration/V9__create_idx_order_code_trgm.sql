create index concurrently if not exists idx_order_code_trgm
on orders using gin (code gin_trgm_ops);