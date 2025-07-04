package com.ptithcm2021.laptopshop.model.entity;

import com.ptithcm2021.laptopshop.model.enums.LoginTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "login_identifiers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"identifier_type", "identifier_value"}),
                @UniqueConstraint(columnNames = {"user_id", "identifier_type"})
        }
)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class LoginIdentifier {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identifier_type")
    @Enumerated(EnumType.STRING)
    private LoginTypeEnum identifierType;

    @Column(name = "identifier_value", unique = true)
    private String identifierValue;

    private String secret;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;

}
