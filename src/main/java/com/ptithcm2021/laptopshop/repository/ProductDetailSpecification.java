package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductFilterRequest;
import com.ptithcm2021.laptopshop.model.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class ProductDetailSpecification {
    public static Specification<ProductDetail> filter(ProductFilterRequest filter) {
        return (root, query, cb) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            // Join từ ProductDetail lên Product
            Join<ProductDetail, Product> productJoin = root.join("product", JoinType.LEFT);
            Join<Product, Brand> brandJoin = productJoin.join("brand", JoinType.LEFT);
            Join<Product, Series> seriesJoin = productJoin.join("series", JoinType.LEFT);
            Join<Product, Category> categoryJoin = productJoin.join("category", JoinType.LEFT);

            // Join Config
            Join<ProductDetail, Config> configJoin = root.join("config", JoinType.LEFT);

            // Category filter
            if (filter.getCategoryId() != null) {
                predicates.add(cb.equal(categoryJoin.get("id"), filter.getCategoryId()));
            }

            // Brand filter
            if (filter.getBrandId() != null) {
                predicates.add(cb.equal(brandJoin.get("id"), filter.getBrandId()));
            }

            // Series filter
            if (filter.getSeriesId() != null) {
                predicates.add(cb.equal(seriesJoin.get("id"), filter.getSeriesId()));
            }

            // Price range
            if (filter.getMinPrice() != null) {
                predicates.add(cb.ge(root.get("discountPrice"), filter.getMinPrice()));
            }
            if (filter.getMaxPrice() != null) {
                predicates.add(cb.le(root.get("discountPrice"), filter.getMaxPrice()));
            }

            // Config CPU
            if (filter.getCpu() != null) {
                predicates.add(cb.like(cb.lower(configJoin.get("cpu")), "%" + filter.getCpu().toLowerCase() + "%"));
            }

            // RAM
            if (filter.getRam() != null) {
                predicates.add(cb.equal(configJoin.get("ram"), filter.getRam()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }



}
