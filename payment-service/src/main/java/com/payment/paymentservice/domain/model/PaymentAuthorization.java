package com.payment.paymentservice.domain.model;

import com.payment.paymentservice.domain.common.EstadoAuthorization;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentAuthorization {
    private String transactionId;
    private EstadoAuthorization status;
    private String authorizationCode;
    private String message;
}
