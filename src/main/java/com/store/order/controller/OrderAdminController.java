package com.store.order.controller;

import com.store.order.dto.OrderAdminResponseDTO;
import com.store.order.entity.OrderStatus;
import com.store.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@Tag(name = "ADMIN - Ordenes", description = "Gestion administrativa de ordenes y reportes")
public class OrderAdminController {

    private final OrderService orderService;

    @Operation(summary = "Listar todas las ordenes", description = "Obtiene un listado paginado de todas las ordenes del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<Page<OrderAdminResponseDTO>> getAllOrders(
            @Parameter(description = "Paginación", hidden = true)
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @Operation(summary = "Obtener orden por ID", description = "Devuelve el detalle completo de una orden especifica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden encontrada"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderAdminResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Operation(summary = "Filtrar por estado", description = "Busca ordenes segun su estado actual")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ordenes filtradas correctamente")
    })
    @GetMapping("/status")
    public ResponseEntity<Page<OrderAdminResponseDTO>> getOrdersByStatus(
            @Parameter(description = "Estado de la orden")
            @RequestParam OrderStatus status,
            @Parameter(description = "Paginación", hidden = true)
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(orderService.filterOrdersByStatus(status, pageable));
    }

    @Operation(summary = "Actualizar estado de orden", description = "Modifica el estado de una orden existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderAdminResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado para la orden")
            @RequestParam OrderStatus newStatus) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, newStatus));
    }

    @Operation(summary = "Reporte avanzado", description = "Reporte de ordenes por rango de fechas y lista opcional de estados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente")
    })
    @GetMapping("/report")
    public ResponseEntity<Page<OrderAdminResponseDTO>> getReport(
            @Parameter(description = "Fecha inicio (dd-MM-yyyy)", example = "01-01-2025")
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate start,

            @Parameter(description = "Fecha fin (dd-MM-yyyy)", example = "31-01-2025")
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate end,

            @Parameter(description = "Lista de estados (opcional)", array = @ArraySchema(schema = @Schema(implementation = OrderStatus.class)))
            @RequestParam(required = false) List<OrderStatus> status,

            @Parameter(description = "Paginación", hidden = true)
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(orderService.getReport(start.atStartOfDay(),
                end.atTime(LocalTime.MAX),
                status, pageable
        ));
    }


    @Operation(
            summary = "Buscar una orden por su ID publico",
            description = "Permite al administrador buscar una orden específica utilizando el Order Number (ej: ORD-9921) visible para el cliente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orden encontrada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderAdminResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró ninguna orden con ese número",
                    content = @Content
            )
    })
    @GetMapping("/search")
    public ResponseEntity<OrderAdminResponseDTO> searchOrders(
            @Parameter(description = "El numero de la orden tal cual lo ve el cliente", required = true, example = "ORD-9921")
            @RequestParam("orderNumber") String orderNumber) {

        return ResponseEntity.ok(orderService.findOrder(orderNumber));
    }
}
