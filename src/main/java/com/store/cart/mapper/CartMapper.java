package com.store.cart.mapper;

import com.store.cart.dto.CartItemResponseDTO;
import com.store.cart.dto.CartResponseDTO;
import com.store.cart.entity.CartEntity;
import com.store.cart.entity.CartItemEntity;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {

    public CartResponseDTO entityToDto(CartEntity entity) {

        List<CartItemResponseDTO> itemsDto = entity.getItems().stream()
                .map(this::itemToDto)
                .toList();

        return CartResponseDTO.builder()
                .id(entity.getId())
                .sessionId(entity.getSessionId())
                .totalAmount(entity.getTotalAmount())
                .totalItems(itemsDto.size())
                .items(itemsDto)
                .build();
    }

    public CartItemResponseDTO itemToDto(CartItemEntity entity) {

        BigDecimal price = entity.getUnitPrice() != null ? entity.getUnitPrice() : BigDecimal.ZERO;

        BigDecimal subtotal = price.multiply(BigDecimal.valueOf(entity.getQuantity()));

        return CartItemResponseDTO.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .productName(entity.getProduct().getName())
                .quantity(entity.getQuantity())
                .unitPrice(price)
                .subtotal(subtotal)
                .build();
    }
}