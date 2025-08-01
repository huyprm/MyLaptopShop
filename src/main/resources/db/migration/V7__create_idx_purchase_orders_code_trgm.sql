create index concurrently if not exists idx_purchase_orders_code_trgm
    on purchase_orders
        using gin (code gin_trgm_ops);