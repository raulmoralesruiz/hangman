package com.hangman.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hangman.entity.Player;

@Repository(value = "playerRepository")
public interface PlayerRepository extends CrudRepository<Player, Long> {
	
	/**
	 * Método que busca un jugador por id
	 * 
	 * @param id
	 * @return Player
	 */
	public Player findPlayerById(Long idUser);
	
	
	/**
	 * Método que busca un jugador por nombre de usuario
	 * 
	 * @param username
	 * @return Player
	 */
	public Player findPlayerByUsername(String username);
	

}
