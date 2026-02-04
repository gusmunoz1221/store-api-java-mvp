package com.store.order.event;

import com.store.order.entity.OrderEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderCreatedEvent {
    private final OrderEntity order;
    private final String sessionId;
}
