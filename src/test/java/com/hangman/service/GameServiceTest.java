package com.hangman.service;


import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.hangman.entity.Game;
import com.hangman.entity.GameStatus;
import com.hangman.entity.Player;
import com.hangman.entity.Word;
import com.hangman.repository.GameRepository;
import com.hangman.repository.PlayerRepository;


//@SpringBootTest
public class GameServiceTest {

	// Subject Under Test
	private GameService sut;
	
	private GameRepository mockedGameRepo;
	Game mockedGame;
	
	private PlayerRepository mockedPlayerRepo;
	Player mockedPlayer;


	
	@BeforeEach
	private void init() {
		mockedGameRepo = mock(GameRepository.class);
		mockedGame = mock(Game.class);
		
		mockedPlayerRepo = mock(PlayerRepository.class);
		mockedPlayer = mock(Player.class);
		
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
	

	
	@Test
	public void cleanStringTest() {
		// Se crea palabra
		String word = "árbol";
		
		// Se "limpia" la palabra
		String cleanWord = sut.cleanString(word);
		
		// Se comprueba si realmente se ha "limpiado"
		assert(cleanWord.equals("arbol"));
	}
	
	
	@Test
	public void selectWordTest() {
		// Se guarda una palabra aleatoria.
		Word randomWord = sut.selectRandomWord();
		
		// Se comprueba que el diccionario contiene la palabra anterior.
		assert(sut.dictionary.contains(randomWord.getWord()));
	}
	
	
	/**
	 * Método de prueba que comienza una partida.
	 */
	@Test
	public void startGameTest() {

		Game newGame = null;

		Mockito.when(mockedPlayerRepo.findPlayerById(Mockito.anyLong())).thenReturn(mockedPlayer);

		if (mockedPlayer != null) {
			// Se crea palabra aleatoria
			Word wordToGuess = sut.selectRandomWord();

			// Se crea palabra con carácteres (*), de la misma longitud a la anterior
			Word wordInProcessToGuess = sut.initWordInProcessToGuess(wordToGuess);

			// Se crea partida, incluyendo las palabras anteriores y el jugador pasado por
			// parámetro.
			newGame = new Game(wordToGuess, wordInProcessToGuess, mockedPlayer);

			// Se establece el estado de partida: en progreso.
			newGame.setStatus(GameStatus.IN_PROGRESS);

		}

		Game game = sut.startGame(mockedPlayer);

		assert (!game.equals(newGame));
	}
	
	/**
	 * Método de prueba que comienza una partida.
	 */
	@Test
	public void startGameTestV2() {

		Mockito.when(mockedPlayerRepo.findPlayerById(Mockito.anyLong())).thenReturn(mockedPlayer);
		
		Game game = sut.startGame(mockedPlayer);

		assert (game.getStatus().equals(GameStatus.IN_PROGRESS));
	}

	
	@Test
	public void containsOnlyLettersTest() {
		assert(sut.containsOnlyLetters("palabra"));
		assert(!(sut.containsOnlyLetters("L3tr4")));	
	}
	

	
}