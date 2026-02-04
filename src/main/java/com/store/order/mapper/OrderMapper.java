package com.store.order.mapper;

import com.store.cart.entity.CartItemEntity;
import com.store.order.dto.OrderItemResponseDTO;
import com.store.order.dto.OrderPublicResponseDTO;
import com.store.order.dto.OrderRequestDTO;
import com.store.order.dto.OrderAdminResponseDTO;
import com.store.order.entity.OrderEntity;
import com.store.order.entity.OrderItemEntity;
import com.store.product.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
public class OrderMapper {

    public OrderPublicResponseDTO entityToPublicDto(OrderEntity entity) {
        if (entity == null) return null;

        return OrderPublicResponseDTO.builder()
                .orderNumber(entity.getOrderNumber() != null ? entity.getOrderNumber() : "N/A")
                .totalAmount(entity.getTotalAmount())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .totalItems(entity.getItems().size())
                .items(mapItems(entity.getItems()))
                .build();
    }

    public OrderAdminResponseDTO entityToAdminDto(OrderEntity entity) {
        if (entity == null) return null;

        return OrderAdminResponseDTO.builder()
                .id(entity.getId())
                .customerName(entity.getCustomerName())
                .customerEmail(entity.getCustomerEmail())
                .customerPhone(entity.getCustomerPhone())
                .shippingAddress(entity.getShippingAddress())
                .shippingCity(entity.getShippingCity())
                .totalAmount(entity.getTotalAmount())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .items(mapItems(entity.getItems()))
                .build();
    }


    private List<OrderItemResponseDTO> mapItems(List<OrderItemEntity> items) {
        if (items == null) return Collections.emptyList();

        return items.stream()
                .map(this::itemToDto)
                .toList();
    }

    public OrderItemResponseDTO itemToDto(OrderItemEntity entity) {
        BigDecimal subtotal = entity.getPrice().multiply(BigDecimal.valueOf(entity.getQuantity()));

        return OrderItemResponseDTO.builder()
                .id(entity.getId())
                .productName(entity.getProduct().getName())
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .subtotal(subtotal)
                .build();
    }

    public OrderEntity requestToEntity(OrderRequestDTO request) {
        OrderEntity order = new OrderEntity();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setCustomerPhone(request.getCustomerPhone());

        order.setShippingAddress(request.getShippingAddress());
        order.setShippingCity(request.getShippingCity());
        order.setShippingZip(request.getShippingZip());
        order.setSessionId(request.getSessionId());

        return order;
    }

    public OrderItemEntity buildOrderItem(OrderEntity order, ProductEntity product, CartItemEntity cartItem) {
        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getUnitPrice());
        return orderItem;
    }
}
