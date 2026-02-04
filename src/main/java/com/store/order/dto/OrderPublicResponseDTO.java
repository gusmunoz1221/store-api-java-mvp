package com.store.order.dto;

import com.store.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderPublicResponseDTO {
    String orderNumber;
    BigDecimal totalAmount;
    OrderStatus status;
    int totalItems;
    LocalDateTime createdAt;
    LocalDateTime purchasedAt;
    List<OrderItemResponseDTO> items;
}
