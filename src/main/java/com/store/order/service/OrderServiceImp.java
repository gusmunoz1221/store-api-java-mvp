package com.store.order.service;

import com.store.cart.entity.CartEntity;
import com.store.cart.entity.CartItemEntity;
import com.store.cart.repository.CartRepository;
import com.store.exception.BusinessException;
import com.store.exception.ResourceNotFoundException;
import com.store.order.dto.OrderRequestDTO;
import com.store.order.dto.OrderResponseDTO;
import com.store.order.entity.OrderEntity;
import com.store.order.entity.OrderItemEntity;
import com.store.order.entity.OrderStatus;
import com.store.order.mapper.OrderMapper;
import com.store.order.repository.OrderRepository;
import com.store.product.entity.ProductEntity;
import com.store.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImp implements OrderService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final TransactionTemplate transactionTemplate;

    /**
     * crea una nueva orden a partir del carrito asociado a una sesión de usuario.
     * <p>La operación se ejecuta dentro de una transacción atómica para garantizar
     * la integridad de los datos*
     * <p><strong>NOTA MVP (Simulación de Pago):</strong>
     * dado que esta versión no integra una pasarela de pagos externa (como MercadoPago),
     * se asume un pago exitoso inmediato. Por lo tanto:
     * <ul>
     *  <li>El stock se descuenta automáticamente al crear la orden.</li>
     *  <li>La orden nace con estado {@code PAID} en lugar de {@code PENDING}.</li>
     * </ul>
     *
     * <p>Flujo de validación:
     *  <ul>
     *      <li>Existencia del carrito y formato de correo.</li>
     *      <li>Verificación de stock disponible (Lanza excepción si es insuficiente).</li>
     *  </ul>
     * @param request DTO con los datos de envío y contacto.
     * @return DTO de la orden confirmada.
     * @throws BusinessException si no hay stock suficiente para cubrir la demanda.
     */
    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        OrderEntity savedOrder = transactionTemplate.execute(status -> {
            CartEntity cart = cartRepository.findBySessionId(request.getSessionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado o expirado"));

            if (cart.getItems().isEmpty())
                throw new ResourceNotFoundException("El carrito está vacío");

            if(!isValidMail(request.getCustomerEmail()))
                throw new IllegalArgumentException("Formato de correo inválido");

            OrderEntity order = orderMapper.requestToEntity(request);
            List<OrderItemEntity> orderItems = new ArrayList<>();
            BigDecimal finalTotal = BigDecimal.ZERO;

            for (CartItemEntity cartItem : cart.getItems()) {
                ProductEntity product = cartItem.getProduct();
                int quantity = cartItem.getQuantity();

                if (product.getStock() < quantity)
                    throw new BusinessException("Stock insuficiente para el producto: " + product.getName());

                // --- LOGICA MVP-> DESCUENTO INMEDIATO ---
                product.setStock(product.getStock() - quantity);
                productRepository.save(product);

                OrderItemEntity orderItem = new OrderItemEntity();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(quantity);
                orderItem.setPrice(cartItem.getUnitPrice());

                orderItems.add(orderItem);
                finalTotal = finalTotal.add(orderItem.getPrice().multiply(BigDecimal.valueOf(quantity)));
            }
            order.setItems(orderItems);
            order.setTotalAmount(finalTotal);

            // --- LOGICA MVP-> ESTADO PAGADO ---
            order.setStatus(OrderStatus.PAID);
            order.setCartId(cart.getId());

            cartRepository.delete(cart);

            return orderRepository.save(order);
        });
        return orderMapper.entityToDto(savedOrder);
    }

    /**
     * Valida el formato de un correo electrónico utilizando una expresión regular.
     * @param mail correo a validar
     * @return {@code true} si el formato es válido, {@code false} en caso contrario
     */
    public boolean isValidMail(String mail){
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
        return Pattern.matches(regex, mail);
    }

    /**
     * Procesa la notificación entrante de un webhook de Mercado Pago.
     * <p>Responsabilidades previstas para la versión Pro:
     * <ul>
     *   <li>Extraer el {@code paymentId} desde el payload recibido.</li>
     *   <li>Consultar el estado real del pago contra la API de Mercado Pago.</li>
     *   <li>Registrar el estado del pago junto con un {@code correlationId}.</li>
     *   <li>Delegar el procesamiento según el resultado del pago.</li>
     *   <li>Manejar errores sin propagar excepciones al webhook.</li>
     * </ul>
     * @param payload cuerpo enviado por Mercado Pago
     * @param correlationId identificador único para trazabilidad del webhook
     */
    public void processPaymentNotification(Map<String, Object> payload,String correlationId) {/*versión Pro*/}

    /**
     * Procesa el resultado final de un pago asociado a una orden.
     * <p>Este método será ejecutado dentro de una transacción controlada y
     * aplicará reglas de idempotencia para evitar reprocesar pagos duplicados.
     * <p>Comportamiento esperado:
     * <ul>
     *   <li>{@code approved} → confirma la orden y descuenta stock.</li>
     *   <li>{@code rejected / cancelled} → cancela la orden.</li>
     * </ul>
     * @param orderId identificador de la orden
     * @param mpStatus estado del pago devuelto por Mercado Pago
     * @param correlationId identificador de trazabilidad del webhook
     */
    void processPaymentResult(Long orderId, String mpStatus, String correlationId) { /*versión Pro*/}

            // --- METODOS AUXILIARES PRIVADOS ---

    /**
     * Maneja el procesamiento de una orden con pago aprobado.
     * <p>Responsabilidades previstas:
     * <ul>
     *   <li>Descontar stock mediante actualización directa en base de datos.</li>
     *   <li>Cancelar la orden si algún producto no posee stock suficiente.</li>
     *   <li>Marcar la orden como {@code PAID}.</li>
     *   <li>Eliminar el carrito asociado tras la confirmación.</li>
     * </ul>
     * @param order orden con pago aprobado
     * @param correlationId identificador de trazabilidad del proceso
     */
    private void handleApprovedOrder(OrderEntity order,String correlationId) {/*versión Pro*/}

    /**
     * Maneja una orden cuyo pago fue rechazado o cancelado.
     * <p>La orden será marcada como cancelada y quedará disponible
     * para auditoría o reintentos posteriores.
     * @param order orden asociada al pago rechazado
     */
    private void handleRejectedOrder(OrderEntity order) {/*versión Pro*/}

    /**
     * Extrae el identificador del pago ({@code paymentId}) desde el payload del webhook.
     * <p>Soporta múltiples formatos comunes enviados por Mercado Pago:
     * <ul>
     *   <li>{@code { data: { id: "123" } }}</li>
     *   <li>{@code { id: "123" }}</li>
     * </ul>
     * @param payload cuerpo recibido en el webhook
     * @return identificador del pago LONG. en MPV->VOID
     * @throws BusinessException si no se puede extraer el paymentId
     */
    @SuppressWarnings("unchecked")
    private void extractPaymentId(Map<String, Object> payload) {/*versión Pro*/}

       //------------METODOS PARA ADMIN---------
    /**
     * Obtiene una orden por su identificador.
     * @param id identificador de la orden
     * @return DTO de la orden
     * @throws ResourceNotFoundException si la orden no existe
     */
    @Override
    public OrderResponseDTO getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::entityToDto)
                .orElseThrow(() -> new ResourceNotFoundException("la orden con el ID: "+id+" no existe"));
    }

    /**
     *  -retorna todas las ordenes paginadas
     */
    @Override
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderRepository
                .findAll(pageable)
                .map(orderMapper::entityToDto);
    }

    /**
     *  -filtra ordenes por estado
     *  -retorna el resultado paginado
     */
    @Override
    public Page<OrderResponseDTO> filterOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository
                .findByStatus(status,pageable)
                .map(orderMapper::entityToDto);
    }

    /*
     * retorna órdenes creadas entre dos fechas pagionado
     */
    @Override
    public Page<OrderResponseDTO> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return orderRepository
                .findByCreatedAtBetween(start,end,pageable)
                .map(orderMapper::entityToDto);
    }

    /**
     * Retorna total de ventas y cantidad de clientes.
     */
}
