package com.store.order.service;


import com.store.order.dto.OrderRequestDTO;
import com.store.order.dto.OrderResponseDTO;
import com.store.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface OrderService {
    // CHECKOUT
    OrderResponseDTO createOrder(OrderRequestDTO request);

    // BUSCAR ORDEN POR ID
    OrderResponseDTO getOrderById(Long id);

     //  ------------(ADMIN)--------
    // LISTAR TODAS LAS ORDENES (ADMIN)
    Page<OrderResponseDTO> getAllOrders(Pageable pageable);

    // BUSCAR ORDEN POR ESTADO
    Page<OrderResponseDTO> filterOrdersByStatus(OrderStatus status, Pageable pageable);

    // BUSCAR ORDENB POR FECHAS
    Page<OrderResponseDTO> findByCreatedAtBetween(LocalDateTime start,
                                                  LocalDateTime end,
                                                  Pageable pageable);

}
