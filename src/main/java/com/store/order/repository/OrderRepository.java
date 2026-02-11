package com.store.order.repository;

import com.store.order.entity.OrderEntity;
import com.store.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findAll(Pageable pageable);

    Page<OrderEntity> findByStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM OrderEntity o WHERE o.orderNumber = :orderNumber OR CAST(o.id AS string) = :orderNumber")
    Optional<OrderEntity> findSmart(@Param("orderNumber") String orderNumber);

    Optional<OrderEntity> findByOrderNumberAndCustomerEmail(String orderNumber, String email);

    @Query("SELECT o FROM OrderEntity o WHERE " +
            "o.createdAt BETWEEN :start AND :end " +
            "AND (:statuses IS NULL OR o.status IN :statuses)")
    Page<OrderEntity> findByReportFilters(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("statuses") List<OrderStatus> statuses,
                                          Pageable pageable);
}