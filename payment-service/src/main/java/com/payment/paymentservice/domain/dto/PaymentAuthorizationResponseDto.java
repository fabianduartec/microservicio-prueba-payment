package com.payment.paymentservice.domain.dto;

public record PaymentAuthorizationResponseDto(
        String transactionId,
        String status,
        String authorizationCode,
        String message
) {
}
