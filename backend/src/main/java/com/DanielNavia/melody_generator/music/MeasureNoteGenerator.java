package com.DanielNavia.melody_generator.music;

import com.DanielNavia.melody_generator.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Genera las notas que forman un compás a partir de su contexto musical.
 */
public class MeasureNoteGenerator {

    private final Random random;
    private final NoteName rootNote;
    private final List<NoteName> scaleNotes;
    private final List<NoteName> chordNotes;

    /**
     * Crea un generador de notas para una escala y un grado concretos.
     *
     * @param scale escala musical utilizada para generar las notas
     * @param degree grado de la progresión sobre el que se construye el acorde
     * @param random generador aleatorio utilizado durante la selección de notas
     */
    public MeasureNoteGenerator(
            Scale scale,
            ScaleDegree degree,
            Random random
    ) {
        this.random = random;
        this.rootNote = scale.getRootNote();
        this.scaleNotes = MusicTheory.getScaleNotes(scale);
        this.chordNotes = getChordNotes(scale, degree);
    }
    /**
     * Obtiene las notas del acorde correspondiente a un grado de la escala.
     *
     * <p>Determina la raíz y la calidad del acorde a partir de la escala y del
     * grado proporcionados, y obtiene las notas que lo forman.</p>
     *
     * @param scale escala musical utilizada para determinar el acorde
     * @param degree grado de la progresión sobre el que se construye el acorde
     * @return notas que forman el acorde
     */
    private List<NoteName> getChordNotes(Scale scale, ScaleDegree degree) {
        NoteName chordRoot = MusicTheory.getNoteFromInterval(
                scale.getRootNote(),
                MusicTheory.getScaleInterval(scale, degree)
        );

        ChordQuality chordQuality = MusicTheory.getChordQuality(scale, degree);

        return MusicTheory.getChordNotes(chordRoot, chordQuality);
    }

    /**
     * Selecciona la siguiente nota de la melodía a partir de la nota actual.
     *
     * <p>La selección tiene en cuenta si existe una nota anterior, si la nota actual
     * pertenece a la escala y si la posición corresponde a un tiempo fuerte.</p>
     *
     * @param currentNote nota actual de la melodía
     * @param strongBeat indica si la siguiente nota se encuentra en un tiempo fuerte
     * @return nota seleccionada para la siguiente posición
     */
    private NoteName chooseNextNote(NoteName currentNote, boolean strongBeat) {

        if (currentNote == null) {
            return MelodyNoteSelector.chooseRandomChordNote(chordNotes, random);
        }
        int fromDegree = scaleNotes.indexOf(currentNote);
        if (fromDegree == -1) {
            return MelodyNoteSelector.chooseRandomChordNote(chordNotes, random);
        }
        double[] weights = MelodyNoteSelector.getTransitionWeights(fromDegree);

        if (strongBeat) {
            weights = MelodyNoteSelector.adjustWeightsForStrongBeat(
                    weights,
                    scaleNotes,
                    chordNotes
            );
        }
        int selectedDegree = MelodyNoteSelector.selectWeightedDegree(weights, random);
        if (selectedDegree == -1) {
            return scaleNotes.get(fromDegree);
        }
        return scaleNotes.get(selectedDegree);
    }

    /**
     * Genera las notas del compás a partir del patrón rítmico y del estado anterior.
     *
     * @param durations duraciones que forman el ritmo del compás
     * @param lastMeasure indica si es el último compás
     * @param previousNote última nota del compás anterior
     * @return lista de notas generadas
     */
    public List<Note> generateNotes(
            List<Duration> durations,
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
            } else {
                currentNote = chooseNextNote(currentNote, strongBeat);
            }
            notes.add(new Note(currentNote, 4, duration));
            currentBeat += duration.getBeats();
        }
        return notes;
    }
}