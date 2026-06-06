package com.payment.paymentservice.domain.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String codigo;
    private String mensaje;

    private Map<String, String> detalles;
    private LocalDateTime timestamp;
    private int status;

    public ErrorResponse(String codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.detalles = Map.of();
        this.timestamp = LocalDateTime.now();
        this.status = 400;
    }

    public ErrorResponse(String codigo, String mensaje, int status) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.detalles = Map.of();
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }
}
