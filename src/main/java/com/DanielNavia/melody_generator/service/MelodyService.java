package com.DanielNavia.melody_generator.service;

import com.DanielNavia.melody_generator.Maker.MelodyMaker;
import com.DanielNavia.melody_generator.dto.MelodyRequest;
import com.DanielNavia.melody_generator.dto.MelodyResponse;
import com.DanielNavia.melody_generator.model.Melody;
import org.springframework.stereotype.Service;

@Service
public class MelodyService {

    private final MelodyMaker melodyMaker;

    public MelodyService(MelodyMaker melodyMaker) {
        this.melodyMaker = melodyMaker;
    }

    public MelodyResponse generateMelody(MelodyRequest request) {

        Melody melody = melodyMaker.generateMelody(
                request.getScale()
        );

        return new MelodyResponse(melody);
    }
}