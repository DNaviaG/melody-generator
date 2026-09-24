package com.DanielNavia.melody_generator.music;

import com.DanielNavia.melody_generator.model.NoteName;

import java.util.List;
import java.util.Random;

public class MelodyNoteSelector {

    /**
     * Tabla de transición de una Cadena de Markov sobre grados de la escala.
     * TRANSITIONS[i][j] es el peso relativo de moverse del grado i al grado j
     * (0 = I, 1 = II, ..., 6 = VII).
     */
    private static final double[][] TRANSITIONS = {
            {5, 30, 10, 15, 25, 10, 5},
            {20, 5, 25, 15, 15, 15, 5},
            {15, 25, 5, 25, 10, 15, 5},
            {15, 15, 20, 5, 25, 15, 5},
            {20, 10, 10, 20, 5, 20, 15},
            {15, 15, 15, 15, 20, 5, 15},
            {60, 5, 5, 5, 10, 5, 10}
    };

    /**
     * Factor por el que se multiplica el peso de los grados que no pertenecen
     * al acorde en un tiempo fuerte.
     */
    private static final double NON_CHORD_WEIGHT_FACTOR = 0.3;

    /**
     * Selecciona una nota aleatoria del acorde.
     *
     * @param chordNotes notas que forman el acorde
     * @param random generador de números aleatorios
     * @return nota seleccionada aleatoriamente del acorde
     */
    public static NoteName chooseRandomChordNote(
            List<NoteName> chordNotes,
            Random random
    ) {
        return chordNotes.get(random.nextInt(chordNotes.size()));
    }

    /**
     * Obtiene una copia de los pesos de transición de un grado.
     *
     * @param fromDegree grado de origen
     * @return pesos de transición correspondientes al grado
     */
    public static double[] getTransitionWeights(int fromDegree) {
        return TRANSITIONS[fromDegree].clone();
    }

    /**
     * Ajusta los pesos de transición para un tiempo fuerte.
     *
     * <p>Se crea una copia de los pesos recibidos para evitar modificar
     * el array original.</p>
     *
     * <p>Se reduce el peso de los grados que no pertenecen al acorde.</p>
     *
     * @param weights pesos de transición originales
     * @param scaleNotes notas de la escala
     * @param chordNotes notas del acorde
     * @return copia de los pesos con el ajuste aplicado
     */
    public static double[] adjustWeightsForStrongBeat(
            double[] weights,
            List<NoteName> scaleNotes,
            List<NoteName> chordNotes
    ) {
        double[] adjustedWeights = weights.clone();

        for (int degree = 0; degree < scaleNotes.size(); degree++) {
            if (!chordNotes.contains(scaleNotes.get(degree))) {
                adjustedWeights[degree] *= NON_CHORD_WEIGHT_FACTOR;
            }
        }

        return adjustedWeights;
    }

    /**
     * Selecciona un grado de la escala utilizando los pesos de transición.
     *
     * @param weights pesos de transición
     * @param random generador de números aleatorios
     * @return grado seleccionado o -1 si no se selecciona ninguno
     */
    public static int selectWeightedDegree(
            double[] weights,
            Random random
    ) {
        double totalWeight = 0;

        for (double weight : weights) {
            totalWeight += weight;
        }

        double pick = random.nextDouble() * totalWeight;
        double cumulative = 0;

        for (int degree = 0; degree < weights.length; degree++) {
            cumulative += weights[degree];

            if (pick < cumulative) {
                return degree;
            }
        }

        return -1;
    }
}