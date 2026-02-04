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
public class OrderAdminResponseDTO {
    Long id;
    String customerName;
    String customerEmail;
    String customerPhone;    // PII (Sensitive)
    String shippingAddress;
    String shippingCity;
    BigDecimal totalAmount;
    OrderStatus status;
    LocalDateTime createdAt;
    List<OrderItemResponseDTO> items;
}
