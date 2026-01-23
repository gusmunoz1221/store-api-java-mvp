package com.store.order.repository;

import com.store.order.entity.OrderEntity;
import com.store.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findAll(Pageable pageable);

    // por estado
    Page<OrderEntity> findByStatus(OrderStatus status, Pageable pageable);

    // solo por Rango de Fechas
    Page<OrderEntity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

}