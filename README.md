# Melody Generator

Aplicación web para generar melodías aleatorias a partir de escalas musicales.

El proyecto está compuesto por un **backend desarrollado con Spring Boot** y un **frontend desarrollado con Angular**.

## Funcionalidades

* Selección de escalas mayores y menores.
* Generación aleatoria de melodías.
* Visualización de las melodías mediante un piano roll.
* Reproducción de melodías mediante Tone.js.
* Controles de reproducción.
* Control de tempo.
* Indicador de la nota que se está reproduciendo.
* Diseño responsive.

## Tecnologías

### Backend

* Java
* Spring Boot
* Maven

### Frontend

* Angular 21
* TypeScript
* Bootstrap
* Tone.js

## Estructura del proyecto

```
melody-generator/
├── backend/
│   └── README.md
├── frontend/
│   └── README.md
└── README.md
```

## Arquitectura

El backend sigue una estructura sencilla basada en responsabilidades:

```
Controller
    ↓
Service
    ↓
MelodyMaker
    ↓
MeasureGenerator
    ↓
MusicTheory / MelodyNoteSelector
```

El frontend se encarga de la interfaz, la comunicación con la API, la representación del piano roll y la reproducción de las melodías.

## API

El frontend se comunica con el backend mediante una API REST.

Endpoint principal:

```
POST /api/v1/melody/generate
```

Ejemplo de petición:

```
{
  "rootNote": "C",
  "mode": "MAJOR"
}
```

## Ejecución

Para ejecutar el proyecto es necesario iniciar el backend y el frontend por separado.

### Backend

Desde la carpeta `backend`:

```
./mvnw spring-boot:run
```

El backend expone la API utilizada por el frontend.

### Frontend

Desde la carpeta `frontend`:

```
npm install
ng serve
```

La aplicación estará disponible en:

```
http://localhost:4200
```

## Versión

Esta es la **V1** de Melody Generator.

La versión inicial se centra en el flujo principal de generación, visualización y reproducción de melodías.
