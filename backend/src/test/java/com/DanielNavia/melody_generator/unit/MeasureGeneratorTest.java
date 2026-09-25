package com.DanielNavia.melody_generator.unit;

import com.DanielNavia.melody_generator.model.*;
import com.DanielNavia.melody_generator.music.MeasureGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeasureGeneratorTest {

    /**
     * Se prueba este comportamiento porque los compases que no son el último
     * deben utilizar únicamente uno de los patrones rítmicos definidos para
     * la generación normal. Así se garantiza que la selección aleatoria no
     * produzca un patrón distinto de los contemplados por el generador.
     */
    @Test
    void shouldUseValidRhythmPatternForNormalMeasure() {
        MeasureGenerator generator = new MeasureGenerator(new Random());
        Scale scale = new Scale(NoteName.C, Mode.MAJOR);
        Measure measure = generator.generateMeasure(
                scale,
                ScaleDegree.I,
                false,
                null
        );
        List<Duration> durations = measure.getNotes().stream()
                .map(Note::getDuration)
                .toList();
        List<List<Duration>> validPatterns = List.of(
                List.of(Duration.QUARTER, Duration.QUARTER, Duration.HALF),
                List.of(Duration.QUARTER, Duration.EIGHTH, Duration.EIGHTH, Duration.QUARTER, Duration.QUARTER),
                List.of(Duration.HALF, Duration.QUARTER, Duration.QUARTER),
                List.of(Duration.EIGHTH, Duration.EIGHTH, Duration.QUARTER, Duration.QUARTER, Duration.QUARTER)
        );
        assertTrue(validPatterns.contains(durations));
    }

    /**
     * Se prueba este comportamiento porque el último compás debe utilizar siempre
     * el patrón rítmico de cierre definido por el generador, independientemente
     * de la selección aleatoria.
     */
    @Test
    void shouldUseClosingRhythmForLastMeasure() {
        MeasureGenerator generator = new MeasureGenerator(new Random());
        Scale scale = new Scale(NoteName.C, Mode.MAJOR);
        Measure measure = generator.generateMeasure(
                scale,
                ScaleDegree.I,
                true,
                null
        );
        List<Duration> durations = measure.getNotes().stream()
                .map(Note::getDuration)
                .toList();
        assertEquals(
                List.of(
                        Duration.QUARTER,
                        Duration.QUARTER,
                        Duration.HALF
                ),
                durations
        );
    }
}
