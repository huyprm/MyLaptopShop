package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.Color;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color,Integer> {
    boolean existsByName(@NotBlank String name);

    boolean existsByHex(@NotBlank String hex);

}
