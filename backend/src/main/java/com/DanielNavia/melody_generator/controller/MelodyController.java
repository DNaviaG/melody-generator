package com.DanielNavia.melody_generator.controller;

import com.DanielNavia.melody_generator.model.Melody;
import com.DanielNavia.melody_generator.model.Scale;
import com.DanielNavia.melody_generator.service.MelodyService;
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
     * @param scale datos de entrada de la petición
     * @return melodía generada
     */
    @PostMapping("/generate")
    public Melody generateMelody(@RequestBody Scale scale) {
        return melodyService.generateMelody(scale);
    }
}