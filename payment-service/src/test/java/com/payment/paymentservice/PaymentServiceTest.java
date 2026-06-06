package com.payment.paymentservice;

import com.payment.paymentservice.application.service.CachedFraudService;
import com.payment.paymentservice.application.service.PaymentService;
import com.payment.paymentservice.application.validation.PaymentValidationChain;
import com.payment.paymentservice.domain.common.MetodoPago;
import com.payment.paymentservice.domain.dto.PaymentAuthorizationRequestDto;
import com.payment.paymentservice.domain.model.PaymentAuthorization;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentValidationChain validationChain;

    @Mock
    private CachedFraudService cachedFraudService;

    @Mock
    private RedisTemplate<String, PaymentAuthorization> redisTemplate;

    @Mock
    private ValueOperations<String, PaymentAuthorization> valueOperations;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void serviceNotNull() {
        assertThat(paymentService).isNotNull();
    }

    @Test
    void autorizarPagoMontoCeroLanzaExcepcion() {
        PaymentAuthorizationRequestDto dto = new PaymentAuthorizationRequestDto(
                "TX-1002",
                "CUST-001",
                BigDecimal.ZERO,
                "COP",
                "MER-900",
                MetodoPago.CARD
        );

        doThrow(new IllegalArgumentException("Monto inválido")).when(validationChain).validate(dto);

        assertThatThrownBy(() -> paymentService.authorize(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Monto inválido");
    }

    @Test
    void autorizarPagoMontoExcedidoLanzaExcepcion() {
        PaymentAuthorizationRequestDto dto = new PaymentAuthorizationRequestDto(
                "TX-1003",
                "CUST-001",
                new BigDecimal("999999"),
                "COP",
                "MER-900",
                MetodoPago.CARD
        );

        doThrow(new IllegalArgumentException("Supera el límite")).when(validationChain).validate(dto);

        assertThatThrownBy(() -> paymentService.authorize(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Supera el límite");
    }

    @Test
    void autorizarPagoProveedorFallaLanzaExcepcion() {
        PaymentAuthorizationRequestDto dto = new PaymentAuthorizationRequestDto(
                "TX-1005",
                "CUST-001",
                new BigDecimal("250000"),
                "COP",
                "MER-900",
                MetodoPago.CARD
        );

        doNothing().when(validationChain).validate(dto);
        when(cachedFraudService.validate(anyString(), anyString(), any(), anyString()))
                .thenThrow(new RuntimeException("Proveedor antifraude no disponible"));

        assertThatThrownBy(() -> paymentService.authorize(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Proveedor antifraude no disponible");
    }
}
