package com.hangman.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hangman.entity.Game;
import com.hangman.entity.GameRound;
import com.hangman.entity.Player;
import com.hangman.service.GameService;
import com.hangman.service.PlayerService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/game")
public class GameController {

	@Autowired
	private GameService gameService;

	@Autowired
	private PlayerService playerService;
	
	
	
	/**
	 * Método que realiza intento para buscar la palabra oculta, indicando letra.
	 * 
	 * @param user
	 * @param idGame
	 * @param letter
	 * @return ResponseEntity<?>
	 * @throws Exception 
	 */
	@GetMapping("/attempt/{user}/{idGame}/{letter}")
	public ResponseEntity<?> makeUserLetterAttempt(@PathVariable String user, @PathVariable Long idGame
			, @PathVariable Character letter, HttpServletRequest request) {

		String ip = request.getRemoteAddr();
		
		ResponseEntity<?> response = null;		

		GameRound gameRound = null;
		try {
			gameRound = gameService.makeLetterAttempt(user, idGame, letter, ip);
		} catch (Exception e) {
			String error = e.getMessage();
			response = ResponseEntity.status(HttpStatus.CONFLICT).body(error);
		}
		
		if (gameRound != null) {
			response = ResponseEntity.status(HttpStatus.OK).body(gameRound);
		}

		return response;
	}
//	/**
//	 * Método que realiza intento para buscar la palabra oculta, indicando letra o palabra.
//	 * 
//	 * @param user
//	 * @param idGame
//	 * @param letter
//	 * @return ResponseEntity<?>
//	 * @throws Exception 
//	 */
//	@GetMapping("/attempt/{user}/{idGame}/{input}")
//	public ResponseEntity<?> makeUserAttempt(@PathVariable String user, @PathVariable Long idGame
//			, @PathVariable String input, HttpServletRequest request) {
//
//		String ip = request.getRemoteAddr();
//		
//		ResponseEntity<?> response = null;		
//
//		GameRound gameRound = null;
//		try {
//			gameRound = gameService.makeAttempt(user, idGame, input, ip);
//		} catch (Exception e) {
//			String error = e.getMessage();
//			response = ResponseEntity.status(HttpStatus.CONFLICT).body(error);
//		}
//		
//		if (gameRound != null) {
//			response = ResponseEntity.status(HttpStatus.OK).body(gameRound);
//		}
//
//		return response;
//	}

	
	
	/**
	 * Método que realiza intento para buscar la palabra oculta, indicando palabra.
	 * 
	 * @param user
	 * @param idGame
	 * @param letter
	 * @return ResponseEntity<?>
	 * @throws Exception 
	 */
	@GetMapping("/attempt/{user}/{idGame}/word/{word}")
	public ResponseEntity<?> makeUserWordAttempt(@PathVariable String user, @PathVariable Long idGame
			, @PathVariable String word, HttpServletRequest request) {

		String ip = request.getRemoteAddr();
		
		ResponseEntity<?> response = null;		

		GameRound gameRound = null;
		try {
			gameRound = gameService.makeWordAttempt(user, idGame, word, ip);
		} catch (Exception e) {
			String error = e.getMessage();
			response = ResponseEntity.status(HttpStatus.CONFLICT).body(error);
		}
		
		if (gameRound != null) {
			response = ResponseEntity.status(HttpStatus.OK).body(gameRound);
		}

		return response;
	}


	
	/**
	 * Método que crea una partida para el jugador indicado
	 * 
	 * @param user
	 * @return ResponseEntity<?>
	 * @throws Exception 
	 */
	@GetMapping("/{user}")
	public ResponseEntity<?> createGame(@PathVariable String user) {
		
		Player player = playerService.getPlayerByUsername(user);
		
		Game gameResult = gameService.startGame(player);
		ResponseEntity<?> response = null;
		
		if (gameResult == null) {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR. No se ha podido crear la partida para el jugador " + user);
		} else {	
			response = ResponseEntity.status(HttpStatus.OK).body("Bienvenido " + user + ".\n" + 
					"Puedes empezar la partida aquí: http://localhost:8080/attempt/" + user + "/" + gameResult.getIdGame() 
					+ "\n\nRecuerda indicar la letra al final de la URL."
					+ "\nPor ejemplo: /a");
		}
		
		return response;
	}
	
	
	/**
	 * Método que ofrece información de la partida (dirigido al jugador)
	 * 
	 * @param user
	 * @param idGame
	 * @param letter
	 * @return ResponseEntity<?>
	 * @throws Exception 
	 */
	@GetMapping("/{user}/{idGame}")
	public ResponseEntity<?> getGameInfoForPlayer(@PathVariable String user, @PathVariable Long idGame
			, HttpServletRequest request) {

		String ip = request.getRemoteAddr();
		
		ResponseEntity<?> response = null;		

		GameRound gameRound = gameService.getGameInfoForPlayer(idGame, user, ip);
		
		if (gameRound != null) {
			response = ResponseEntity.status(HttpStatus.OK).body(gameRound);
		} else {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR. No se han podido obtener los datos de la partida.");
		}

		return response;
	}

	
	
	
	/**
	 * Método que ofrece información de la partida (dirigido al administrador)
	 * 
	 * @param idGame
	 * @return ResponseEntity<?>
	 */
	@GetMapping("/admin/{idGame}")
	public ResponseEntity<?> getGameInfoForAdmin(@PathVariable Long idGame, HttpServletRequest request) {
		
		String ip = request.getRemoteAddr();
		
		Game gameResult = gameService.getGameInfoForAdmin(idGame, ip);
		ResponseEntity<?> response = null;
		
		if (gameResult == null) {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR. No es posible mostrar la información de la partida.");
		} else {	
			response = ResponseEntity.status(HttpStatus.OK).body(gameResult);
		}
		
		return response;
	}
	
	

}


