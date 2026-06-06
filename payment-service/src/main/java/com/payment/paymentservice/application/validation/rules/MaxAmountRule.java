package com.payment.paymentservice.application.validation.rules;

import com.payment.paymentservice.application.validation.PaymentValidationRule;
import com.payment.paymentservice.domain.common.ExceededAmountException;
import com.payment.paymentservice.domain.dto.PaymentAuthorizationRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MaxAmountRule implements PaymentValidationRule {
    @Value("${pago.monto-max}")
    private BigDecimal max;

    @Override
    public void validate(PaymentAuthorizationRequestDto request) {
        BigDecimal amount = request.amount();
        if (amount != null && amount.compareTo(max) > 0) {
            throw new ExceededAmountException(amount, max);
        }
    }
}
