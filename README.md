# Hangman (Ahorcado)

#### Raúl Morales Ruiz

---

## Introducción

El objetivo del proyecto es jugar al clásico juego del ahorcado.

En el apartado de funciones disponibles se indican las distintas posibilidades del proyecto.

## Requisitos

- Tener instalado [Spring Tool Suite](https://spring.io/tools), para la parte backend.
- Utilizar [Postman](https://www.postman.com/downloads/) para realizar las peticiones HTTP.

## Preparación del entorno

Para utilizar la aplicación web, primero debemos descargar el entorno backend.

- Descargamos el [proyecto](https://github.com/raulmoralesruiz/hangman) desde el siguiente enlace:
  https://github.com/raulmoralesruiz/hangman

### Backend

- Importamos el proyecto en Spring.

        File -> Import -> Maven -> Existing Maveng Projects

        Una vez agregado el proyecto, Spring descargará las dependencias necesarias (podemos comprobarlo en la esquina inferior derecha.)

- Iniciamos el servidor desde Spring

        Tras importar el proyecto, iniciamos el servidor desde el apartado "Boot Dashboard" (normalmente ubicado en la parte inferior izquieda.)

        Desplegamos el botón "local" -> click botón derecho sobre "hangman" -> click sobre start

## Funciones disponibles

### Jugador

#### Registrar jugador

- Descripción

        Esta petición crea un jugador en la base de datos, para poder jugar partidas del ahorcado.

- Petición:

        POST
        localhost:8080/player

- Ejemplo de petición:

        localhost:8080/player

- Ejemplo del cuerpo (body) con los datos del usuario que vamos a crear en la petición.

        {
        "username": "raul"
        }

- Ejemplo de respuesta:

        {
        "username": "raul",
        "ip": "0:0:0:0:0:0:0:1",
        "idUser": 2
        }

### Partida

#### Crear partida

- Descripción

        Esta petición crea una partida para el jugador que indiquemos.

- Petición:

        GET
        localhost:8080/game/<username_jugador>

- Ejemplo de petición:

        localhost:8080/game/raul

- Ejemplo de respuesta:

        Bienvenido raul.
        Puedes empezar la partida aquí: http://localhost:8080/attempt/raul/1

        Recuerda indicar la letra al final de la URL.
        Por ejemplo: /a

#### Obtener información de la partida (jugador)

- Descripción

        Esta petición obtiene los datos de una partida, ya esté en proceso o se haya terminado.
        Le sirve al jugador para revisar el estado de la partida.

- Petición:

        GET
        localhost:8080/game/<username_jugador>/<id_partida>

- Ejemplo de petición:

        localhost:8080/game/raul/1

- Ejemplo de respuesta:

        {
        "wordInProcessToGuess": {
                "word": "****a*"
        },
        "lives": 5,
        "mistakes": 0,
        "hits": 1,
        "attempts": 1,
        "letters": [
                "a"
        ],
        "words": [],
        "roundMessage": "Bien, has acertado la letra!"
        }

#### Realizar intento indicando letra

- Descripción

        Esta petición realiza un intento en la partida del usuario, indicando una letra al final de la URL.
        El objetivo es descubrir si la letra indicada existe en la palabra.

- Petición:

        GET
        localhost:8080/game/attempt/<username_jugador>/<id_partida>/<letra>

- Ejemplo de petición:

        localhost:8080/game/attempt/raul/1/a

- Ejemplo de respuesta:

        {
        "wordInProcessToGuess": {
                "word": "****a*"
        },
        "lives": 4,
        "mistakes": 1,
        "hits": 1,
        "attempts": 2,
        "letters": [
                "a",
                "f"
        ],
        "words": [],
        "roundMessage": "Mala suerte, no has acertado..."
        }

#### Realizar intento indicando palabra

- Descripción

        Esta petición realiza un intento en la partida del usuario, indicando una palabra al final de la URL.
        El objetivo es descubrir si la palabra indicada coincide con la palabra oculta.

- Petición:

        GET
        localhost:8080/game/attempt/<username_jugador>/<id_partida>/word/<palabra>

- Ejemplo de petición:

        localhost:8080/game/attempt/raul/1/word/juegas

- Ejemplo de respuesta:

        {
        "wordInProcessToGuess": {
                "word": "****a*"
        },
        "lives": 2,
        "mistakes": 3,
        "hits": 1,
        "attempts": 4,
        "letters": [
                "a",
                "f"
        ],
        "words": [
                "culata",
                "juegas"
        ],
        "roundMessage": "Mala suerte, no has acertado..."
        }

#### Obtener información de la partida (administrador)

- Descripción

        Esta petición obtiene los datos de una partida, ya esté en proceso o se haya terminado.
        Le sirve al administrador para revisar el estado de la partida.

        Se incluyen todos los datos de la partida, hasta la palabra oculta.
        Sólo funcionará desde el equipo del administrador, evitando de esta forma que el jugador pueda hacer trampas.

- Petición:

        GET
        localhost:8080/game/admin/<id_partida>

- Ejemplo de petición:

        localhost:8080/game/admin/1

- Ejemplo de respuesta:

        {
        "wordToGuess": {
                "word": "verdad"
        },
        "wordInProcessToGuess": {
                "word": "****a*"
        },
        "player": {
                "username": "raul",
                "ip": "0:0:0:0:0:0:0:1",
                "idUser": 2
        },
        "lives": 2,
        "status": "MISS",
        "mistakes": 3,
        "hits": 1,
        "attempts": 4,
        "letters": [
                "a",
                "f"
        ],
        "words": [
                "culata",
                "juegas"
        ],
        "roundMessage": "Mala suerte, no has acertado...",
        "idGame": 1,
        "max_LIVES": 5
        }
