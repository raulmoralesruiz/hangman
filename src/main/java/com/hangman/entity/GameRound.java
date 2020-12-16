package com.hangman.entity;

import java.util.LinkedHashSet;

public class GameRound {

//	/** id de la ronda */
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "id_game")
////	private Long idGame;
//	private Long id;

	
	/** Palabra en proceso de adivinar, por defecto vacía */
//	@OneToOne(targetEntity = Word.class, cascade = CascadeType.ALL, orphanRemoval = true)
	private Word wordInProcessToGuess;
		
	/** Número de vidas disponibles para el jugador */
	private int lives;
	
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

	
	
	
	public GameRound() {
		super();
	}
	
//	public GameRound(Word wordInProcessToGuess, int lives, int mistakes, int hits, int attempts,
//			LinkedHashSet<Character> letters, String roundMessage) {
//		super();
//		this.wordInProcessToGuess = wordInProcessToGuess;
//		this.lives = lives;
//		this.mistakes = mistakes;
//		this.hits = hits;
//		this.attempts = attempts;
//		this.letters = letters;
//		this.roundMessage = roundMessage;
//	}
	
	public GameRound(Word wordInProcessToGuess, int lives, int mistakes, int hits, int attempts,
			LinkedHashSet<Character> letters, LinkedHashSet<String> words, String roundMessage) {
		super();
		this.wordInProcessToGuess = wordInProcessToGuess;
		this.lives = lives;
		this.mistakes = mistakes;
		this.hits = hits;
		this.attempts = attempts;
		this.letters = letters;
		this.words = words;
		this.roundMessage = roundMessage;
	}

	
	


	

	/**
	 * @return the wordInProcessToGuess
	 */
	public Word getWordInProcessToGuess() {
		return wordInProcessToGuess;
	}

	/**
	 * @param wordInProcessToGuess the wordInProcessToGuess to set
	 */
	public void setWordInProcessToGuess(Word wordInProcessToGuess) {
		this.wordInProcessToGuess = wordInProcessToGuess;
	}

	/**
	 * @return the lives
	 */
	public int getLives() {
		return lives;
	}

	/**
	 * @param lives the lives to set
	 */
	public void setLives(int lives) {
		this.lives = lives;
	}

	/**
	 * @return the mistakes
	 */
	public int getMistakes() {
		return mistakes;
	}

	/**
	 * @param mistakes the mistakes to set
	 */
	public void setMistakes(int mistakes) {
		this.mistakes = mistakes;
	}

	/**
	 * @return the hits
	 */
	public int getHits() {
		return hits;
	}

	/**
	 * @param hits the hits to set
	 */
	public void setHits(int hits) {
		this.hits = hits;
	}

	/**
	 * @return the attempts
	 */
	public int getAttempts() {
		return attempts;
	}

	/**
	 * @param attempts the attempts to set
	 */
	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

//	/**
//	 * @return the letters
//	 */
//	public ArrayList<Character> getLetters() {
//		return letters;
//	}
//
//	/**
//	 * @param letters the letters to set
//	 */
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

	/**
	 * @return the roundMessage
	 */
	public String getRoundMessage() {
		return roundMessage;
	}

	/**
	 * @param roundMessage the roundMessage to set
	 */
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
		return "GameRound [wordInProcessToGuess=" + wordInProcessToGuess + ", lives=" + lives + ", mistakes=" + mistakes
				+ ", hits=" + hits + ", attempts=" + attempts + ", letters=" + letters + ", roundMessage="
				+ roundMessage + "]";
	}
	
}
