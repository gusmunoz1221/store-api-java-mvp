package com.store.cart.controller;

import com.store.cart.dto.CartItemRequestDTO;
import com.store.cart.dto.CartResponseDTO;
import com.store.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Operaciones de gestion de carritos de compras")
public class CartController {

    private final CartService cartService;

    // VER CARRITO
    @Operation(
            summary = "Obtener carrito actual",
            description = "Recupera el estado actual del carrito basado en el Session-Id enviado en los headers."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Carrito recuperado exitosamente",
                    content = @Content(schema = @Schema(implementation = CartResponseDTO.class))
            )
    })
    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(
            @Parameter(description = "ID de sesión (si no se envía, se asume carrito vacío o nuevo)", example = "session-123")
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {

        return ResponseEntity.ok(cartService.getCart(sessionId));
    }

    // AGREGAR ITEM
    @Operation(
            summary = "Agregar producto",
            description = "Agrega un item al carrito. Si no existe sesión, genera una nueva y la devuelve en los headers."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto agregado correctamente. Se devuelve el ID de sesión actualizado en los headers.",
                    headers = @Header(
                            name = "X-Session-Id",
                            description = "Identificador de la sesion (nuevo o existente) para persistencia en frontend",
                            schema = @Schema(type = "string")
                    ),
                    content = @Content(schema = @Schema(implementation = CartResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos invalidos (ej. cantidad negativa, sin ID de producto)"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado en base de datos")
    })
    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addToCart(
            @Parameter(description = "ID de sesion actual", example = "session-(uuid)")
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,

            @RequestBody @Valid CartItemRequestDTO request) {

        // LOGICA DE SESION
        String resolvedSessionId = (sessionId == null || sessionId.isBlank())
                ? "session-" + UUID.randomUUID().toString()
                : sessionId;

        CartResponseDTO response = cartService.addToCart(
                resolvedSessionId,
                request.getProductId(),
                request.getQuantity());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("X-Session-Id", resolvedSessionId)
                .body(response);
    }

    // ELIMINAR ITEM
    @Operation(summary = "Eliminar un producto específico del carrito")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item eliminado, devuelve el carrito actualizado"),
            @ApiResponse(responseCode = "404", description = "El producto no existIa en el carrito")
    })
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponseDTO> removeFromCart(
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            @PathVariable Long productId) {

        return ResponseEntity.ok(cartService.removeItemFromCart(sessionId, productId));
    }

    // VACIAR CARRITO
    @Operation(summary = "Vaciar carrito completo")
    @ApiResponse(responseCode = "204", description = "Carrito vaciado correctamente -> Sin contenido")
    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {

        cartService.clearCart(sessionId);
        return ResponseEntity.noContent().build();
    }
}