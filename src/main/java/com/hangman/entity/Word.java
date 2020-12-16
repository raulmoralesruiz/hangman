package com.hangman.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "word")
public class Word {
	
	/** id de la palabra */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_word")
	private Long idWord;

	/** Valor de la palabra */
	private String word;


	
	
	public Word() {
		super();
	}

	public Word(String word) {
		super();
		this.word = word;
	}



	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}



	@Override
	public String toString() {
		return this.word;
	}
	
	
	
	
	
	
}
