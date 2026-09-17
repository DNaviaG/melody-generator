package com.DanielNavia.melody_generator.controller;

import com.DanielNavia.melody_generator.dto.MelodyRequest;
import com.DanielNavia.melody_generator.dto.MelodyResponse;
import com.DanielNavia.melody_generator.model.Melody;
import com.DanielNavia.melody_generator.Maker.MelodyMaker;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;


@RestController //Le dice a Spring que esta clase va a gestionar peticiones HTTP de la API.
@RequestMapping("/api/v1/melody") //Establece la ruta base del controlador.
public class MelodyController {

    /**
     * Genera una nueva melodía a partir de la escala recibida en la petición.
     *
     * @param request datos de entrada de la petición, incluyendo la escala
     * @return respuesta que contiene la melodía generada
     */
    @PostMapping("/generate")
    public MelodyResponse generateMelody(@Valid @RequestBody MelodyRequest request) {
        MelodyMaker melodyMaker = new MelodyMaker();
        Melody melody = melodyMaker.generateMelody(request.getScale());
        return new MelodyResponse(melody);
    }
}