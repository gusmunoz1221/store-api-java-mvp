package com.store.order.controller;

import com.store.order.dto.OrderRequestDTO;
import com.store.order.dto.OrderResponseDTO;
import com.store.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Operaciones de ordenes para clientes")
public class OrderCustomerController {
    private final OrderService orderService;


    // CREAR ORDEN
    @Operation(summary = "Crear orden", description = "Crea una nueva orden para el cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Orden creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para la orden")
    })
    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@Valid @RequestBody OrderRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }


    // OBTENER ORDEN POR ID
    @Operation(summary = "Obtener orden por ID", description = "Devuelve los detalles de una orden específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getMyOrder(
            @Parameter(description = "ID de la orden", example = "123")
            @PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}