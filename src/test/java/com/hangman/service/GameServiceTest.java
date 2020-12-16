package com.hangman.service;


import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.hangman.entity.Game;
import com.hangman.repository.GameRepository;


//@SpringBootTest
public class GameServiceTest {

	// Subject Under Test
	private GameService sut;
	
	private GameRepository mockedGameRepo;
	Game mockedGame;

	
	@BeforeEach
	private void init() {
		mockedGameRepo = mock(GameRepository.class);
		mockedGame = mock(Game.class);
		
		sut = new GameService(mockedGameRepo);
	}
	

	// ------------------- TEST -------------------
	
	/**
	 * Método de prueba que busca una partida por id.
	 */
	@Test
	public void getOneGame() {
		
		Game game = new Game();

		Mockito.when(mockedGameRepo.findGameById(Mockito.anyLong())).thenReturn(mockedGame);
		game = mockedGameRepo.findGameById(Mockito.anyLong());

		assert (game == mockedGame);
	}
	
	
	
	/**
	 * Método de prueba que comienza una partida.
	 */
//	@Test
//	public void startGame() {
//		
//		Game game = new Game();
//
//		Mockito.when(mockedGameRepo.findGameById(Mockito.anyLong())).thenReturn(mockedGame);
//		game = mockedGameRepo.findGameById(Mockito.anyLong());
//
//		assert (game == mockedGame);
//	}
	
	
//	public Game startGame(Player player) {
//
//		Game newGame = null;
//
//		if (player != null) {
//			// Se crea palabra aleatoria
//			Word wordToGuess = selectRandomWord();
//
//			// Se crea palabra con carácteres (*), de la misma longitud a la anterior
//			Word wordInProcessToGuess = initWordInProcessToGuess(wordToGuess);
//
//			// Se crea partida, incluyendo las palabras anteriores y el jugador pasado por
//			// parámetro.
//			newGame = new Game(wordToGuess, wordInProcessToGuess, player);
//
//			// Se establece el estado de partida: en progreso.
//			newGame.setStatus(GameStatus.IN_PROGRESS);
//
//			// Se guarda la partida en BBDD
//			gameRepo.save(newGame);
//		}
//
//		// Se devuelve la partida
//		return newGame;
//	}
	
	


	
}


///**
//* Test que comprueba el método "getCustomerVisuals" de CustomerService.
//* Se busca al cliente por nombre y apellido
//* En la comprobación final no se utiliza sut (servicio)
//*/
//@Test
//public void getCustomerVisualsByNameAndSurname() {		
//	List<Visual> auxVisuals = null;
//	
//	Mockito.when(mockedGameRepo.findCustomerByNameAndSurname(Mockito.any(), Mockito.any())).thenReturn(mockedGame);
//	
//	if (!mockedGame.getVisuals().isEmpty()) {
//		auxVisuals = mockedGame.getVisuals();
//	}
//
//	assert (auxVisuals == mockedGame.getVisuals() || auxVisuals == null);
//}


///**
//* Test que comprueba el método "getCustomerVisuals" de CustomerService.
//* Se busca al cliente por id
//* En la comprobación final se utiliza sut (servicio)
//*/
//@Test
//public void getCustomerVisualsById() {		
//	List<Visual> auxVisuals = null;
//	
//	Mockito.when(mockedGameRepo.findCustomerById(Mockito.anyLong())).thenReturn(mockedGame);
//	
//	if (!mockedGame.getVisuals().isEmpty()) {
//		auxVisuals = mockedGame.getVisuals();
//	}
//	
//	assert (auxVisuals == sut.getCustomerVisuals(mockedGame.getId()) || auxVisuals == null);
//}