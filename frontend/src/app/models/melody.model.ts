export interface Note {
  // Representa una nota de la melodía
  note: string;
  octave: number;
  duration: string;
}

export interface Measure {
  // Representa un compás, que contiene varias notas
  notes: Note[];
}

export interface MelodyObject {
  scale: Scale;
  measures: Measure[];
}

export interface Scale {
  rootNote: string;
  mode: string;
}