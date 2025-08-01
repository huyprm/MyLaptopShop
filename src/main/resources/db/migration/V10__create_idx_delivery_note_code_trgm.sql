create index concurrently if not exists idx_grn_code_trgm
    on delivery_notes
        using gin (code gin_trgm_ops);