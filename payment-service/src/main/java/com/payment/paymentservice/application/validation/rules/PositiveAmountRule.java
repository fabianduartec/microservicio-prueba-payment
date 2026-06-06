package com.payment.paymentservice.application.validation.rules;

import com.payment.paymentservice.application.validation.PaymentValidationRule;
import com.payment.paymentservice.domain.common.InvalidAmountException;
import com.payment.paymentservice.domain.dto.PaymentAuthorizationRequestDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PositiveAmountRule implements PaymentValidationRule {
    @Override
    public void validate(PaymentAuthorizationRequestDto request) {
        BigDecimal amount = request.amount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(amount != null ? amount : BigDecimal.ZERO);
        }
    }
}