///**
//* Método que realiza intento para buscar la palabra oculta.
//* DE MOMENTO SOLO SE PUEDE JUGAR UNA PARTIDA AL MISMO TIEMPO.
//* PODRÍA SOLUCIONARSE GUARDANDO EL ID_GAME, REALIZANDO EL INTENTO INDICANDO ESTE ID.
//* 
//* @param user
//* @param letter
//* @return ResponseEntity<?>
//* @throws Exception 
//*/
//@GetMapping("/attemp/{user}/{idGame}/{letter}")
//public ResponseEntity<?> makeUserAttempt(@PathVariable String user, @PathVariable Long idGame, @PathVariable Character letter) throws Exception{
//	
//	Game game = gameService.getGame(idGame);
//			
//	GameRound gameRound = gameService.makeAttempt(user, idGame, letter);
////	GameStatus attemptResult = gameService.makeAttempt(user, idGame, letter);
//	ResponseEntity<?> response = null;
//			
////	String commonMessage = "Letras usadas: " + game.getLetters() + "\n"
////			+ "Intentos: " + game.getAttempts() + "\n"
////			+ "Aciertos: " + game.getHits() + "\n"
////			+ "Fallos: " + game.getMistakes() + "\n"
////			+ "Vidas disponibles: " + game.getLives();
////
////	if (attemptResult.equals(GameStatus.IN_PROGRESS)) {
////		response = ResponseEntity.status(HttpStatus.OK).body(
////				"Sigues vivo...\n\n" + "Palabra oculta: " + game.getWordInProcessToGuess() + "\n" + commonMessage);
////	}
////
////	if (attemptResult.equals(GameStatus.MISS)) {
////		response = ResponseEntity.status(HttpStatus.OK).body("Mala suerte, esa letra no existe... \n\n"
////				+ "Palabra oculta: " + game.getWordInProcessToGuess() + "\n" + commonMessage);
////	}
////
////	if (attemptResult.equals(GameStatus.SUCCESS)) {
////		response = ResponseEntity.status(HttpStatus.OK).body("Bien, has acertado la letra!\n\n" + "Palabra oculta: "
////				+ game.getWordInProcessToGuess() + "\n" + commonMessage);
////	}
////
////	if (attemptResult.equals(GameStatus.GAME_OVER)) {
////		response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
////				"Has perdido...\n\n" + "La palabra oculta era: " + game.getWordToGuess() + "\n" + commonMessage);
////	}
////
////	if (attemptResult.equals(GameStatus.VICTORY)) {
////		response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
////				"HAS GANADO!!! \n\n" + "La palabra oculta era: " + game.getWordToGuess() + "\n" + commonMessage);
////	}
//	
//	
//	response = ResponseEntity.status(HttpStatus.OK).body(gameRound);
////	response = ResponseEntity.status(HttpStatus.OK).body(game);
//
//
//	return response;
//}
