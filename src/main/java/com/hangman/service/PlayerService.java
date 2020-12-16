package com.hangman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.hangman.entity.Player;
import com.hangman.repository.PlayerRepository;

@Service
public class PlayerService {

	@Autowired
	private PlayerRepository playerRepo;
	
	
	// ----- GET -----

	/**
	 * Método que obtiene un jugador, buscando por id
	 * 
	 * @param idUser
	 * @return Player
	 */
	public Player getPlayerById(Long idUser) {
		return playerRepo.findPlayerById(idUser);
	}
	
	/**
	 * Método que obtiene un jugador, buscando por nombre de usuario
	 * 
	 * @param username
	 * @return Player
	 */
	public Player getPlayerByUsername(String username) {
		return playerRepo.findPlayerByUsername(username);
	}
	
	
	// ----- POST -----
	
	public Player registerPlayer(@RequestBody Player player) {

		// Se comprueba si el jugador ya existe o es un jugador nuevo.
		Player playerSearched = playerRepo.findPlayerByUsername(player.getUsername());
		
		// Si el jugador buscado no existe, se puede crear el nuevo jugador (player)
		if (playerSearched == null) {
			
			// Se guarda el nuevo jugador en BBDD
			playerRepo.save(player);
			
			// Se guarda el jugador para devolverlo al final
			playerSearched = player;
			
		// Si el jugador buscado existe, se establece a null
		} else {
			// Se guarda el jugador como null, para mostrar error en controller
			playerSearched = null;
		}
		
		// Se devuelve el jugador
		return playerSearched;
	}

	
}
