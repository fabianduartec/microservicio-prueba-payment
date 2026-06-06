package com.payment.paymentservice.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.paymentservice.application.validation.PaymentValidationChain;
import com.payment.paymentservice.cliente.fraude.FraudRisk;
import com.payment.paymentservice.domain.common.AntifraudProviderException;
import com.payment.paymentservice.domain.common.EstadoAuthorization;
import com.payment.paymentservice.domain.common.PaymentNotFoundException;
import com.payment.paymentservice.domain.dto.PaymentAuthorizationRequestDto;
import com.payment.paymentservice.domain.dto.PaymentAuthorizationResponseDto;
import com.payment.paymentservice.domain.dto.PaymentCheckResponse;
import com.payment.paymentservice.domain.model.PaymentAuthorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
public class PaymentService {
    private final PaymentValidationChain validationChain;
    private final CachedFraudService cachedFraudService;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${fraude.cache-minutos}")
    private long cacheMinutes;

    public PaymentService(PaymentValidationChain validationChain,
                                       CachedFraudService cachedFraudService,
                          StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.validationChain = validationChain;
        this.cachedFraudService = cachedFraudService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    public PaymentAuthorizationResponseDto authorize(PaymentAuthorizationRequestDto request) {

        log.info("Aplicando validación de campos");
        validationChain.validate(request);

        PaymentAuthorization authorization = new PaymentAuthorization();
        authorization.setTransactionId(request.transactionId());
        try {
            FraudRisk risk = cachedFraudService.validate(request.transactionId(), request.customerId(), request.amount(), request.currency());
            log.info("Respuesta obtenida desde caché transactionId={}", request.transactionId());
            if (risk == FraudRisk.LOW_RISK) {
                authorization.setStatus(EstadoAuthorization.APPROVED);
                authorization.setAuthorizationCode("AUTH-" + UUID.randomUUID().toString().substring(0, 6));
                authorization.setMessage("Payment authorized");
            } else {
                authorization.setStatus(EstadoAuthorization.REJECTED);
                authorization.setMessage("Payment rejected by business rules");
            }
        } catch (AntifraudProviderException ex) {
            authorization.setStatus(EstadoAuthorization.REJECTED);
            authorization.setAuthorizationCode(null);
            authorization.setMessage("Proveedor antifraude no disponilbe");
        }

        try {
            String json = objectMapper.writeValueAsString(authorization);
            stringRedisTemplate.opsForValue().set(
                    request.transactionId(),
                    json,
                    Duration.ofMinutes(cacheMinutes)
            );
        } catch (Exception e) {
            throw new RuntimeException("Error saving authorization in Redis", e);
        }

        log.info("Resultado de autorización transactionId={} status={}",
                request.transactionId(), authorization.getStatus());
        return new PaymentAuthorizationResponseDto(
                authorization.getTransactionId(),
                authorization.getStatus().name(),
                authorization.getAuthorizationCode(),
                authorization.getMessage()
        );
    }

    public PaymentCheckResponse getByTransactionId(String transactionId) {
        String json = stringRedisTemplate.opsForValue().get(transactionId);
        if (json == null) throw new PaymentNotFoundException("Transaction not found");
        try {
            PaymentAuthorization auth = objectMapper.readValue(json, PaymentAuthorization.class);
            return new PaymentCheckResponse(
                    auth.getTransactionId(),
                    auth.getStatus().name(),
                    auth.getAuthorizationCode(),
                    auth.getMessage()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error reading authorization from Redis", e);
        }
    }
}
