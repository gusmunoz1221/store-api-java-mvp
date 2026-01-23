package com.store.cart.service;

import com.store.cart.dto.CartResponseDTO;

public interface CartService {
    // OBTENER EL CARRITO ACTUAL, O CREA UNO EN CASO QUE NO EXISTA
    CartResponseDTO getCart(String sessionId);

    // AGREGAR UN PRODUCTO O SUMAR CANTIDAD EN CASO QUE NO EXISTA
    CartResponseDTO addToCart(String sessionId, Long productId, Integer quantity);

    // ELIMINAR UN PRODUCTO DEL CARRITO
    CartResponseDTO removeItemFromCart(String sessionId, Long productId);

    // VACIAR CARRITO
    void clearCart(String sessionId);
}
