package com.DanielNavia.melody_generator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Note {

    private NoteName note;
    private int octave;
    private Duration duration;
}