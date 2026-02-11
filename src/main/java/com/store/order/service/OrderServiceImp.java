package com.store.order.service;

import com.store.cart.entity.CartEntity;
import com.store.cart.entity.CartItemEntity;
import com.store.cart.repository.CartRepository;
import com.store.exception.ResourceNotFoundException;
import com.store.order.dto.OrderPublicResponseDTO;
import com.store.order.dto.OrderRequestDTO;
import com.store.order.dto.OrderAdminResponseDTO;
import com.store.order.entity.OrderEntity;
import com.store.order.entity.OrderItemEntity;
import com.store.order.entity.OrderStatus;
import com.store.order.event.OrderCreatedEvent;
import com.store.order.mapper.OrderMapper;
import com.store.order.repository.OrderRepository;
import com.store.product.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImp implements OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final TransactionTemplate transactionTemplate;
    private final ApplicationEventPublisher eventPublisher;


    /**
     * Crea y persiste una nueva orden de compra basada en el carrito de la sesión actual.
     * * Implementa un bloqueo transaccional programático (TransactionTemplate) para garantizar
     * que el decremento de stock y la creación de la orden ocurran atómicamente,
     * previniendo condiciones de carrera.
     * * Utiliza un patrón orientado a eventos (ApplicationEventPublisher) para manejar
     * tareas asíncronas posteriores a la transacción exitosa.
     * * @param request Datos de entrada del formulario de checkout.
     * @return Respuesta pública limitada para seguridad del cliente.
     */
    @Transactional
    @Override
    public OrderPublicResponseDTO createOrder(OrderRequestDTO request) {

        if (!isValidMail(request.getCustomerEmail())) {
            throw new IllegalArgumentException("Formato de correo inválido");
        }

        OrderEntity savedOrder = transactionTemplate.execute(status -> {
            CartEntity cart = cartRepository.findBySessionId(request.getSessionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Carrito expirado o no encontrado"));

            if (cart.getItems().isEmpty()) {
                throw new ResourceNotFoundException("El carrito está vacío");
            }

            OrderEntity order = orderMapper.requestToEntity(request);
            List<OrderItemEntity> orderItems = new ArrayList<>();
            BigDecimal finalTotal = BigDecimal.ZERO;

            for (CartItemEntity cartItem : cart.getItems()) {
                ProductEntity product = cartItem.getProduct();

                product.decreaseStock(cartItem.getQuantity());

                OrderItemEntity orderItem = orderMapper.buildOrderItem(order, product, cartItem);

                orderItems.add(orderItem);

                BigDecimal itemTotal = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
                finalTotal = finalTotal.add(itemTotal);
            }
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setItems(orderItems);
            order.setTotalAmount(finalTotal);
            order.setSessionId(cart.getSessionId());
            // MVP: Asumimos pagado
            order.setStatus(OrderStatus.PAID);
            order = orderRepository.save(order);

            String orderNum = generateOrderNumber(order);
            order.setOrderNumber(orderNum);

            return orderRepository.save(order);
        });
        eventPublisher.publishEvent(new OrderCreatedEvent(savedOrder, request.getSessionId()));
        return orderMapper.entityToPublicDto(savedOrder);
    }

    /**
     * Para el Formulario "Rastrear mi pedido"
     * Requiere clave compuesta: numero de orden + email.
     * Esto evita que alguien adivine el numero de orden y vea datos ajenos.
     */
    @Transactional(readOnly = true)
    public OrderPublicResponseDTO trackOrder(String orderNumber, String email) {
        return orderRepository.findByOrderNumberAndCustomerEmail(orderNumber, email)
                .map(orderMapper::entityToPublicDto)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro una orden con ese numero y email asociados."));
    }

    /**
     * Busca por el UUID (trackingId)  unico.
     */


    private String generateOrderNumber(OrderEntity order) {
        int year = LocalDate.now().getYear();
        return String.format("ORD-%d-%08d", year, order.getId());
    }

    private boolean isValidMail(String mail) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
        return Pattern.matches(regex, mail);
    }

    //----------------------MÉTODOS PARA ADMIN---------------------//
    @Override
    public OrderAdminResponseDTO getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::entityToAdminDto)
                .orElseThrow(() -> new ResourceNotFoundException("La orden con ID: " + id + " no existe"));
    }

    @Override
    public Page<OrderAdminResponseDTO> getAllOrders(Pageable pageable) {
        return orderRepository
                .findAll(pageable)
                .map(orderMapper::entityToAdminDto);
    }

    @Override
    public Page<OrderAdminResponseDTO> filterOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository
                .findByStatus(status, pageable)
                .map(orderMapper::entityToAdminDto);
    }


    @Transactional
    @Override
    public OrderAdminResponseDTO updateOrderStatus(Long id, OrderStatus newStatus) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada ID: " + id));

        order.setStatus(newStatus);

        return orderMapper.entityToAdminDto(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderAdminResponseDTO> getReport(LocalDateTime start,
                                                 LocalDateTime end,
                                                 List<OrderStatus> statuses,
                                                 Pageable pageable) {
        List<OrderStatus> filterStatuses = (statuses != null && !statuses.isEmpty())
                ? statuses
                : null;

        Page<OrderEntity> orderPage = orderRepository.findByReportFilters(start, end, filterStatuses, pageable);

        return orderPage.map(orderMapper::entityToAdminDto);
    }

    @Override
    public OrderAdminResponseDTO findOrder(String orderNumber) {
        return orderRepository.findSmart(orderNumber)
                .map(orderMapper::entityToAdminDto)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con: " + orderNumber));
    }
}