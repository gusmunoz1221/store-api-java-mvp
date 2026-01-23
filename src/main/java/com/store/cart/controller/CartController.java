package com.store.cart.controller;

import com.store.cart.dto.CartItemRequestDTO;
import com.store.cart.dto.CartResponseDTO;
import com.store.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Operaciones de gestion de carritos")
public class CartController {
    private final CartService cartService;


    // VER CARRITO
    @Operation(
            summary = "Ver carrito",
            description = "Obtiene el carrito asociado a la sesión actual"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito obtenido correctamente"),
    })
    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(
            @Parameter(description = "Identificador de sesión", example = "session-123")
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {

        return ResponseEntity.ok(cartService.getCart(sessionId));
    }


    //  AGREGAR ITEM
    @Operation(
            summary = "Agregar producto al carrito",
            description = "Agrega un producto al carrito actual o crea uno nuevo si no existe"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto agregado al carrito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addToCart(
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            @RequestBody CartItemRequestDTO request) {

        String resolvedSessionId = sessionId == null || sessionId.isBlank()
                ? "session-" + UUID.randomUUID().toString()
                : sessionId;

        CartResponseDTO response = cartService.addToCart(resolvedSessionId,
                request.getProductId(),
                request.getQuantity());

        // el frontend lee este header para actualizar el localStorage.
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("X-Session-Id", response.getSessionId())
                .body(response);
    }


    // ELIMINAR
    @Operation(
            summary = "Eliminar producto del carrito",
            description = "Elimina un producto específico del carrito"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto eliminado del carrito"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado en el carrito")
    })
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponseDTO> removeFromCart(
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(sessionId, productId));
    }


    // VACIAR CARRITO
    @Operation(
            summary = "Vaciar carrito",
            description = "Elimina todos los productos del carrito"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Carrito vaciado correctamente")
    })
    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {
        cartService.clearCart(sessionId);
        return ResponseEntity.noContent().build();
    }
}
