package com.DanielNavia.melody_generator.music;

import com.DanielNavia.melody_generator.model.*;

import java.util.ArrayList;
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
     * Genera las notas de un compás a partir del patrón rítmico y del contexto musical.
     *
     * @param durations duraciones que forman el ritmo del compás
     * @param rootNote tónica de la escala
     * @param scaleNotes notas de la escala
     * @param chordNotes notas del acorde actual
     * @param lastMeasure indica si es el último compás
     * @param previousNote última nota del compás anterior
     * @return lista de notas generadas
     */
    private List<Note> generateNotes(
            List<Duration> durations,
            NoteName rootNote,
            List<NoteName> scaleNotes,
            List<NoteName> chordNotes,
            boolean lastMeasure,
            NoteName previousNote
    ) {
        List<Note> notes = new ArrayList<>();
        NoteName currentNote = previousNote;
        double currentBeat = 0.0;

        for (int i = 0; i < durations.size(); i++) {
            Duration duration = durations.get(i);
            boolean strongBeat = currentBeat == 0.0 || currentBeat == 2.0;

            if (lastMeasure && i == durations.size() - 1) {
                currentNote = rootNote;
            } else if (currentNote == null) {
                currentNote = MelodyNoteSelector.chooseRandomChordNote(chordNotes, random);
            } else {
                int fromDegree = scaleNotes.indexOf(currentNote);
                if (fromDegree == -1) {
                    currentNote = MelodyNoteSelector.chooseRandomChordNote(chordNotes, random);
                } else {
                    double[] weights = MelodyNoteSelector.getTransitionWeights(fromDegree);
                    if (strongBeat) {
                        weights = MelodyNoteSelector.adjustWeightsForStrongBeat(weights,scaleNotes,chordNotes);
                    }
                    int selectedDegree = MelodyNoteSelector.selectWeightedDegree(weights, random);
                    if (selectedDegree == -1) {
                        currentNote = scaleNotes.get(fromDegree);
                    } else {
                        currentNote = scaleNotes.get(selectedDegree);
                    }
                }
            }
            notes.add(new Note(currentNote, 4, duration));
            currentBeat += duration.getBeats();
        }
        return notes;
    }

    /**
     * Genera un compás de la melodía a partir de un grado de la progresión.
     *
     * <p>El compás determina las notas de la escala y del acorde correspondiente,
     * selecciona un patrón rítmico y genera las notas teniendo en cuenta la nota
     * anterior, los tiempos fuertes y la posición del compás dentro de la melodía.</p>
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
        NoteName rootNote = scale.getRootNote();// Obtiene la tónica de la escala.
        List<NoteName> scaleNotes = MusicTheory.getScaleNotes(scale);// Obtiene las notas de la escala.
        NoteName chordRoot = MusicTheory.getNoteFromInterval(rootNote, MusicTheory.getScaleInterval(scale, degree)); // Obtiene la nota raíz del acorde.
        ChordQuality chordQuality = MusicTheory.getChordQuality(scale, degree);// Obtiene el tipo de acorde.
        List<NoteName> chordNotes = MusicTheory.getChordNotes(chordRoot, chordQuality);// Obtiene las notas del acorde.
        List<Duration> durations = chooseDurations(lastMeasure);// Elige el ritmo del compás.

        List<Note> notes = generateNotes(// Genera las notas del compás.
            durations,
            rootNote,
            scaleNotes,
            chordNotes,
            lastMeasure,
            previousNote
        );
        return new Measure(notes);
    }
}
