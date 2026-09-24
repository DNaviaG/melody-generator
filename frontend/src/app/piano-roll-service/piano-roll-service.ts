import { Injectable } from '@angular/core';

import { MelodyObject } from '../models/melody.model';
import { DURATION_BEATS, PIANO_ROLL_NOTES } from '../models/melody-constants';

export interface PianoRollBlock {
  id: string;
  measureIndex: number;
  noteIndex: number;
  top: number;
  left: number;
  width: number;
  label: string;
}

// Toda la lógica de "convertir una melodía en coordenadas para pintar el
// piano roll" vive aquí. El componente no calcula nada, solo le pide a
// este servicio los bloques y las líneas divisorias ya calculados.
@Injectable({
  providedIn: 'root',
})
export class PianoRollService {

  readonly noteNames = PIANO_ROLL_NOTES;
  readonly rowHeight = 28; // píxeles por fila, debe coincidir con el CSS

  // Convierte la melodía en bloques posicionados (top/left/width en %)
  buildBlocks(melody: MelodyObject, formatLabel: (note: string, octave: number) => string): PianoRollBlock[] {
    const totalBeats = this.getTotalBeats(melody);
    let beat = 0;
    const blocks: PianoRollBlock[] = [];

    melody.measures.forEach((measure, measureIndex) => {
      measure.notes.forEach((note, noteIndex) => {
        const durationBeats = DURATION_BEATS[note.duration] ?? 1;
        const rowIndex = this.noteNames.indexOf(note.note);

        blocks.push({
          id: `${measureIndex}-${noteIndex}`,
          measureIndex,
          noteIndex,
          top: rowIndex * this.rowHeight,
          left: (beat / totalBeats) * 100,
          width: (durationBeats / totalBeats) * 100,
          label: formatLabel(note.note, note.octave)
        });

        beat += durationBeats;
      });
    });

    return blocks;
  }

  // Posiciones (en %) donde pintar la línea divisoria entre compases
  buildMeasureDividers(melody: MelodyObject): number[] {
    const totalBeats = this.getTotalBeats(melody);
    let beat = 0;
    const dividers: number[] = [];

    melody.measures.forEach((measure, index) => {
      if (index > 0) {
        dividers.push((beat / totalBeats) * 100);
      }
      measure.notes.forEach(note => {
        beat += DURATION_BEATS[note.duration] ?? 1;
      });
    });

    return dividers;
  }

  private getTotalBeats(melody: MelodyObject): number {
    return melody.measures
      .flatMap(m => m.notes)
      .reduce((sum, note) => sum + (DURATION_BEATS[note.duration] ?? 1), 0);
  }
}