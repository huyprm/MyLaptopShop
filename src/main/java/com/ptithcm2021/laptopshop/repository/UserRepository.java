package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
