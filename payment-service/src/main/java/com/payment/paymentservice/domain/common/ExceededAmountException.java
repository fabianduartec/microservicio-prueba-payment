package com.payment.paymentservice.domain.common;

import java.math.BigDecimal;

public class ExceededAmountException extends DomainException{
    public ExceededAmountException(BigDecimal amount, BigDecimal maxAmount) {
        super(
                "PAGO:MONTO_EXCEDIDO",
                String.format(
                        "El monto excede el limite permitido. Limite: %s,Solicitado: %s",
                        maxAmount.toPlainString(),
                        amount.toPlainString()
                )
        );
    }
}
