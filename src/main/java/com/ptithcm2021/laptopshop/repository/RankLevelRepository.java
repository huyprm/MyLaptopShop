package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.RankLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankLevelRepository extends JpaRepository<RankLevel,Integer> {
    @Query("SELECT r FROM RankLevel r WHERE r.active = true")
    List<RankLevel> findAllByIsActive();


    RankLevel findByPriorityAndActive(int i, boolean b);

    @Query(
            value = "SELECT * FROM rank_levels r " +
                    "WHERE r.active = true AND r.priority > :currentRankPriority " +
                    "ORDER BY r.priority ASC LIMIT 1",
            nativeQuery = true
    )
    Optional<RankLevel> findNextRankIsActive(@Param("currentRankPriority") int currentRankPriority);

    @Query("SELECT COUNT(r) FROM RankLevel r WHERE r.id IN :rankIds AND r.active = true")
    long countByActiveRankIds(@Param("rankIds") List<Integer> rankIds);

}
