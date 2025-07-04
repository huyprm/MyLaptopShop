package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.LoginIdentifier;
import com.ptithcm2021.laptopshop.model.enums.LoginTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginIdentifierRepository extends JpaRepository<LoginIdentifier, Long> {
    Optional<LoginIdentifier> findByIdentifierValue(String identifierValue);
    boolean existsByIdentifierValue(String identifierValue);
    Optional<LoginIdentifier> findByIdentifierTypeAndUserId(LoginTypeEnum identifierType, String user_id);
}
