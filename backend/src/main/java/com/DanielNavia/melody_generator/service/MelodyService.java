package com.DanielNavia.melody_generator.service;

import com.DanielNavia.melody_generator.Maker.MelodyMaker;
import com.DanielNavia.melody_generator.model.Melody;
import com.DanielNavia.melody_generator.model.Scale;
import org.springframework.stereotype.Service;

@Service
public class MelodyService {

    private final MelodyMaker melodyMaker;

    public MelodyService(MelodyMaker melodyMaker) {
        this.melodyMaker = melodyMaker;
    }

    public Melody generateMelody(Scale scale) {

        return melodyMaker.generateMelody(scale);
    }
}