package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import com.ptithcm2021.laptopshop.model.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("select r from Review r where r.productDetail.id=:pId and r.parentReview is null and r.rating is null")
    List<Review> findReviewParentByProductDetailId(@Param("pId") long productDetailId);

    @Query("select r.id from Review r where r.parentReview.id = :id and r.rating is null")
    List<Review> findAllByParentReviewId(Long id);


    @Query("select r from Review r where r.productDetail.id = :productDetailId and r.rating is not null")
    Page<Review> findAllRatingByProductDetailId(long productDetailId, Pageable pageable);
}