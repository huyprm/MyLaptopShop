package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.dto.projection.DashboardCustomerTopProjection;
import com.ptithcm2021.laptopshop.model.dto.projection.DashboardRevenueProjection;
import com.ptithcm2021.laptopshop.model.dto.projection.DashboardSummaryProjection;
import com.ptithcm2021.laptopshop.model.dto.projection.DashboardTopProductProjection;
import com.ptithcm2021.laptopshop.model.dto.response.DashboardRevenueResponse;
import com.ptithcm2021.laptopshop.model.entity.Order;
import com.ptithcm2021.laptopshop.model.entity.User;
import com.ptithcm2021.laptopshop.model.enums.OrderStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUser(User user, Pageable pageable);

    Page<Order> findAllByUserAndStatus(User user, OrderStatusEnum status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.createdDate < :threshold")
    List<Order> findAllOrderExpiredPayment(LocalDateTime threshold);

    @Query(value = "SELECT remove_expired_orders()", nativeQuery = true)
    void removeExpiredOrders();

    int countByCodeStartingWith(String baseCode);

    Optional<Order> findByCode(String code);

    @Query("SELECT o FROM Order o WHERE (:code is null or o.code like :code) AND (:status is null or o.status = :status)")
    Page<Order> findByCodeAndStatus(String code, OrderStatusEnum status, Pageable pageable);

    @Query("""
        SELECT
            COUNT(DISTINCT o.id) AS totalOrders,
            COUNT(DISTINCT o.user) AS totalCustomers,
            SUM(o.totalPrice) AS totalRevenue
        FROM Order o
        WHERE o.status = 'COMPLETED'
""")
    DashboardSummaryProjection getDashboardSummary();

    @Query(value = "SELECT yearMonth, monthlyRevenue, monthlyGrossProfit, monthlyTotalCost " +
            "FROM calculate_gross_profit(:year)",
            nativeQuery = true)
    List<DashboardRevenueProjection> getDashboardRevenue(
            @Param("year")  int year
    );


    @Query("""
        SELECT
            p.id as productDetailId,
            p.title as productTitle,
            p.thumbnail as productThumbnail,
            SUM(od.quantity) AS totalSold,
            SUM(od.quantity * od.price) AS revenue
        FROM OrderDetail od
        JOIN Order o ON o.id = od.order.id
        JOIN ProductDetail p ON p.id = od.productDetail.id
        WHERE o.status = 'COMPLETED'
        GROUP BY p.id, p.title, p.thumbnail
        ORDER BY totalSold DESC
        LIMIT :limit
""")
    List<DashboardTopProductProjection> getDashboardTopProduct(int limit);

    @Query("""
        SELECT
            u.id as id,
            u.fullName as fullName,
            u.email as email,
            COUNT(o.id) AS totalOrders,
            SUM(o.totalPrice) AS totalSpent
        FROM Order o
        JOIN User u ON u.id = o.user.id
        WHERE o.status = 'COMPLETED'
        GROUP BY u.id, u.fullName
        ORDER BY totalSpent DESC
        LIMIT :limit
""")
    List<DashboardCustomerTopProjection> getDashboardCustomerTop(int limit);



    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"orderDetails"})
    @Query("""
        SELECT o
        FROM Order o
        WHERE o.completedAt >= :from AND o.createdDate < :to
    """)
    List<Order> findAllByCreatedDateBetween(LocalDateTime from, LocalDateTime to);
}
