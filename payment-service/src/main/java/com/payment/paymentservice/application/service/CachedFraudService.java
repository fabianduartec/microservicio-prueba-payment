package com.payment.paymentservice.application.service;

import com.payment.paymentservice.cliente.fraude.FraudRisk;
import com.payment.paymentservice.domain.common.AntifraudProviderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class CachedFraudService {
    private final FraudRiskService fraudRiskService;

    public CachedFraudService(FraudRiskService fraudRiskService) {
        this.fraudRiskService = fraudRiskService;
    }

    @Cacheable(cacheNames = "transactions", key = "#transactionId")
    public FraudRisk validate(String transactionId, String customerId, BigDecimal amount, String currency) {
        try {
            return fraudRiskService.checkRisk(transactionId, customerId, amount, currency).get().getRisk();
        }  catch (AntifraudProviderException ex) {
            log.warn("Error consultando antifraude, no fue posible consultar proveedor");
            throw ex;
        }catch (Exception ex) {
            log.warn("Error consultando antifraude transactionId={} motivo={}", transactionId, ex.getMessage());
            return FraudRisk.HIGH_RISK;
        }
    }
}
