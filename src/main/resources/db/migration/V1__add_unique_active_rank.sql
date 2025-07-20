CREATE UNIQUE INDEX uq_rank_active_priority
    ON rank_levels (priority)
    WHERE active = true;
