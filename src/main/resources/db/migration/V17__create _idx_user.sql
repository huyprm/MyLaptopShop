create index concurrently if not exists idx_user_email_trgm
on users
    using gin (email gin_trgm_ops);

create index concurrently if not exists idx_user_full_name_trgm
on users
    using gin (full_name gin_trgm_ops);