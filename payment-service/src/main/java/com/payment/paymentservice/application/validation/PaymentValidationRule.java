package com.payment.paymentservice.application.validation;

import com.payment.paymentservice.domain.dto.PaymentAuthorizationRequestDto;

public interface PaymentValidationRule {
    void validate(PaymentAuthorizationRequestDto request);
}
