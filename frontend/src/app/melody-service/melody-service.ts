// Importamos Injectable para convertir esta clase en un servicio de Angular
import { Injectable } from '@angular/core';

// Importamos HttpClient para hacer peticiones HTTP al backend
import { HttpClient } from '@angular/common/http';

// Importamos los modelos que representan la petición y la respuesta del backend
import { Scale, MelodyObject } from '../models/melody.model';

import { environment } from '../../environments/environment';


@Injectable({
  providedIn: 'root',
  // Hace que MelodyService esté disponible en toda la aplicación
})

/**
 * Gestiona la comunicación con la API de generación de melodías.
 *
 * Responsabilidades:
 * - Enviar solicitudes de generación de melodías.
 * - Recibir y tipar las respuestas de la API.
 *
 */

export class MelodyService {

  constructor(private http: HttpClient) {
    // Inyectamos HttpClient para poder utilizarlo dentro del servicio
  }

   generateMelody(scale: Scale) {
    return this.http.post<MelodyObject>(//lo que recibimos
      `${environment.apiUrl}/v1/melody/generate`,
      scale//lo que enviamos
    );
  }
}