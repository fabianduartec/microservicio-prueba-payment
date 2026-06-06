package com.payment.paymentservice.cliente.fraude;

import com.payment.paymentservice.domain.common.AntifraudProviderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Slf4j
@Component
public class FraudProviderClientImpl implements IFraudProviderClient{
    @Override
    public FraudRiskResponse checkFraud(String transactionId, String customerId, BigDecimal amount, String currency) {
        log.info("Autorizando transactionId={}", transactionId);

        if ("FAIL".equalsIgnoreCase(customerId)) {
            throw new AntifraudProviderException();
        }

        if ("TIMEOUT".equalsIgnoreCase(customerId)) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AntifraudProviderException();
            }
        }

        boolean highAmount = amount.compareTo(new BigDecimal("500000")) >= 0;
        boolean suspiciousCustomer = customerId != null && customerId.endsWith("9");
        boolean suspiciousTransaction = transactionId != null && transactionId.endsWith("000");
        boolean suspiciousCurrency = currency != null && !currency.equals("COP");

        if (highAmount || suspiciousCustomer || suspiciousTransaction || suspiciousCurrency) {
            return new FraudRiskResponse(
                    FraudRisk.HIGH_RISK,
                    "Simulated antifraud provider: high risk"
            );
        }

        return new FraudRiskResponse(
                FraudRisk.LOW_RISK,
                "Simulated antifraud provider: low risk"
        );
    }
}
