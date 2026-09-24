package com.DanielNavia.melody_generator.exception;

import com.DanielNavia.melody_generator.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestiona los errores producidos durante la generación de una melodía.
     *
     * @param exception excepción producida durante la generación
     * @param request petición HTTP que originó el error
     * @return respuesta HTTP con estado 500 y los detalles del error
     */
    @ExceptionHandler(MelodyGenerationException.class)
    public ResponseEntity<ErrorResponse> handleMelodyGenerationException(
            MelodyGenerationException exception,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Melody Generation Error",
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    /**
     * Gestiona las peticiones cuyo cuerpo no puede ser convertido
     * al objeto esperado por la aplicación.
     *
     * @param exception excepción producida al leer el cuerpo de la petición
     * @param request petición HTTP que originó el error
     * @return respuesta HTTP con estado 400 y los detalles del error
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "La petición contiene datos no válidos",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}