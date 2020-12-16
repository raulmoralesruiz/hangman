package com.hangman.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "player")
public class Player {
	
	/** id del usuario */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_user")
//	private Long idUser;
	private Long id;

	
	/** Nombre de usuario */
	private String username;
	
	/** IP del usuario */
	private String ip;
	
//	/** Fallos del usuario*/
//	private int mistakes;
//	
//	/** Letras usadas por el usuario */
//	private ArrayList<Character> letters = new ArrayList<Character>();

	
	
	
	public Player() {
		super();
	}

	public Player(String username) {
		super();
		this.username = username;
//		this.mistakes = 0;
	}

	
	


	public Long getIdUser() {
		return id;
	}

	public void setIdUser(Long idUser) {
		this.id = idUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

//	public int getMistakes() {
//		return mistakes;
//	}
//
//	public void setMistakes(int mistakes) {
//		this.mistakes = mistakes;
//	}
//
//	public ArrayList<Character> getLetters() {
//		return letters;
//	}
//
//	public void setLetters(ArrayList<Character> letters) {
//		this.letters = letters;
//	}



	@Override
	public String toString() {
		return "Player [username=" + username + ", ip=" + ip + "]";
	}
	
	
	

}
