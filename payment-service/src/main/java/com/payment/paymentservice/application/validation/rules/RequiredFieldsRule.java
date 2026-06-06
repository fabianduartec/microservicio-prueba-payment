package com.payment.paymentservice.application.validation.rules;

import com.payment.paymentservice.application.validation.PaymentValidationRule;
import com.payment.paymentservice.domain.common.DomainException;
import com.payment.paymentservice.domain.dto.PaymentAuthorizationRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
public class RequiredFieldsRule implements PaymentValidationRule {
    @Override
    public void validate(PaymentAuthorizationRequestDto request) {
        if (request == null
                || request.transactionId() == null
                || request.customerId() == null) {
            throw new DomainException(
                    "CAMPOS_REQUERIDOS",
                    "Faltan campos requeridos"
            );
        }
    }
}
