CREATE UNIQUE INDEX auq_true_address
    ON address (user_id)
    WHERE is_default = true;