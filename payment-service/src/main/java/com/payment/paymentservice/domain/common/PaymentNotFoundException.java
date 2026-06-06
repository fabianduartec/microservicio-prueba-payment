package com.payment.paymentservice.domain.common;

public class PaymentNotFoundException extends DomainException{
    public PaymentNotFoundException(String transactionId){
        super("PAGO_NO_ENCONTRADO",
                String.format("Pago no encontrado: %s", transactionId));
    }

}
