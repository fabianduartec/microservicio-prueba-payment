package com.payment.paymentservice.application.service;

import com.payment.paymentservice.cliente.fraude.FraudRisk;
import com.payment.paymentservice.cliente.fraude.FraudRiskResponse;
import com.payment.paymentservice.cliente.fraude.IFraudProviderClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class FraudRiskService {
    private final IFraudProviderClient iFraudProviderClient;

    public FraudRiskService(IFraudProviderClient iFraudProviderClient) {
        this.iFraudProviderClient = iFraudProviderClient;
    }

    @CircuitBreaker(name = "fraudProvider", fallbackMethod = "fallbackRisk")
    @TimeLimiter(name = "fraudProvider")
    public CompletableFuture<FraudRiskResponse> checkRisk(String transactionId, String customerId, BigDecimal amount, String currency) {
        return CompletableFuture.supplyAsync(() -> iFraudProviderClient.checkFraud(transactionId, customerId, amount, currency));
    }

    public CompletableFuture<FraudRiskResponse> fallbackRisk(String transactionId, String customerId, BigDecimal amount, String currency, Throwable ex) {
        log.warn("Fallback activado transactionId={} motivo={}", transactionId, ex.getMessage());
        return CompletableFuture.completedFuture(new FraudRiskResponse(FraudRisk.HIGH_RISK, "Fallback: proveedor no disponible")
        );
    }
}
