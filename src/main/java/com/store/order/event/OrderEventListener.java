package com.store.order.event;

import com.store.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final CartRepository cartRepository;

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Orden creada: {}. Limpiando carrito de sesión: {}",
                event.getOrder().getId(), event.getSessionId());

        cartRepository.deleteBySessionId(event.getSessionId());
    }
}