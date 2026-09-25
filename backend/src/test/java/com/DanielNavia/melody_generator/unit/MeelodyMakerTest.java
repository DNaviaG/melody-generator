package com.DanielNavia.melody_generator.unit;

import com.DanielNavia.melody_generator.Maker.MelodyMaker;
import com.DanielNavia.melody_generator.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MeelodyMakerTest {

    /**
     * Se prueba este comportamiento porque cada progresión disponible contiene
     * cuatro grados y, por tanto, la melodía generada debe estar formada siempre
     * por cuatro compases, independientemente de la progresión seleccionada.
     */
    @Test
    void shouldGenerateMelodyWithFourMeasures() {
        MelodyMaker melodyMaker = new MelodyMaker();
        Scale scale = new Scale(NoteName.C, Mode.MAJOR);
        Melody melody = melodyMaker.generateMelody(scale);
        assertEquals(4, melody.getMeasures().size());
    }

    /**
     * Se prueba este comportamiento porque MelodyMaker debe identificar correctamente
     * el último compás al recorrer la progresión. Esta condición permite que la
     * generación aplique la regla de cierre tonal en la última medida de la melodía.
     */
    @Test
    void shouldEndMelodyOnRootNote() {
        MelodyMaker melodyMaker = new MelodyMaker();
        Scale scale = new Scale(NoteName.C, Mode.MAJOR);
        Melody melody = melodyMaker.generateMelody(scale);
        List<Measure> measures = melody.getMeasures();
        Measure lastMeasure = measures.get(measures.size() - 1);
        Note lastNote = lastMeasure.getNotes().get(lastMeasure.getNotes().size() - 1);
        assertEquals(NoteName.C, lastNote.getNote());
    }

    /**
     * Se prueba este comportamiento porque la melodía generada debe mantener
     * la escala utilizada durante toda la generación. Así se garantiza que el
     * resultado está asociado al contexto musical solicitado.
     */
    @Test
    void shouldKeepProvidedScale() {
        MelodyMaker melodyMaker = new MelodyMaker();
        Scale scale = new Scale(NoteName.C, Mode.MINOR);
        Melody melody = melodyMaker.generateMelody(scale);
        assertEquals(scale, melody.getScale());
    }
}
