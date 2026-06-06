package com.payment.paymentservice.domain.common;

public class AntifraudProviderException extends DomainException {
    public AntifraudProviderException() {
        super(
                "PAGO:ANTIFRAUD_PROVIDER_ERROR",
                "No fue posible consultar el proveedor antifraude."
        );
    }
}