package com.ptithcm2021.laptopshop.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permission {
    @Id
    private String id;
    private String description;
}
