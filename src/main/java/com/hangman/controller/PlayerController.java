package com.hangman.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hangman.entity.Player;
import com.hangman.service.PlayerService;

@RestController
@CrossOrigin(origins = "*")
public class PlayerController {
	
	@Autowired
	private PlayerService playerService;
	
	
	// ----- GET -----	

	@GetMapping("/player/ip")
	public String getUserIp(HttpServletRequest request) {
		
		System.out.println("Navegador: " + request.getHeader("user-agent"));
		System.out.println("IP jugador: " + request.getRemoteAddr());
		System.out.println("IP Servidor local: " + request.getLocalAddr());

		return null;
	}
	
	
	
	// ----- POST -----	
	
	/**
	 * POST. Método para crear jugador.
	 * 
	 * @param player
	 * @return
	 */
	@PostMapping("/player")
	public ResponseEntity<?> registerPlayer(@RequestBody Player player, HttpServletRequest request) {
		
		ResponseEntity<?> response = null;
		
		// Establecer IP al jugador
		player.setIp(request.getRemoteAddr());

		// Crear jugador
		Player createPlayer = playerService.registerPlayer(player);
		
		// Comprobar si el jugador se ha creado
		if (createPlayer == null) {
			// Si el jugador no se ha creado (porque ya existe), se guarda error en la respuesta.
			response = ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR. El jugador indicado ya existe: " + player.getUsername());
		} else {
			// Si el jugador se ha creado, se guardan sus datos en la respuesta.
			response = ResponseEntity.status(HttpStatus.OK).body(player);
		}
				
		// Devolver respuesta http 
		return response;
	}

	

}
