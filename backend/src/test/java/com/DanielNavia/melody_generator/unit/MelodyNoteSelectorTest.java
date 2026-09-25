package com.DanielNavia.melody_generator.unit;

import com.DanielNavia.melody_generator.model.NoteName;
import com.DanielNavia.melody_generator.music.MelodyNoteSelector;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MelodyNoteSelectorTest {

    /**
     * Se prueba este comportamiento porque las notas seleccionadas por este método
     * deben pertenecer siempre al acorde proporcionado. Devolver una nota externa
     * al acorde introduciría una nota musical incorrecta en la melodía generada.
     */
    @Test
    void shouldReturnNoteFromChord() {
        List<NoteName> chordNotes = List.of(
                NoteName.C,
                NoteName.E,
                NoteName.G
        );
        NoteName selectedNote = MelodyNoteSelector.chooseRandomChordNote(chordNotes, new Random());
        assertTrue(chordNotes.contains(selectedNote));
    }

    /**
     * Se prueba este comportamiento porque los pesos de transición determinan
     * las probabilidades relativas utilizadas para seleccionar el siguiente grado.
     * También se comprueba que el resultado sea una copia para evitar que código
     * externo pueda modificar accidentalmente la tabla de transición interna.
     */
    @Test
    void shouldReturnCorrectTransitionWeightsWithoutExposingInternalData() {
        double[] weights = MelodyNoteSelector.getTransitionWeights(0);

        assertArrayEquals(new double[]{5, 30, 10, 15, 25, 10, 5}, weights);
        weights[0] = 999;
        assertArrayEquals(new double[]{5, 30, 10, 15, 25, 10, 5}, MelodyNoteSelector.getTransitionWeights(0));
    }

    /**
     * Se prueba este comportamiento porque en los tiempos fuertes las notas que
     * no pertenecen al acorde deben tener una probabilidad menor de ser seleccionadas.
     * También se comprueba que los pesos originales no sean modificados, ya que el
     * método debe trabajar sobre una copia para evitar efectos secundarios.
     */
    @Test
    void shouldReduceNonChordWeightsWithoutModifyingOriginalWeights() {
        double[] weights = {10, 20, 30, 40, 50, 60, 70};
        List<NoteName> scaleNotes = List.of(
                NoteName.C,
                NoteName.D,
                NoteName.E,
                NoteName.F,
                NoteName.G,
                NoteName.A,
                NoteName.B
        );
        List<NoteName> chordNotes = List.of(
                NoteName.C,
                NoteName.E,
                NoteName.G
        );
        double[] adjustedWeights = MelodyNoteSelector.adjustWeightsForStrongBeat(weights,scaleNotes,chordNotes);
        assertArrayEquals(new double[]{10, 6, 30, 12, 50, 18, 21}, adjustedWeights);
        assertArrayEquals(new double[]{10, 20, 30, 40, 50, 60, 70}, weights);
    }

    /**
     * Se prueba este comportamiento porque este método selecciona el siguiente
     * grado de la escala utilizando los pesos de transición recibidos: cada grado
     * ocupa una parte del rango total proporcional a su peso, y el valor aleatorio
     * determina en qué parte del rango cae la selección. Este comportamiento es
     * importante porque controla cómo se aplican las probabilidades de transición
     * durante la generación de la melodía.
     */
    @Test
    void shouldSelectDegreeAccordingToWeights() {

        // Los pesos indican cuánto espacio ocupa cada grado dentro del rango total.
        // En este caso:
        // grado 0 → 10
        // grado 1 → 20
        // grado 2 → 30
        double[] weights = {10, 20, 30};

        /*
         * Creamos un Random controlado únicamente para este test.
         *
         * Normalmente nextDouble() devuelve un valor aleatorio entre 0.0 y 1.0.
         * Aquí lo sobrescribimos para devolver siempre 0.0.
         *
         * Esto nos permite saber exactamente qué valor recibe el método y hace
         * que el test sea determinista.
         */
        Random random = new Random() {
            @Override
            public double nextDouble() {
                return 0.0;
            }
        };
        /*
         * totalWeight = 10 + 20 + 30 = 60
         * pick = 0.0 * 60 = 0
         *
         * El 0 pertenece al primer rango, por lo que esperamos el grado 0.
         */
        assertEquals(0, MelodyNoteSelector.selectWeightedDegree(weights, random));

        /*
         * Ahora hacemos que el valor aleatorio sea 0.2.
         *
         * pick = 0.2 * 60 = 12
         *
         * El primer rango termina en 10 y el segundo llega hasta 30.
         * Por tanto, el 12 pertenece al segundo rango y esperamos el grado 1.
         */
        Random secondRandom = new Random() {
            @Override
            public double nextDouble() {
                return 0.2;
            }
        };
        assertEquals(1, MelodyNoteSelector.selectWeightedDegree(weights, secondRandom));

        /*
         * Finalmente hacemos que el valor aleatorio sea 0.7.
         *
         * pick = 0.7 * 60 = 42
         *
         * El tercer rango va de 30 hasta 60, por lo que esperamos el grado 2.
         */
        Random thirdRandom = new Random() {
            @Override
            public double nextDouble() {
                return 0.7;
            }
        };
        assertEquals(2, MelodyNoteSelector.selectWeightedDegree(weights, thirdRandom));
    }
}
