# Melody Generator - Backend

API REST desarrollada con **Java 21 y Spring Boot** para la generación algorítmica de melodías a partir de una escala musical.

Este backend forma parte de la aplicación **Melody Generator**, junto con un frontend desarrollado con Angular.

## Descripción

Melody Generator genera melodías de forma algorítmica utilizando conceptos básicos de teoría musical, como:

* Escalas mayores y menores.
* Grados de la escala.
* Progresiones de acordes.
* Notas pertenecientes a los acordes.
* Movimiento melódico.
* Ritmo y duración de las notas.
* Estructuración de las melodías en compases.

La lógica de generación está separada de la capa HTTP para mantener una estructura sencilla y clara.

## Funcionalidades actuales

* Generación de melodías a partir de una escala musical.
* Soporte para escalas mayores y menores.
* Generación de progresiones de acordes.
* Generación de notas a partir de las escalas y acordes.
* Generación de melodías estructuradas en compases.
* API REST para solicitar la generación de melodías.
* Manejo de errores de la API.

## Tecnologías

* **Java 21**
* **Spring Boot**
* **Spring Web MVC**
* **Maven**
* **Lombok**
* **Springdoc OpenAPI / Swagger UI**

## Estructura del proyecto

El backend está organizado en diferentes paquetes según la responsabilidad de cada componente:

```
src/
└── main/
    ├── java/
    │   └── com.DanielNaviaG.melody_generator/
    │       ├── controller/
    │       ├── dto/
    │       ├── exception/
    │       ├── Maker/
    │       ├── model/
    │       ├── music/
    │       └── service/
    └── resources/
        └── application.properties
```

### Principales componentes

* **Controller**: recibe las peticiones HTTP y delega la generación al servicio.
* **Service**: coordina el caso de uso de generación de melodías.
* **Maker**: contiene la lógica principal encargada de generar la melodía.
* **Music**: contiene la lógica relacionada con teoría musical y generación de compases.
* **Model**: contiene las estructuras que representan los elementos musicales.
* **DTO**: contiene las estructuras específicas utilizadas por la API cuando son necesarias.
* **Exception**: contiene el manejo de errores de la aplicación.

## API

### Generar una melodía

```
POST /api/v1/melody/generate
```

Recibe una escala musical y genera una melodía basada en ella.

### Request

```
{
  "rootNote": "C",
  "mode": "MAJOR"
}
```

Los modos disponibles actualmente son:

* `MAJOR`
* `MINOR`

### Response

La respuesta contiene la escala utilizada y los diferentes compases y notas de la melodía generada.

Ejemplo simplificado:

```
{
  "scale": {
    "rootNote": "C",
    "mode": "MAJOR"
  },
  "measures": [
    {
      "notes": [
        {
          "note": "G",
          "octave": 4,
          "duration": "QUARTER"
        },
        {
          "note": "E",
          "octave": 4,
          "duration": "QUARTER"
        },
        {
          "note": "C",
          "octave": 4,
          "duration": "HALF"
        }
      ]
    }
  ]
}
```

## Swagger UI

La API puede probarse mediante **Swagger UI** cuando la aplicación está en ejecución.

Swagger permite consultar la documentación de los endpoints y realizar peticiones directamente contra la API.

## Ejecución

### Requisitos

* Java 21
* Maven

### Ejecutar el proyecto

Desde la carpeta `backend`:

```
./mvnw spring-boot:run
```

En Windows:

```
.\mvnw.cmd spring-boot:run
```

También puede ejecutarse directamente desde IntelliJ IDEA.

Una vez iniciada la aplicación, la API estará disponible para recibir peticiones de generación de melodías.

## Estado del proyecto

Esta versión corresponde a la **V1** del backend.

El proyecto está planteado para evolucionar progresivamente con nuevas funcionalidades, como persistencia de melodías, usuarios, autenticación y exportación a formatos como MIDI.
