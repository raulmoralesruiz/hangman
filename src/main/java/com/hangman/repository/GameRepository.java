package com.hangman.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hangman.entity.Game;
import com.hangman.entity.Player;

@Repository(value = "gameRepository")
public interface GameRepository extends CrudRepository<Game, Long> {

	/**
	 * Método que busca una partida por id
	 * 
	 * @param id
	 * @return Game
	 */
	public Game findGameById(Long idGame);
	
	
	/**
	 * Método que busca una partida por jugador
	 * 
	 * @param player
	 * @return Game
	 */
	public Game findGameByPlayer(Player player);
		
}
