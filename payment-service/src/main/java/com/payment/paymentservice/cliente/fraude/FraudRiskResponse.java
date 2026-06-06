package com.payment.paymentservice.cliente.fraude;

public class FraudRiskResponse {
    private final FraudRisk risk;
    private final String message;

    public FraudRiskResponse(FraudRisk risk, String message) {
        this.risk = risk;
        this.message = message;
    }

    public FraudRisk getRisk() {
        return risk;
    }

    public String getMessage() {
        return message;
    }
}
