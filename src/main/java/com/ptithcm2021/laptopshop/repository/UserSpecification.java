package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.Role;
import com.ptithcm2021.laptopshop.model.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> filter(String keyword, String roleId, Boolean blocked) {
        return (root, query, cb)->{
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.isBlank()) {
                Predicate namePredicate = cb.like(root.get("fullName"), "%" + keyword.toLowerCase() + "%");
                Predicate emailPredicate = cb.like(root.get("email"), "%" + keyword.toLowerCase() + "%");

                predicates.add(cb.or(namePredicate, emailPredicate));
            }

            if(roleId != null && !roleId.isBlank()) {
                Join<User, Role> rolesJoin = root.join("roles");
                predicates.add(cb.equal(rolesJoin.get("id"), roleId));
            }

            if(blocked != null) {
                predicates.add(cb.equal(root.get("blocked"), blocked));
            }

            assert query != null;
            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
