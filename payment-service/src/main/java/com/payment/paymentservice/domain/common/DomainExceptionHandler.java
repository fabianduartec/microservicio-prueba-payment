package com.payment.paymentservice.domain.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class DomainExceptionHandler {
    public final MessageSource messageSource;

    public DomainExceptionHandler(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ErrorResponse> handleMontoMenorOIgualACero(InvalidAmountException ex){
        log.error("Monto no valido: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(ex.getCodigo(), ex.getMessage()) {
                });
    }

    @ExceptionHandler(ExceededAmountException.class)
    public ResponseEntity<ErrorResponse> handleMontoExcedido(ExceededAmountException ex){
        log.error("Monto Excedido: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(ex.getCodigo(), ex.getMessage()) {
                });
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(PaymentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(
                ex.getCodigo(),
                ex.getMessage(),
                404
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        log.warn("Validacion DTO Fallida");
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errores.put(field, message);
        });

        ErrorResponse error = new ErrorResponse(
                "VALIDACION_DTO",
                "Datos de entrada inválidos",
                errores,
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.badRequest().body(error);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);  // Log completo

        ErrorResponse error = new ErrorResponse(
                "ERROR_INTERNO",
                ex.getMessage() + " - " + ex.getClass().getSimpleName(),  // Muestra error real
                500
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AntifraudProviderException.class)
    public ResponseEntity<ErrorResponse> handleAntifraudProvider(AntifraudProviderException ex) {
        log.error("Error en proveedor antifraude: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse(
                        ex.getCodigo(),
                        ex.getMessage(),
                        HttpStatus.SERVICE_UNAVAILABLE.value()
                ));
    }

}
