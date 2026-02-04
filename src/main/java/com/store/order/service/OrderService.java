package com.store.order.service;


import com.store.order.dto.OrderPublicResponseDTO;
import com.store.order.dto.OrderRequestDTO;
import com.store.order.dto.OrderAdminResponseDTO;
import com.store.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface OrderService {
    // Crea una nueva orden
    OrderPublicResponseDTO createOrder(OrderRequestDTO request);

    // Busca una orden específica
    OrderAdminResponseDTO getOrderById(Long id);


    //------------ADMIN------
    // Lista todas las órdenes paginadas
    Page<OrderAdminResponseDTO> getAllOrders(Pageable pageable);

    // Filtra órdenes por estado (Ej: ver solo las "PENDING")
    Page<OrderAdminResponseDTO> filterOrdersByStatus(OrderStatus status, Pageable pageable);

    // Busca órdenes por rango de fechas (Reportes)
    Page<OrderAdminResponseDTO> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // ACTUALIZA ESTADO
    OrderAdminResponseDTO updateOrderStatus(Long id, OrderStatus newStatus);
}