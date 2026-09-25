package com.DanielNavia.melody_generator.music;

import com.DanielNavia.melody_generator.model.*;

import java.util.List;
import java.util.Random;

public class MeasureGenerator {

    private final Random random;

    public MeasureGenerator(Random random) {
        this.random = random;
    }

    private static final List<List<Duration>> RHYTHM_PATTERNS = List.of(
            List.of(Duration.QUARTER, Duration.QUARTER, Duration.HALF),
            List.of(Duration.QUARTER, Duration.EIGHTH, Duration.EIGHTH, Duration.QUARTER, Duration.QUARTER),
            List.of(Duration.HALF, Duration.QUARTER, Duration.QUARTER),
            List.of(Duration.EIGHTH, Duration.EIGHTH, Duration.QUARTER, Duration.QUARTER, Duration.QUARTER)
    );

    /**
     * Selecciona el patrón rítmico que utilizará el compás.
     *
     * <p>Los compases normales utilizan un patrón aleatorio entre los disponibles.
     * El último compás utiliza siempre un patrón estable de cierre.</p>
     *
     * @param lastMeasure indica si el compás es el último de la melodía
     * @return lista de duraciones que formará el patrón rítmico del compás
     */
    private List<Duration> chooseDurations(boolean lastMeasure) {
        if (lastMeasure) {
            return List.of(Duration.QUARTER, Duration.QUARTER, Duration.HALF);
        }

        return RHYTHM_PATTERNS.get(random.nextInt(RHYTHM_PATTERNS.size()));
    }

    /**
     * Genera un compás de la melodía a partir de un grado de la progresión.
     *
     * <p>Selecciona el patrón rítmico del compás y delega la generación de sus
     * notas en {@link MeasureNoteGenerator}.</p>
     *
     * @param scale escala musical utilizada para generar el compás
     * @param degree grado de la progresión sobre el que se construye el acorde
     * @param lastMeasure indica si el compás es el último de la melodía
     * @param previousNote última nota generada en el compás anterior
     * @return compás generado con sus notas
     */
    public Measure generateMeasure(
            Scale scale,
            ScaleDegree degree,
            boolean lastMeasure,
            NoteName previousNote
    ) {
        List<Duration> durations = chooseDurations(lastMeasure);// Elige el ritmo del compás.
        MeasureNoteGenerator noteGenerator = new MeasureNoteGenerator(scale, degree, random);
        List<Note> notes = noteGenerator.generateNotes(
                durations,
                lastMeasure,
                previousNote
        );
        return new Measure(notes);
    }
}
