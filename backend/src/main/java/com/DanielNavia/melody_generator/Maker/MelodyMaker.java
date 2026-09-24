package com.DanielNavia.melody_generator.Maker;

import com.DanielNavia.melody_generator.model.*;
import com.DanielNavia.melody_generator.music.MeasureGenerator;
import org.springframework.stereotype.Component;
import java.util.*;

@Component

public class MelodyMaker {

    private final List<List<ScaleDegree>> progressionsList = List.of(//Lista de listas de progresiones de acordes
        List.of(ScaleDegree.I, ScaleDegree.IV, ScaleDegree.V, ScaleDegree.I),
        List.of(ScaleDegree.I, ScaleDegree.V, ScaleDegree.VI, ScaleDegree.IV),
        List.of(ScaleDegree.VI, ScaleDegree.II, ScaleDegree.V, ScaleDegree.I),
        List.of(ScaleDegree.I, ScaleDegree.VI, ScaleDegree.II, ScaleDegree.V)
    );
    private final Random random = new Random();
    private final MeasureGenerator measureGenerator = new MeasureGenerator(random);

    /**
     * Genera una melodía completa a partir de la escala indicada.
     *
     * <p>La melodía se construye recorriendo la progresión de acordes
     * seleccionada y generando un compás para cada grado de la progresión.
     * También mantiene la última nota generada para favorecer la continuidad
     * melódica entre compases.</p>
     *
     * @param scale escala musical sobre la que se genera la melodía
     * @return melodía generada con su escala y sus compases
     */
    public Melody generateMelody(Scale scale) {

        List<ScaleDegree> progression =
                progressionsList.get(random.nextInt(progressionsList.size()));

        List<Measure> measures = new ArrayList<>();
        NoteName previousNote = null;
        for (int i = 0;
             i < progression.size();
             i++) {
            ScaleDegree degree = progression.get(i);
            boolean lastMeasure = i == progression.size() - 1;
            Measure measure = measureGenerator.generateMeasure(
                scale,
                degree,
                lastMeasure,
                previousNote
            );
            measures.add(measure);
            if (!measure.getNotes().isEmpty()) {
                previousNote = measure.getNotes().get(measure.getNotes().size() - 1).getNote();
            }
        }
        return new Melody(scale, measures);
    }
}
