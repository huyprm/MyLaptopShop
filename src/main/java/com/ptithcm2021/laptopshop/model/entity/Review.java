package com.ptithcm2021.laptopshop.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @CreatedDate
    private LocalDateTime reviewDate;

    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Review parentReview;

    @ManyToOne()
    @JoinColumn(name = "reply_on_user")
    private User replyOnUser;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;
    private String reviewImage;
}
