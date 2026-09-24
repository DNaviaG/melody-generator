import { Injectable, signal } from '@angular/core';
import * as Tone from 'tone';

import { MelodyObject, Note } from '../models/melody.model';
import { NOTE_SYMBOLS, DURATION_TO_TONE } from '../models/melody-constants';

// Todo lo relacionado con reproducir audio (Tone.js) vive aquí.
// El componente ya no sabe nada de osciladores, Transport, ni Part:
// solo le pide a este servicio "reproduce esta melodía" o "pausa".
@Injectable({
  providedIn: 'root',
})
export class MelodyPlayerService {

  // Estos dos signals son los únicos que el componente necesita leer
  isPlaying = signal(false);
  currentNote = signal<{ measure: number; note: number } | null>(null); 

  private synth: Tone.Synth | null = null;
  private part: Tone.Part | null = null;

  // Reloj maestro y utilidad de sincronización visual de Tone.js (v15+)
  private transport = Tone.getTransport();
  private draw = Tone.getDraw();

  // Construye la Part: la lista de notas traducidas y programadas en el tiempo
  private buildPart(melody: MelodyObject, tempo: number) {
    this.transport.bpm.value = tempo;

    this.synth = new Tone.Synth().toDestination();

    let time = 0;
    const events: { time: number; note: string; duration: string; measureIndex: number; noteIndex: number }[] = [];

    melody.measures.forEach((measure, measureIndex) => {
      measure.notes.forEach((note: Note, noteIndex) => {
        const pitch = `${NOTE_SYMBOLS[note.note]}${note.octave}`;
        const duration = DURATION_TO_TONE[note.duration] ?? '4n';

        events.push({ time, note: pitch, duration, measureIndex, noteIndex });
        time += Tone.Time(duration).toSeconds();
      });
    });

    this.part = new Tone.Part((playTime, event) => {
      this.synth!.triggerAttackRelease(event.note, event.duration, playTime);

      this.draw.schedule(() => {
        this.currentNote.set({ measure: event.measureIndex, note: event.noteIndex });
      }, playTime);
    }, events).start(0);

    this.part.loop = false;

    this.transport.scheduleOnce(() => {     
      this.stop();     
    }, time);
  }

  // Reproduce (o reanuda) la melodía dada. Si ya hay una Part construida
  // (venimos de pausa), no se reconstruye: se reanuda tal cual estaba.
  async play(melody: MelodyObject, tempo: number) {
    if (this.isPlaying()) return;

    await Tone.start(); // desbloquea el audio del navegador (requiere gesto del usuario)

    if (!this.part) {
      this.buildPart(melody, tempo);
    }

    this.transport.start();
    this.isPlaying.set(true);
    }

    pause() {
        this.transport.pause();
        this.isPlaying.set(false);
    }

    setTempo(value: number) {
        this.transport.bpm.value = value;
    }

    // Para el audio del todo y libera recursos. Se llama al generar una
    // melodía nueva, al hacer reset, o al destruir el componente.
    stop() {
        this.transport.stop();
        // Cancelar eventos pendientes del Transport
        this.transport.cancel(0);
        // Cancelar actualizaciones visuales pendientes
        this.draw.cancel(0);
        this.transport.position = 0;
        this.part?.dispose();
        this.synth?.dispose();
        this.part = null;
        this.synth = null;
        this.isPlaying.set(false);
        this.currentNote.set(null);
    }
}