create index concurrently if not exists idx_promotion_code_trgm
on promotions
    using gin (code gin_trgm_ops);