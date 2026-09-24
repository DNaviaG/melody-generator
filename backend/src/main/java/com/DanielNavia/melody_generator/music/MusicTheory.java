package com.DanielNavia.melody_generator.music;

import com.DanielNavia.melody_generator.model.*;

import java.util.Arrays;
import java.util.List;

public class MusicTheory {

    private static final int SEMITONES_PER_OCTAVE = 12;

    /**
     * Obtiene el intervalo, en semitonos, entre la tónica de la escala
     * y el grado indicado.
     *
     * @param scale  escala musical utilizada
     * @param degree grado de la escala cuyo intervalo se desea obtener
     * @return número de semitonos desde la tónica hasta el grado indicado
     */
    public static int getScaleInterval(Scale scale, ScaleDegree degree) {
        if (isMajor(scale)){
            return switch (degree) {
                case I -> 0;
                case II -> 2;
                case III -> 4;
                case IV -> 5;
                case V -> 7;
                case VI -> 9;
                case VII -> 11;
            };
        }
        return switch (degree) {
            case I -> 0;
            case II -> 2;
            case III -> 3;
            case IV -> 5;
            case V -> 7;
            case VI -> 8;
            case VII -> 10;
        };
    }

    /**
     * Determina la calidad del acorde correspondiente a un grado de la escala.
     *
     * @param scale  escala musical utilizada
     * @param degree grado de la escala sobre el que se construye el acorde
     * @return calidad del acorde: mayor, menor o disminuido
     */
    public static ChordQuality getChordQuality(Scale scale, ScaleDegree degree) {
        if (isMajor(scale)) {
            return switch (degree) {
                case I, IV, V -> ChordQuality.MAJOR;
                case II, III, VI -> ChordQuality.MINOR;
                case VII -> ChordQuality.DIMINISHED;
            };
        }
        return switch (degree) {
            case I, IV, V -> ChordQuality.MINOR;
            case II -> ChordQuality.DIMINISHED;
            case III, VI, VII -> ChordQuality.MAJOR;
        };
    }

    /**
     * Obtiene las notas que forman un acorde a partir de su nota raíz
     * y de su calidad.
     *
     * @param rootNote nota raíz del acorde
     * @param chordQuality calidad del acorde: mayor, menor o disminuido
     * @return lista con la raíz, tercera y quinta del acorde
     */
    public static List<NoteName> getChordNotes(NoteName rootNote, ChordQuality chordQuality) {
        return List.of(
                rootNote,
                getNoteFromInterval(rootNote, chordQuality.getThirdInterval()),
                getNoteFromInterval(rootNote, chordQuality.getFifthInterval())
        );
    }

    /**
     * Calcula la nota resultante al aplicar un intervalo de semitonos
     * a una nota raíz.
     *
     * @param rootNote nota de referencia desde la que se aplica el intervalo
     * @param interval intervalo en semitonos
     * @return nota resultante del intervalo
     */
    public static NoteName getNoteFromInterval(NoteName rootNote, int interval) {
        int notePosition = Math.floorMod(rootNote.ordinal() + interval, SEMITONES_PER_OCTAVE);
        return NoteName.values()[notePosition];
    }

    /**
     * Obtiene las siete notas que forman la escala indicada.
     *
     * @param scale escala musical utilizada
     * @return lista de notas correspondientes a los siete grados de la escala
     */
    public static List<NoteName> getScaleNotes(Scale scale) {
        return Arrays.stream(ScaleDegree.values())
                .map(degree -> getNoteFromInterval(
                        scale.getRootNote(),
                        getScaleInterval(scale, degree)
                ))
                .toList();
    }

    /**
     * Indica si la escala es de modo mayor.
     *
     * @param scale escala musical utilizada
     * @return {@code true} si la escala es mayor, {@code false} si es menor
     */
    private static boolean isMajor(Scale scale) {
        return scale.getMode() == Mode.MAJOR;
    }
}
