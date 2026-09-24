import { Component, OnDestroy, computed, signal } from '@angular/core';

import { MelodyObject, Scale } from '../models/melody.model';
import { MelodyService } from '../melody-service/melody-service';
import { MelodyPlayerService } from '../melody-player-service/melody-player-service';
import { PianoRollService } from '../piano-roll-service/piano-roll-service';

@Component({
  selector: 'app-melody',
  templateUrl: './melody.html',
  styleUrl: './melody.css'
})
export class Melody implements OnDestroy {

  isPlaying!: typeof this.melodyPlayer.isPlaying;
  currentNote!: typeof this.melodyPlayer.currentNote;

  constructor(
    private melodyService: MelodyService,
    private melodyPlayer: MelodyPlayerService,
    private pianoRoll: PianoRollService
  ) {
    this.isPlaying = this.melodyPlayer.isPlaying;
    this.currentNote = this.melodyPlayer.currentNote;
    this.pianoRollNotes = this.pianoRoll.noteNames;
  }

  // --- Estado general del componente ---

  tempo = signal(120);

  melodyObject = signal<MelodyObject | null>(null);

  selectedScale = signal<Scale>({
    rootNote: 'C',
    mode: 'MAJOR'
  });

  noteNames = [
    'C', 'C_SHARP', 'D', 'D_SHARP',
    'E', 'F', 'F_SHARP', 'G',
    'G_SHARP', 'A', 'A_SHARP', 'B'
  ];

  pianoRollNotes!: typeof this.pianoRoll.noteNames;

  // --- Piano roll ---

  pianoRollBlocks = computed(() => {
    const melody = this.melodyObject();

    if (!melody) return [];

    return this.pianoRoll.buildBlocks(
      melody,
      (note, octave) => this.formatNoteName(note) + octave
    );
  });

  measureDividers = computed(() => {
    const melody = this.melodyObject();

    if (!melody) return [];

    return this.pianoRoll.buildMeasureDividers(melody);
  });

  // --- Generación ---

  generateMelody() {
    this.melodyPlayer.stop();

    this.melodyService.generateMelody(this.selectedScale()).subscribe({
      next: response => {
        this.melodyObject.set(response);
      },
      error: () => {
        this.melodyObject.set(null);
      }
    });
  }

  resetMelody() {
    this.melodyPlayer.stop();
    this.melodyObject.set(null);
  }

  // --- Formateo ---

  formatScaleName(scale: Scale): string {
    const root = this.formatNoteName(scale.rootNote);

    return `${root} ${scale.mode}`;
  }

  formatNoteName(note: string): string {
    return note.replace('_SHARP', '♯');
  }

  // --- Reproducción ---

  isCurrentNote(measureIndex: number, noteIndex: number): boolean {
    const current = this.currentNote();

    return current !== null &&
      current.measure === measureIndex &&
      current.note === noteIndex;
  }

  playMelody() {
    const melody = this.melodyObject();

    if (!melody) return;

    this.melodyPlayer.play(melody, this.tempo());
  }

  pauseMelody() {
    this.melodyPlayer.pause();
  }

  stopMelody() {
    this.melodyPlayer.stop();
  }

  changeTempo(value: number) {
    this.tempo.set(value);
    this.melodyPlayer.setTempo(value);
  }

  formatScaleValue(scale: Scale): string {
    return `${scale.rootNote}_${scale.mode}`;
  }

  changeScale(value: string) {
    const separatorIndex = value.lastIndexOf('_');

    this.selectedScale.set({
      rootNote: value.substring(0, separatorIndex),
      mode: value.substring(separatorIndex + 1)
    });
  }

  ngOnDestroy() {
    this.melodyPlayer.stop();
  }
}