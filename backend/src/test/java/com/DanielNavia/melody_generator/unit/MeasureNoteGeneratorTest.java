package com.DanielNavia.melody_generator.unit;

import com.DanielNavia.melody_generator.model.*;
import com.DanielNavia.melody_generator.music.MeasureNoteGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeasureNoteGeneratorTest {

    /**
     * Se prueba este comportamiento porque el último compás debe resolver siempre
     * en la tónica de la escala, proporcionando un cierre tonal definido a la melodía.
     * La selección de las notas anteriores es aleatoria, pero la nota final debe ser
     * determinista y no depender de esa aleatoriedad.
     */
    @Test
    void shouldEndLastMeasureOnRootNote() {
        Scale scale = new Scale(NoteName.C, Mode.MAJOR);
        MeasureNoteGenerator generator =
                new MeasureNoteGenerator(scale, ScaleDegree.I, new Random());

        List<Duration> durations = List.of(
                Duration.QUARTER,
                Duration.QUARTER,
                Duration.HALF
        );

        List<Note> notes = generator.generateNotes(
                durations,
                true,
                null
        );
        assertEquals(NoteName.C, notes.get(notes.size() - 1).getNote());
    }

    /**
     * Se prueba este comportamiento porque la generación de notas debe respetar
     * exactamente el patrón rítmico recibido. Se comprueban tanto el número de
     * notas como sus duraciones para garantizar que ninguna posición del compás
     * se pierde o altera durante la generación.
     */
    @Test
    void shouldGenerateNotesWithGivenDurations() {
        Scale scale = new Scale(NoteName.C, Mode.MAJOR);
        MeasureNoteGenerator generator = new MeasureNoteGenerator(scale, ScaleDegree.I, new Random());
        List<Duration> durations = List.of(
                Duration.QUARTER,
                Duration.EIGHTH,
                Duration.EIGHTH,
                Duration.HALF
        );
        List<Note> notes = generator.generateNotes(
                durations,
                false,
                null
        );
        assertEquals(durations.size(), notes.size());
        assertEquals(Duration.QUARTER, notes.get(0).getDuration());
        assertEquals(Duration.EIGHTH, notes.get(1).getDuration());
        assertEquals(Duration.EIGHTH, notes.get(2).getDuration());
        assertEquals(Duration.HALF, notes.get(3).getDuration());
    }

    /**
     * Se prueba este comportamiento porque el primer punto de partida de la melodía
     * debe ser una nota perteneciente al acorde actual cuando no existe una nota
     * anterior. Así se garantiza que la melodía comienza dentro del contexto armónico
     * del compás.
     */
    @Test
    void shouldStartWithChordNoteWhenThereIsNoPreviousNote() {
        Scale scale = new Scale(NoteName.C, Mode.MAJOR);
        MeasureNoteGenerator generator = new MeasureNoteGenerator(scale, ScaleDegree.I, new Random());
        List<Duration> durations = List.of(
                Duration.QUARTER,
                Duration.QUARTER,
                Duration.HALF
        );
        List<Note> notes = generator.generateNotes(
                durations,
                false,
                null
        );
        List<NoteName> chordNotes = List.of(
                NoteName.C,
                NoteName.E,
                NoteName.G
        );
        assertTrue(chordNotes.contains(notes.get(0).getNote()));
    }

    /**
     * Se prueba este comportamiento porque una nota anterior que no pertenece a la
     * escala no puede utilizarse como grado de origen para la transición. En ese
     * caso, la generación debe recuperarse seleccionando una nota del acorde actual.
     */
    @Test
    void shouldStartFromChordWhenPreviousNoteIsNotInScale() {
        Scale scale = new Scale(NoteName.C, Mode.MAJOR);
        MeasureNoteGenerator generator = new MeasureNoteGenerator(scale, ScaleDegree.I, new Random());
        List<Duration> durations = List.of(
                Duration.QUARTER,
                Duration.QUARTER,
                Duration.HALF
        );
        List<Note> notes = generator.generateNotes(
                durations,
                false,
                NoteName.D_SHARP
        );
        List<NoteName> chordNotes = List.of(
                NoteName.C,
                NoteName.E,
                NoteName.G
        );
        assertTrue(chordNotes.contains(notes.get(0).getNote()));
    }

    /**
     * Se prueba este comportamiento porque las notas generadas durante un compás
     * deben pertenecer a la escala utilizada. Esta es una condición fundamental
     * de la generación melódica, independientemente de qué transición o nota del
     * acorde resulte seleccionada aleatoriamente.
     */
    @Test
    void shouldGenerateNotesFromScale() {
        Scale scale = new Scale(NoteName.C, Mode.MAJOR);
        MeasureNoteGenerator generator = new MeasureNoteGenerator(scale, ScaleDegree.I, new Random());
        List<Duration> durations = List.of(
                Duration.QUARTER,
                Duration.EIGHTH,
                Duration.EIGHTH,
                Duration.QUARTER,
                Duration.QUARTER
        );
        List<Note> notes = generator.generateNotes(
                durations,
                false,
                NoteName.C
        );
        List<NoteName> scaleNotes = List.of(
                NoteName.C,
                NoteName.D,
                NoteName.E,
                NoteName.F,
                NoteName.G,
                NoteName.A,
                NoteName.B
        );
        for (Note note : notes) {
            assertTrue(scaleNotes.contains(note.getNote()));
        }
    }

    /**
     * Se prueba este comportamiento porque una nota anterior válida debe utilizarse
     * como grado de origen para calcular la transición hacia la siguiente nota.
     * El resultado esperado se diferencia de una selección directa del acorde para
     * garantizar que el test comprueba realmente el uso de la nota anterior.
     */
    @Test
    void shouldUsePreviousNoteAsTransitionOrigin() {
        Scale scale = new Scale(NoteName.C, Mode.MAJOR);

        Random random = new Random() {
            @Override
            public int nextInt(int bound) {
                return 2;
            }
            @Override
            public double nextDouble() {
                return 0.0;
            }
        };
        MeasureNoteGenerator generator = new MeasureNoteGenerator(scale, ScaleDegree.I, random);
        List<Note> notes = generator.generateNotes(
                List.of(Duration.QUARTER),
                false,
                NoteName.D
        );
        assertEquals(NoteName.C, notes.get(0).getNote());
    }

    /**
     * Se prueba este comportamiento porque los tiempos fuertes deben dar prioridad
     * a las notas pertenecientes al acorde. Esto verifica que la generación de notas
     * utiliza el ajuste de pesos definido para reforzar la estabilidad armónica.
     */
    @Test
    void shouldFavorChordNotesOnStrongBeat() {
        Scale scale = new Scale(NoteName.C, Mode.MAJOR);
        Random random = new Random() {
            @Override
            public double nextDouble() {
                return 0.3;
            }
        };
        MeasureNoteGenerator generator = new MeasureNoteGenerator(scale, ScaleDegree.I, random);
        List<Duration> durations = List.of(Duration.QUARTER);
        List<Note> notes = generator.generateNotes(
                durations,
                false,
                NoteName.C
        );
        List<NoteName> chordNotes = List.of(
                NoteName.C,
                NoteName.E,
                NoteName.G
        );
        assertTrue(chordNotes.contains(notes.get(0).getNote()));
    }
}
