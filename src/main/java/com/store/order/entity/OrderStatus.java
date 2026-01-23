package com.store.order.entity;

public enum OrderStatus{
    PENDING,    // Recién creada, pago no confirmado
    PAID,       // Pago exitoso (MercadoPago aprobado)
    SHIPPED,
    ERROR,
    DELIVERED,  // Entregado al cliente
    CANCELLED   // Pago rechazado o cancelado por ADMIN
}
