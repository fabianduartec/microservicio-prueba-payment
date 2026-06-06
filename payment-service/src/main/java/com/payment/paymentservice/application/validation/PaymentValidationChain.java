package com.payment.paymentservice.application.validation;

import com.payment.paymentservice.domain.dto.PaymentAuthorizationRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentValidationChain {
    private final List<PaymentValidationRule> rules;

    public PaymentValidationChain(List<PaymentValidationRule> rules) {
        this.rules = rules;
    }

    public void validate(PaymentAuthorizationRequestDto request) {
        for (PaymentValidationRule rule : rules) {
            rule.validate(request);
        }
    }
}
