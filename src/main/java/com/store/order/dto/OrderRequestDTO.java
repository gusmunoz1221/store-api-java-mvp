package com.store.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderRequestDTO {
    @NotBlank(message = "se requiere ID de la session")
    private String sessionId;

    @NotBlank(message = "el nombre es obligatorio")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$",
            message = "El nombre solo puede contener letras")
    private String customerName;

    @Pattern(regexp = "^(?=.{1,254}$)(?=.{1,64}@)[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,63}$",
            message = "Formato de email inválido")
    private String customerEmail;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$",
            message = "El teléfono debe contener entre 10 y 15 dígitos, opcionalmente con prefijo '+'")
    private String customerPhone;

    // Datos de Envío
    @NotBlank(message = "La dirección es obligatoria")
    @Size(min = 5, max = 200, message = "La dirección debe tener entre 5 y 200 caracteres")
    private String shippingAddress;

    @NotBlank(message = "La ciudad es obligatoria")
    private String shippingCity;

    @Pattern(regexp = "^[A-Za-z]\\d{4}[A-Za-z]{3}$|^\\d{4}$",
            message = "Código postal inválido (ej: 1234 o B1234ABC)")
    private String shippingZip;
}
