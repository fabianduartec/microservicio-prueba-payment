package com.payment.paymentservice.domain.common;

import java.math.BigDecimal;

public class InvalidAmountException extends DomainException{
    public InvalidAmountException(BigDecimal amount) {
        super(
                "PAGO:MONTO_INVALIDO",
                String.format(
                        "El monto debe ser mayor que cero. Valor recibido: %s",
                        amount.toPlainString()
                )
        );
    }
}
