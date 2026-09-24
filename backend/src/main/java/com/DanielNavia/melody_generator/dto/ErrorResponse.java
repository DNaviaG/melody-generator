package com.DanielNavia.melody_generator.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO utilizado para representar la información de un error HTTP
 * devuelto por la API.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}