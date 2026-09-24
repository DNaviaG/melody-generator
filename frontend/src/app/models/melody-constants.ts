// Traduce el nombre de nota del backend ("D_SHARP") al símbolo musical que
// entiende Tone.js ("D#"). Usado por MelodyPlayerService.
export const NOTE_SYMBOLS: Record<string, string> = {
  C: 'C', C_SHARP: 'C#', D: 'D', D_SHARP: 'D#', E: 'E', F: 'F',
  F_SHARP: 'F#', G: 'G', G_SHARP: 'G#', A: 'A', A_SHARP: 'A#', B: 'B'
};

// Traduce la duración del backend ("QUARTER") a notación de Tone.js ("4n").
// Usado por MelodyPlayerService.
export const DURATION_TO_TONE: Record<string, string> = {
  WHOLE: '1n', HALF: '2n', QUARTER: '4n', EIGHTH: '8n'
};

// Cuántas "negras" (beats) dura cada tipo de figura. Usado por PianoRollService
// para calcular proporciones, y también sirve de referencia para lo de arriba:
// si el backend añade una duración nueva, se añade AQUÍ y en las dos tablas
// de arriba a la vez, en el mismo archivo.
export const DURATION_BEATS: Record<string, number> = {
  WHOLE: 4, HALF: 2, QUARTER: 1, EIGHTH: 0.5
};

// Las 12 notas cromáticas, de aguda a grave. Usado por PianoRollService
// para saber en qué fila va cada nota.
export const PIANO_ROLL_NOTES = [
  'B', 'A_SHARP', 'A', 'G_SHARP', 'G', 'F_SHARP',
  'F', 'E', 'D_SHARP', 'D', 'C_SHARP', 'C'
];