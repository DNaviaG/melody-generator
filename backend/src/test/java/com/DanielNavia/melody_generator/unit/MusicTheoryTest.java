package com.DanielNavia.melody_generator.unit;

import com.DanielNavia.melody_generator.model.*;
import com.DanielNavia.melody_generator.music.MusicTheory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MusicTheoryTest {

    /**
     * Se prueba este comportamiento porque los intervalos de los grados
     * son una regla fundamental de la lógica musical. Se comprueban ambos
     * modos porque utilizan fórmulas diferentes y un error en cualquiera
     * de ellos afecta a la construcción posterior de escalas y acordes.
     */
    @Test
    void shouldReturnCorrectIntervalsForMajorAndMinorScales() {
        Scale majorScale = new Scale(NoteName.C, Mode.MAJOR);
        Scale minorScale = new Scale(NoteName.C, Mode.MINOR);

        assertEquals(0, MusicTheory.getScaleInterval(majorScale, ScaleDegree.I));
        assertEquals(2, MusicTheory.getScaleInterval(majorScale, ScaleDegree.II));
        assertEquals(4, MusicTheory.getScaleInterval(majorScale, ScaleDegree.III));
        assertEquals(5, MusicTheory.getScaleInterval(majorScale, ScaleDegree.IV));
        assertEquals(7, MusicTheory.getScaleInterval(majorScale, ScaleDegree.V));
        assertEquals(9, MusicTheory.getScaleInterval(majorScale, ScaleDegree.VI));
        assertEquals(11, MusicTheory.getScaleInterval(majorScale, ScaleDegree.VII));

        assertEquals(0, MusicTheory.getScaleInterval(minorScale, ScaleDegree.I));
        assertEquals(2, MusicTheory.getScaleInterval(minorScale, ScaleDegree.II));
        assertEquals(3, MusicTheory.getScaleInterval(minorScale, ScaleDegree.III));
        assertEquals(5, MusicTheory.getScaleInterval(minorScale, ScaleDegree.IV));
        assertEquals(7, MusicTheory.getScaleInterval(minorScale, ScaleDegree.V));
        assertEquals(8, MusicTheory.getScaleInterval(minorScale, ScaleDegree.VI));
        assertEquals(10, MusicTheory.getScaleInterval(minorScale, ScaleDegree.VII));
    }

    /**
     * Se prueba este comportamiento porque la construcción de una escala
     * es un resultado de dominio utilizado directamente durante la generación
     * de melodías. Se comprueban ambos modos porque producen conjuntos de notas
     * diferentes y un error afectaría a las notas disponibles para la melodía.
     */
    @Test
    void shouldReturnCorrectNotesForMajorAndMinorScales() {
        Scale majorScale = new Scale(NoteName.C, Mode.MAJOR);
        Scale minorScale = new Scale(NoteName.C, Mode.MINOR);

        assertEquals(
                List.of(
                        NoteName.C,
                        NoteName.D,
                        NoteName.E,
                        NoteName.F,
                        NoteName.G,
                        NoteName.A,
                        NoteName.B
                ),
                MusicTheory.getScaleNotes(majorScale)
        );
        assertEquals(
                List.of(
                        NoteName.C,
                        NoteName.D,
                        NoteName.D_SHARP,
                        NoteName.F,
                        NoteName.G,
                        NoteName.G_SHARP,
                        NoteName.A_SHARP
                ),
                MusicTheory.getScaleNotes(minorScale)
        );
    }

    /**
     * Se prueba este comportamiento porque la calidad de cada acorde depende
     * del grado y del modo de la escala, y determina la armonía utilizada
     * durante la generación de la melodía. Se comprueban ambos modos porque
     * sus reglas de construcción son diferentes.
     */
    @Test
    void shouldReturnCorrectChordQualitiesForMajorAndMinorScales() {
        Scale majorScale = new Scale(NoteName.C, Mode.MAJOR);
        Scale minorScale = new Scale(NoteName.C, Mode.MINOR);

        assertEquals(ChordQuality.MAJOR, MusicTheory.getChordQuality(majorScale, ScaleDegree.I));
        assertEquals(ChordQuality.MINOR, MusicTheory.getChordQuality(majorScale, ScaleDegree.II));
        assertEquals(ChordQuality.MINOR, MusicTheory.getChordQuality(majorScale, ScaleDegree.III));
        assertEquals(ChordQuality.MAJOR, MusicTheory.getChordQuality(majorScale, ScaleDegree.IV));
        assertEquals(ChordQuality.MAJOR, MusicTheory.getChordQuality(majorScale, ScaleDegree.V));
        assertEquals(ChordQuality.MINOR, MusicTheory.getChordQuality(majorScale, ScaleDegree.VI));
        assertEquals(ChordQuality.DIMINISHED, MusicTheory.getChordQuality(majorScale, ScaleDegree.VII));

        assertEquals(ChordQuality.MINOR, MusicTheory.getChordQuality(minorScale, ScaleDegree.I));
        assertEquals(ChordQuality.DIMINISHED, MusicTheory.getChordQuality(minorScale, ScaleDegree.II));
        assertEquals(ChordQuality.MAJOR, MusicTheory.getChordQuality(minorScale, ScaleDegree.III));
        assertEquals(ChordQuality.MINOR, MusicTheory.getChordQuality(minorScale, ScaleDegree.IV));
        assertEquals(ChordQuality.MINOR, MusicTheory.getChordQuality(minorScale, ScaleDegree.V));
        assertEquals(ChordQuality.MAJOR, MusicTheory.getChordQuality(minorScale, ScaleDegree.VI));
        assertEquals(ChordQuality.MAJOR, MusicTheory.getChordQuality(minorScale, ScaleDegree.VII));
    }

    /**
     * Se prueba este comportamiento porque la construcción de un acorde
     * determina las notas que el generador considera notas de acorde.
     * Se comprueban las tres calidades porque cada una utiliza intervalos
     * diferentes para construir la tercera y la quinta.
     */
    @Test
    void shouldReturnCorrectNotesForEachChordQuality() {
        assertEquals(List.of(NoteName.C, NoteName.E, NoteName.G),
                MusicTheory.getChordNotes(NoteName.C, ChordQuality.MAJOR));

        assertEquals(List.of(NoteName.C, NoteName.D_SHARP, NoteName.G),
                MusicTheory.getChordNotes(NoteName.C, ChordQuality.MINOR));

        assertEquals(List.of(NoteName.C, NoteName.D_SHARP, NoteName.F_SHARP),
                MusicTheory.getChordNotes(NoteName.C, ChordQuality.DIMINISHED));
    }

    /**
     * Se prueba este comportamiento porque el cálculo debe mantener correctamente
     * el ciclo de las doce notas al aplicar un intervalo, especialmente cuando
     * el resultado supera el límite del conjunto de notas.
     */
    @Test
    void shouldReturnCorrectNoteForInterval() {
        assertEquals(NoteName.E, MusicTheory.getNoteFromInterval(NoteName.C, 4));
        assertEquals(NoteName.C, MusicTheory.getNoteFromInterval(NoteName.B, 1));
    }
}