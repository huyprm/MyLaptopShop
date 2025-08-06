package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Page<User> findAllByBlocked(Pageable page, boolean blocked);

    @Query("""
    select u.id from User u
    where u.currentRankLevel.id in :rankIds and u.blocked = false
""")
    List<String> findUserIdsByRankIds(List<Integer> rankIds);

    long countByIdIn(Set<String> toAdd);
}
