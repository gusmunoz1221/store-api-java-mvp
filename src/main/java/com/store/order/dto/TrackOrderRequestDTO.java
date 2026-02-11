package com.store.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrackOrderRequestDTO {
    @NotBlank(message = "El número de orden es obligatorio")
    private String orderNumber;
    @NotBlank
    @Email(message = "Debes confirmar el email de la compra")
    private String email;
}