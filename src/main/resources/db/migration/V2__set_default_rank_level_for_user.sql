update users
set rank_level_id = (
    select id from rank_levels
    where priority = 1 and active = true
    limit 1)
where rank_level_id is null;