package com.payment.paymentservice.domain.dto;

public record PaymentCheckResponse(
        String transactionId,
        String status,
        String authorizationCode,
        String message
) {
}
