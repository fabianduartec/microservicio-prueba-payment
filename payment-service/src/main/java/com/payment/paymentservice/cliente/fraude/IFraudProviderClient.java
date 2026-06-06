package com.payment.paymentservice.cliente.fraude;

import java.math.BigDecimal;

public interface IFraudProviderClient {
    FraudRiskResponse checkFraud(String transactionId, String customerId, BigDecimal amount, String currency);
}
