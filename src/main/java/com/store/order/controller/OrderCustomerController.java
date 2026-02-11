package com.store.order.controller;

import com.store.order.dto.OrderRequestDTO;
import com.store.order.dto.TrackOrderRequestDTO;
import com.store.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
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
import com.store.order.dto.OrderPublicResponseDTO;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders (Cliente)", description = "Endpoints públicos para compra y seguimiento de pedidos")
public class OrderCustomerController {

    private final OrderService orderService;

    // CREAR ORDEN
    @Operation(
            summary = "Crear nueva orden",
            description = "Finaliza la compra creando una orden basada en el carrito actual. Retorna el Tracking ID y el Número de Orden."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Orden creada exitosamente",
                    content = @Content(schema = @Schema(implementation = OrderPublicResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos de envío invalidos, carrito vacio o stock insuficiente")
    })
    @PostMapping
    public ResponseEntity<OrderPublicResponseDTO> create(@Valid @RequestBody OrderRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    // TRACKING
    @Operation(
            summary = "Rastrear orden manualmente",
            description = "Busca una orden validando que coincidan el Numero de Orden y el Email del comprador. Usado en formularios de 'Seguir mi envío'."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Orden validada y encontrada",
                    content = @Content(schema = @Schema(implementation = OrderPublicResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Formato de email o numero de orden invalido"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró ninguna orden que coincida con ese numero Y ese email"
            )
    })
    @PostMapping("/track")
    public ResponseEntity<OrderPublicResponseDTO> trackManual(@Valid @RequestBody TrackOrderRequestDTO request) {
        return ResponseEntity.ok(orderService.trackOrder(request.getOrderNumber(), request.getEmail()));
    }
}