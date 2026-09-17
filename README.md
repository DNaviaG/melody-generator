# Melody Generator

API REST desarrollada con **Java 21 y Spring Boot** para la generación algorítmica de melodías a partir de una escala musical.

El proyecto forma parte de una aplicación más amplia que, en futuras fases, contará con un frontend desarrollado con **Angular**, gestión de usuarios y persistencia de melodías.

## Descripción

Melody Generator genera melodías de forma algorítmica utilizando conceptos básicos de teoría musical, como:

* Escalas mayores y menores.
* Grados de la escala.
* Progresiones de acordes.
* Notas pertenecientes a los acordes.
* Movimiento melódico.
* Ritmo y duración de las notas.
* Estructuración de las melodías en compases.

Actualmente, el proyecto se centra en el desarrollo del **backend y de la lógica de generación musical**.

## Funcionalidades actuales

* Generación de melodías a partir de una escala musical.
* Soporte para escalas mayores y menores.
* Generación de progresiones de acordes.
* Generación de notas a partir de las escalas y acordes.
* Generación de melodías estructuradas en compases.
* API REST para solicitar la generación de melodías.
* Validación de las peticiones recibidas.
* Pruebas de los endpoints mediante Swagger UI.

## Tecnologías

* **Java 21**
* **Spring Boot**
* **Spring Web MVC**
* **Spring Validation**
* **Maven**
* **Lombok**
* **Springdoc OpenAPI / Swagger UI**

## Estructura del proyecto

El backend está organizado en diferentes paquetes según la responsabilidad de cada componente:

```text
src/
└── main/
    └── java/
        └── com.DanielNavia.melody_generator/
            ├── controller/
            ├── dto/
            ├── model/
            └── Maker/
```

### Principales componentes

* **Controller**: recibe y gestiona las peticiones HTTP.
* **DTO**: define los objetos utilizados para las peticiones y respuestas de la API.
* **Model**: contiene las estructuras que representan los elementos musicales.
* **Maker**: contiene la lógica encargada de generar las melodías.

## API

### Generar una melodía

```http
POST /api/v1/melody/generate
```

Recibe una escala musical y genera una melodía basada en ella.

#### Request

```json
{
  "scale": "C_MAJOR"
}
```

#### Response

La respuesta contiene la melodía generada, incluyendo la escala utilizada y sus diferentes compases y notas.

Ejemplo simplificado:

```json
{
  "melody": {
    "scale": "C_SHARP_MINOR",
    "measures": [
      {
        "notes": [
          {
            "note": "G_SHARP",
            "octave": 4,
            "duration": "EIGHTH"
          },
          {
            "note": "G_SHARP",
            "octave": 4,
            "duration": "EIGHTH"
          },
          {
            "note": "F_SHARP",
            "octave": 4,
            "duration": "QUARTER"
          },
          {
            "note": "E",
            "octave": 4,
            "duration": "QUARTER"
          },
          {
            "note": "D_SHARP",
            "octave": 4,
            "duration": "QUARTER"
          }
        ]
      },
      {
        "notes": [
          {
            "note": "A",
            "octave": 4,
            "duration": "QUARTER"
          },
          {
            "note": "B",
            "octave": 4,
            "duration": "QUARTER"
          },
          {
            "note": "E",
            "octave": 4,
            "duration": "HALF"
          }
        ]
      },
      {
        "notes": [
          {
            "note": "A",
            "octave": 4,
            "duration": "EIGHTH"
          },
          {
            "note": "C_SHARP",
            "octave": 4,
            "duration": "EIGHTH"
          },
          {
            "note": "D_SHARP",
            "octave": 4,
            "duration": "QUARTER"
          },
          {
            "note": "A",
            "octave": 4,
            "duration": "QUARTER"
          },
          {
            "note": "G_SHARP",
            "octave": 4,
            "duration": "QUARTER"
          }
        ]
      },
      {
        "notes": [
          {
            "note": "B",
            "octave": 4,
            "duration": "QUARTER"
          },
          {
            "note": "A",
            "octave": 4,
            "duration": "QUARTER"
          },
          {
            "note": "C_SHARP",
            "octave": 4,
            "duration": "HALF"
          }
        ]
      }
    ]
  }
}
```

## Pruebas de la API

El proyecto utiliza **Swagger UI** para probar los endpoints durante el desarrollo.

Desde Swagger UI se pueden enviar peticiones al backend y comprobar las respuestas directamente.

## Ejecución

### Requisitos

* Java 21
* Maven

### Ejecutar el proyecto

El proyecto puede ejecutarse desde IntelliJ IDEA o mediante Maven.

Una vez iniciada la aplicación, la API queda disponible para realizar peticiones al endpoint de generación de melodías.

## Próximas funcionalidades

El proyecto está planteado para evolucionar desde el generador actual hacia una aplicación completa de creación y gestión de melodías.

Entre las funcionalidades previstas se encuentran:

* Desarrollo del frontend con **Angular**.
* Registro y gestión de usuarios.
* Autenticación de usuarios.
* Gestión de peticiones de generación de melodías.
* Guardado de melodías.
* Carga de melodías guardadas.
* Edición de melodías.
* Persistencia mediante una base de datos.
* Descarga de melodías en formato MIDI.

Estas funcionalidades se incorporarán progresivamente a medida que avance el desarrollo del proyecto.
