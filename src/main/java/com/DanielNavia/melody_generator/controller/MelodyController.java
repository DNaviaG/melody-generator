package com.DanielNavia.melody_generator.controller;

import com.DanielNavia.melody_generator.dto.MelodyRequest;
import com.DanielNavia.melody_generator.dto.MelodyResponse;
import com.DanielNavia.melody_generator.service.MelodyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/melody")
public class MelodyController {

    private final MelodyService melodyService;

    public MelodyController(MelodyService melodyService) {
        this.melodyService = melodyService;
    }

    /**
     * Genera una nueva melodía a partir de la escala recibida en la petición.
     *
     * @param request datos de entrada de la petición, incluyendo la escala
     * @return respuesta que contiene la melodía generada
     */
    @PostMapping("/generate")
    public MelodyResponse generateMelody(@Valid @RequestBody MelodyRequest request) {
        return melodyService.generateMelody(request);
    }
}