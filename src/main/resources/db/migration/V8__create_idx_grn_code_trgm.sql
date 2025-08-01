create index concurrently if not exists idx_grn_code_trgm
    on goods_receipt_notes
        using gin (code gin_trgm_ops);