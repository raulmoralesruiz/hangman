package com.hangman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hangman.entity.Player;
import com.hangman.service.PlayerService;

@SpringBootApplication
public class HangmanApplication implements CommandLineRunner {

	@Autowired
	private PlayerService playerService;

	
	public static void main(String[] args) {
		SpringApplication.run(HangmanApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		// Creación de jugador
		Player admin = new Player("admin");
		
		// Establecer dirección IP
		admin.setIp("0:0:0:0:0:0:0:1");
		
		// Registrar usuario admin en BBDD
		playerService.registerPlayer(admin);
		
	}



	
	/*
		Descripción:
		En este proyecto debes realizar el juego del ahorcado haciendo uso del patrón MVC,  tests (unitarios y para endpoints) y filtrado de peticiones web.
		En este juego un jugador debe acertar una palabra desconocida preguntando en cada paso por una letra. 
		Si la letra no se ha consultado antes y está contenida en la palabra, se muestran todas sus ocurrencias. 
		Si la letra no está contenida o se solicitó anteriormente, se informa del error cometido.
		Tras un número determinado de errores, el usuario pierde la partida y se muestra la figura del ahorcado completa.
		Esta se va mostrando a especie de contador a medida que el número de fallos se incrementa:
		________
		|/      |
		|       O
		|     / | \
		|      / \
		
		Funcionalidad mínima:
		- La palabra a adivinar puede ser proporcionada desde la misma ip que levanta el servidor, pero no desde otra
		- El jugador sólo puede preguntar por una letra en cada petición y sólo puede acceder desde una ip diferente a la que levanta el servidor del juego
		- Se debe incluir un usuario o registro asociado a cada ip para que no colisionen peticiones de diferentes jugadores
		- Las respuestas deben darse en formato json
		- Incluir tests unitarios y a los endpoints
		
		Opcional (nota):
		- almacenar un histórico de jugadas y ránking de jugadores
		- incluir registro de usuario/pass en las peticiones
		- seleccionar palabra aleatoria de un banco de datos (descargado o consumido mediante api rest).
		- Incluir tests end-to-end
	 */
}


// Creación de jugador
//Player p1 = new Player("raulmr");

// Creación de partida
//Game game = gameService.startGame(p1);
