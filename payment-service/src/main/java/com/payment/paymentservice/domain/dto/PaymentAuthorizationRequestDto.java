package com.payment.paymentservice.domain.dto;

import com.payment.paymentservice.domain.common.MetodoPago;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentAuthorizationRequestDto(
        @NotBlank(message = "transactionId es obligatorio")
        String transactionId,
        @NotBlank(message = "customerId es obligatorio")
        String customerId,
        @NotNull(message = "amount es obligatorio")
        @Positive(message = "amount debe ser mayor que cero")
        BigDecimal amount,
        @NotBlank(message = "currency es obligatorio")
        String currency,
        @NotBlank(message = "merchantId es obligatorio")
        String merchantId,
        @NotNull(message = "paymentMethod es obligatorio")
        MetodoPago paymentMethod
) {
}
