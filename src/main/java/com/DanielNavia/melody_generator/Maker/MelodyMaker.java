package com.DanielNavia.melody_generator.Maker;

import com.DanielNavia.melody_generator.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;
import javax.sound.midi.*;
import java.io.File;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MelodyMaker {

    // ==================== CONFIGURACIÓN ====================

    private final List<List<ScaleDegree>> progressionsList = List.of(//Lista de listas de progresiones de acordes
            List.of(ScaleDegree.I, ScaleDegree.IV, ScaleDegree.V, ScaleDegree.I),
            List.of(ScaleDegree.I, ScaleDegree.V, ScaleDegree.VI, ScaleDegree.IV),
            List.of(ScaleDegree.VI, ScaleDegree.II, ScaleDegree.V, ScaleDegree.I),
            List.of(ScaleDegree.I, ScaleDegree.VI, ScaleDegree.II, ScaleDegree.V)
    );
    private final Random random = new Random();
    List<ScaleDegree> progression = progressionsList.get(random.nextInt(progressionsList.size()));//Coge una de las listas


    // ==================== ESCALAS Y ACORDES ====================

    /**
     * Obtiene el intervalo, en semitonos, entre la tónica de la escala
     * y el grado indicado.
     *
     * @param scale  escala musical utilizada
     * @param degree grado de la escala cuyo intervalo se desea obtener
     * @return número de semitonos desde la tónica hasta el grado indicado
     */
    private static int getScaleInterval(Scale scale, ScaleDegree degree) {
        if (scale.name().endsWith("MAJOR")) {
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
    private static String getChordQuality(Scale scale, ScaleDegree degree) {
        if (scale.name().endsWith("MAJOR")) {
            return switch (degree) {
                case I, IV, V -> "MAJOR";
                case II, III, VI -> "MINOR";
                case VII -> "DIMINISHED";
            };
        }

        return switch (degree) {
            case I, IV, V -> "MINOR";
            case II -> "DIMINISHED";
            case III, VI, VII -> "MAJOR";
        };
    }

    /**
     * Calcula la nota resultante al aplicar un intervalo de semitonos
     * a una nota raíz.
     *
     * @param rootNote nota de referencia desde la que se aplica el intervalo
     * @param interval intervalo en semitonos
     * @return nota resultante del intervalo
     */
    private static NoteName getNoteFromInterval(NoteName rootNote, int interval) {
        int notePosition = (rootNote.ordinal() + interval) % 12;
        return NoteName.values()[notePosition];
    }

    /**
     * Obtiene las notas que forman un acorde a partir de su nota raíz
     * y de su calidad.
     *
     * @param rootNote nota raíz del acorde
     * @param chordQuality calidad del acorde: mayor, menor o disminuido
     * @return lista con la raíz, tercera y quinta del acorde
     * @throws IllegalArgumentException si la calidad del acorde no es válida
     */
    private static List<NoteName> getChordNotes(NoteName rootNote, String chordQuality) {
        int thirdInterval;
        int fifthInterval;

        switch (chordQuality) {
            case "MAJOR" -> {
                thirdInterval = 4;
                fifthInterval = 7;
            }
            case "MINOR" -> {
                thirdInterval = 3;
                fifthInterval = 7;
            }
            case "DIMINISHED" -> {
                thirdInterval = 3;
                fifthInterval = 6;
            }
            default -> throw new IllegalArgumentException("Tipo de acorde no válido");
        }

        NoteName third = getNoteFromInterval(rootNote, thirdInterval);
        NoteName fifth = getNoteFromInterval(rootNote, fifthInterval);

        return List.of(rootNote, third, fifth);
    }

    /**
     * Obtiene las siete notas que forman la escala a partir de su tónica.
     *
     * @param rootNote nota tónica de la escala
     * @param scale escala musical utilizada
     * @return lista de notas correspondientes a los siete grados de la escala
     */
    private static List<NoteName> getScaleNotes(NoteName rootNote, Scale scale) {
        return List.of(
                getNoteFromInterval(rootNote, getScaleInterval(scale, ScaleDegree.I)),
                getNoteFromInterval(rootNote, getScaleInterval(scale, ScaleDegree.II)),
                getNoteFromInterval(rootNote, getScaleInterval(scale, ScaleDegree.III)),
                getNoteFromInterval(rootNote, getScaleInterval(scale, ScaleDegree.IV)),
                getNoteFromInterval(rootNote, getScaleInterval(scale, ScaleDegree.V)),
                getNoteFromInterval(rootNote, getScaleInterval(scale, ScaleDegree.VI)),
                getNoteFromInterval(rootNote, getScaleInterval(scale, ScaleDegree.VII))
        );
    }

    // ==================== GENERACIÓN DE LA MELODÍA ====================

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

        List<Measure> measures =
                new ArrayList<>();

        NoteName previousNote = null;

        for (int i = 0;
             i < progression.size();
             i++) {

            ScaleDegree degree =
                    progression.get(i);

            boolean lastMeasure =
                    i == progression.size() - 1;

            Measure measure =
                    generateMeasure(
                            scale,
                            degree,
                            lastMeasure,
                            previousNote,
                            i
                    );

            measures.add(measure);

            if (!measure.getNotes().isEmpty()) {

                previousNote =
                        measure.getNotes()
                                .get(
                                        measure.getNotes().size() - 1
                                )
                                .getNote();
            }
        }

        Melody melody =
                new Melody(
                        scale,
                        measures
                );
        //Borrar luego de pruebas
        try {
            generateMidi(melody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return melody;
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
     * @param measureIndex posición del compás dentro de la melodía, comenzando en 0
     * @return compás generado con sus notas
     */
    private Measure generateMeasure(
            Scale scale,
            ScaleDegree degree,
            boolean lastMeasure,
            NoteName previousNote,
            int measureIndex
    ) {
        NoteName rootNote = NoteName.valueOf(
                scale.name()
                        .replace("_MAJOR", "")
                        .replace("_MINOR", "")
        );

        List<NoteName> scaleNotes =
                getScaleNotes(rootNote, scale);

        NoteName chordRoot =
                getNoteFromInterval(
                        rootNote,
                        getScaleInterval(scale, degree)
                );

        String chordQuality =
                getChordQuality(scale, degree);

        List<NoteName> chordNotes =
                getChordNotes(
                        chordRoot,
                        chordQuality
                );

        /*
         * Patrones rítmicos.
         */
        List<List<Duration>> rhythmPatterns = List.of(

                List.of(
                        Duration.QUARTER,
                        Duration.QUARTER,
                        Duration.HALF
                ),

                List.of(
                        Duration.QUARTER,
                        Duration.EIGHTH,
                        Duration.EIGHTH,
                        Duration.QUARTER,
                        Duration.QUARTER
                ),

                List.of(
                        Duration.HALF,
                        Duration.QUARTER,
                        Duration.QUARTER
                ),

                List.of(
                        Duration.EIGHTH,
                        Duration.EIGHTH,
                        Duration.QUARTER,
                        Duration.QUARTER,
                        Duration.QUARTER
                )
        );

        List<Duration> durations =
                rhythmPatterns.get(
                        random.nextInt(rhythmPatterns.size())
                );

        /*
         * El último compás tiene un ritmo más estable.
         */
        if (lastMeasure) {
            durations = List.of(
                    Duration.QUARTER,
                    Duration.QUARTER,
                    Duration.HALF
            );
        }

        List<Note> notes =
                new ArrayList<>();

        NoteName currentNote =
                previousNote;

        /*
         * Posición actual dentro del compás.
         *
         * 0.0 = beat 1
         * 1.0 = beat 2
         * 2.0 = beat 3
         * 3.0 = beat 4
         */
        double currentBeat = 0.0;

        for (int i = 0; i < durations.size(); i++) {

            Duration duration =
                    durations.get(i);

            /*
             * Un tiempo es fuerte cuando la nota
             * comienza en el beat 1 o en el beat 3.
             */
            boolean strongBeat =
                    currentBeat == 0.0
                            || currentBeat == 2.0;

            /*
             * Última nota de la melodía:
             * resolución absoluta a la tónica.
             */
            if (lastMeasure
                    && i == durations.size() - 1) {

                currentNote =
                        rootNote;

            } else {

                /*
                 * Primer compás:
                 * presentamos el motivo.
                 */
                if (measureIndex == 0
                        && currentNote == null) {

                    currentNote =
                            chooseMotifNote(
                                    scaleNotes,
                                    chordNotes,
                                    null,
                                    strongBeat,
                                    random
                            );

                } else {

                    currentNote =
                            chooseMotifNote(
                                    scaleNotes,
                                    chordNotes,
                                    currentNote,
                                    strongBeat,
                                    random
                            );
                }
            }

            notes.add(
                    new Note(
                            currentNote,
                            4,
                            duration
                    )
            );

            /*
             * Avanzamos la posición dentro del compás.
             */
            currentBeat += duration.getBeats();
        }

        /*
         * En el tercer compás buscamos el clímax.
         */
        if (measureIndex == 2
                && !notes.isEmpty()) {

            NoteName highNote =
                    chordNotes.get(
                            chordNotes.size() - 1
                    );

            Note climax =
                    new Note(
                            highNote,
                            4,
                            notes.get(0).getDuration()
                    );

            notes.set(0, climax);
        }

        return new Measure(notes);
    }

    /**
     * Selecciona la siguiente nota de la melodía teniendo en cuenta
     * el tiempo del compás, las notas del acorde y la nota anterior.
     *
     * <p>En los tiempos fuertes se priorizan notas pertenecientes
     * al acorde. En los tiempos débiles se favorece el movimiento
     * conjunto dentro de la escala y, ocasionalmente, la repetición
     * de la nota anterior.</p>
     *
     * @param scaleNotes notas de la escala utilizada para generar la melodía
     * @param chordNotes notas del acorde correspondiente al compás
     * @param previousNote nota generada anteriormente, si existe
     * @param strongBeat indica si la nota comienza en un tiempo fuerte
     * @param random generador utilizado para seleccionar aleatoriamente entre las opciones disponibles
     * @return nota seleccionada para continuar la melodía
     */
    private static NoteName chooseMotifNote(
            List<NoteName> scaleNotes,
            List<NoteName> chordNotes,
            NoteName previousNote,
            boolean strongBeat,
            Random random
    ){
        /*
         * Las posiciones importantes del motivo
         * prefieren notas del acorde.
         */
        if (strongBeat) {

            List<NoteName> candidates =
                    new ArrayList<>(chordNotes);

            /*
             * Evitamos repetir constantemente
             * la misma nota.
             */
            if (previousNote != null && candidates.size() > 1) {
                candidates.remove(previousNote);
            }

            return candidates.get(
                    random.nextInt(candidates.size())
            );
        }

        /*
         * En posiciones débiles buscamos principalmente
         * movimiento conjunto.
         */
        if (previousNote != null) {

            int index =
                    scaleNotes.indexOf(previousNote);

            List<NoteName> candidates =
                    new ArrayList<>();

            if (index > 0) {
                candidates.add(
                        scaleNotes.get(index - 1)
                );
            }

            if (index < scaleNotes.size() - 1) {
                candidates.add(
                        scaleNotes.get(index + 1)
                );
            }

            /*
             * Ocasionalmente permitimos volver a la
             * misma nota para crear repetición rítmica.
             */
            if (random.nextInt(4) == 0) {
                candidates.add(previousNote);
            }

            return candidates.get(
                    random.nextInt(candidates.size())
            );
        }

        return chordNotes.get(
                random.nextInt(chordNotes.size())
        );
    }

    // ==================== GENERACIÓN MIDI ====================

    /**
     * Genera un archivo MIDI a partir de la melodía proporcionada.
     *
     * <p>Convierte las notas y sus duraciones en eventos MIDI y los
     * añade a una única pista. Los silencios avanzan la posición temporal
     * sin generar eventos de nota.</p>
     *
     * @param melody melodía que se convertirá en un archivo MIDI
     * @throws Exception si se produce un error durante la creación o escritura del archivo MIDI
     */
    private static void generateMidi(Melody melody) throws Exception {

        int ticksPerQuarterNote = 480;

        Sequence sequence = new Sequence(
                Sequence.PPQ,
                ticksPerQuarterNote
        );

        Track track = sequence.createTrack();

        int currentTick = 0;

        for (Measure measure : melody.getMeasures()) {

            for (Note note : measure.getNotes()) {

                int durationTicks =
                        (int) (note.getDuration().getBeats() * ticksPerQuarterNote);

                // Si es un silencio, simplemente avanzamos el tiempo
                if (note.getNote() == NoteName.REST) {
                    currentTick += durationTicks;
                    continue;
                }

                int midiNote =
                        (note.getOctave() + 1) * 12
                                + note.getNote().ordinal();

                ShortMessage noteOn = new ShortMessage();
                noteOn.setMessage(
                        ShortMessage.NOTE_ON,
                        0,
                        midiNote,
                        100
                );

                ShortMessage noteOff = new ShortMessage();
                noteOff.setMessage(
                        ShortMessage.NOTE_OFF,
                        0,
                        midiNote,
                        0
                );

                track.add(
                        new MidiEvent(noteOn, currentTick)
                );

                track.add(
                        new MidiEvent(
                                noteOff,
                                currentTick + durationTicks
                        )
                );

                currentTick += durationTicks;
            }
        }
        File midiFile = new File("generated-melody.mid");
        MidiSystem.write(
                sequence,
                1,
                midiFile
        );
    }

}
