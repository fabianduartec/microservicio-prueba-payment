package com.payment.paymentservice.infrastructure.controller;

import com.payment.paymentservice.application.service.PaymentService;
import com.payment.paymentservice.domain.dto.PaymentAuthorizationRequestDto;
import com.payment.paymentservice.domain.dto.PaymentAuthorizationResponseDto;
import com.payment.paymentservice.domain.dto.PaymentCheckResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {
    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/authorize")
    public ResponseEntity<PaymentAuthorizationResponseDto> authorize(@Valid @RequestBody PaymentAuthorizationRequestDto request) {
        log.info("Solicitud recibida transactionId={}", request.transactionId());
        return ResponseEntity.ok(service.authorize(request));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<PaymentCheckResponse> getByTransactionId(@PathVariable String transactionId) {
        return ResponseEntity.ok(service.getByTransactionId(transactionId));
    }
}
