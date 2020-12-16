package com.hangman.entity;

import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity(name = "game")
public class Game {
	
	private final int MAX_LIVES = 5;

	/** id de la partida */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_game")
//	private Long idGame;
	private Long id;


	/** Palabra para adivinar */
	@OneToOne(targetEntity = Word.class, cascade = CascadeType.ALL, orphanRemoval = true)
	private Word wordToGuess;
	
	/** Palabra en proceso de adivinar, por defecto vacía */
	@OneToOne(targetEntity = Word.class, cascade = CascadeType.ALL, orphanRemoval = true)
	private Word wordInProcessToGuess;
	
	/** Jugador que debe adivinar la palabra */
	@OneToOne(targetEntity = Player.class, cascade = CascadeType.ALL, orphanRemoval = true)
	private Player player;
	
	/** Número de vidas disponibles para el jugador */
	private int lives;
	
	/** Estado de la partida*/
	private GameStatus status;
	
	/** Fallos del usuario*/
	private int mistakes;
	
	/** Aciertos del usuario*/
	private int hits;
	
	/** Intentos del usuario*/
	private int attempts;
	
	/** Letras usadas por el usuario */
	private LinkedHashSet<Character> letters = new LinkedHashSet<Character>();

	/** Palabras usadas por el usuario */
	private LinkedHashSet<String> words = new LinkedHashSet<String>();
	
	/** Mensaje informativo de cada ronda */
	private String roundMessage;

	
	
	
	public Game() {
		super();
	}

	public Game(Word wordToGuess, Word wordInProcessToGuess, Player player) {
		super();
		this.wordToGuess = wordToGuess;
		this.wordInProcessToGuess = wordInProcessToGuess;
		this.player = player;
		this.lives = MAX_LIVES;
		this.status = GameStatus.NOT_STARTED;
		this.mistakes = 0;
		this.hits = 0;
		this.attempts = 0;
		this.roundMessage = "Bienvenido al ahorcado";
	}
	
	

	public Long getIdGame() {
		return id;
	}

	public void setIdGame(Long idGame) {
		this.id = idGame;
	}
	
	public Word getWordToGuess() {
		return wordToGuess;
	}

	public void setWordToGuess(Word wordToGuess) {
		this.wordToGuess = wordToGuess;
	}

	public Word getWordInProcessToGuess() {
		return wordInProcessToGuess;
	}

	public void setWordInProcessToGuess(Word wordInProcessToGuess) {
		this.wordInProcessToGuess = wordInProcessToGuess;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int attempts) {
		this.lives = attempts;
	}

	public int getMAX_LIVES() {
		return MAX_LIVES;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public int getMistakes() {
		return mistakes;
	}

	public void setMistakes(int mistakes) {
		this.mistakes = mistakes;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

//	public ArrayList<Character> getLetters() {
//		return letters;
//	}
//
//	public void setLetters(ArrayList<Character> letters) {
//		this.letters = letters;
//	}
	
	/**
	 * @return the letters
	 */
	public LinkedHashSet<Character> getLetters() {
		return letters;
	}

	/**
	 * @param letters the letters to set
	 */
	public void setLetters(LinkedHashSet<Character> letters) {
		this.letters = letters;
	}
	
	public String getRoundMessage() {
		return roundMessage;
	}

	public void setRoundMessage(String roundMessage) {
		this.roundMessage = roundMessage;
	}
	
	public LinkedHashSet<String> getWords() {
		return words;
	}

	public void setWords(LinkedHashSet<String> words) {
		this.words = words;
	}


	
	
	@Override
	public String toString() {
		return "Game [MAX_LIVES=" + MAX_LIVES + ", id=" + id + ", wordToGuess=" + wordToGuess
				+ ", wordInProcessToGuess=" + wordInProcessToGuess + ", player=" + player + ", lives=" + lives
				+ ", status=" + status + ", mistakes=" + mistakes + ", hits=" + hits + ", attempts=" + attempts
				+ ", letters=" + letters + "]";
	}
	
	
	public void addLetterToList(Character letter) {
		this.letters.add(letter);
	}
	
	public void addWordToList(String word) {
		this.words.add(word);
	}


	
	
}


