package com.hangman.service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//org.apache.commons.lang3


import com.hangman.entity.Game;
import com.hangman.entity.GameRound;
import com.hangman.entity.GameStatus;
import com.hangman.entity.Player;
import com.hangman.entity.Word;
import com.hangman.repository.GameRepository;
import com.hangman.repository.PlayerRepository;

@Service
public class GameService {
	
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private PlayerRepository playerRepo;
	
	
	private final String hiddenCharacter = "*";
	
	
	
	//  ----------------- TEST -----------------
	public GameService(GameRepository gameRepo) {
		this.gameRepo = gameRepo;
	}
	
	
	
	//  ----------- Working on text -----------
	/**
	 * Método que limpia el texto.
	 * Lo convierte a minúsculas y elimina los acentos.
	 * 
	 * @param texto
	 * @return String
	 */
    public String cleanString(String texto) {
    	texto = texto.toLowerCase();
    	
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }
	
    
	/**
	 * Método que selecciona una palabra aleatoria
	 * 
	 * @return Word
	 */
	public Word selectRandomWord() {
		
		// Se indica la longitud mínima de la palabra
		int minimumWordLength = 6;
		
		// Se declara la palabra aleatoria con valor inicial
		String randomWord = "a";		
		
		// Se busca una palabra que cumpla la longitud mínima y se guarda
		while (randomWord.length() < minimumWordLength) {
			int randomNumber = (int) Math.floor(Math.random() * dictionary.size());
			randomWord = dictionary.get(randomNumber);
		}
		
		// limpiar palabra (acentos o mayúsculas)
		randomWord = cleanString(randomWord);
		
		// Se crea la palabra con la cadena buscada anteriormente
		Word word = new Word(randomWord);
		
		// Se devuelve la palabra
		return word;
	}
	
	
	/**
	 * Método que inicializa la palabra en proceso de búsqueda, poniendo los caracteres ocultos.
	 * 
	 * @param wordToGuess
	 * @return Word
	 */
	public Word initWordInProcessToGuess(Word wordToGuess) {
		
		// Se crea una cadena auxiliar.
		StringBuilder aux = new StringBuilder();
		
		// Se rellena la cadena auxiliar con caracteres (*)
		for (int i = 0; i < wordToGuess.getWord().length(); i++) {
			aux.append(hiddenCharacter);
		}
		
		// Se crea la palabra en proceso, partiendo de la cadena auxiliar
		Word wordInProcessToGuess = new Word(aux.toString());
		
		// Se devuelve la palabra creada
		return wordInProcessToGuess;
	}
	//  ----------- Working on text -----------

	
	
	//  ----------------- GET -----------------	
	/**
	 * Método que obtiene una partida, buscando por id
	 * 
	 * @param idGame
	 * @return
	 */
	public Game getGameById(Long idGame) {
		return 	gameRepo.findGameById(idGame);
	}
	
	
	/**
	 * Método que obtiene una partida, buscando por jugador
	 * 
	 * @param player
	 * @return Game
	 */
	public Game getGameByPlayer(Player player) {
		return gameRepo.findGameByPlayer(player);
	}

	
	/**
	 * Método que obtiene una partida, buscando por jugador
	 * 
	 * @param player
	 * @return Game
	 */
	public Game getGameInfoForAdmin(Long idGame, String ip) {
		
		Game game = null;
		
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			game = gameRepo.findGameById(idGame);
		}
		
		return 	game;
	}
	
	
	/**
	 * Método que obtiene información de una partida (enfocado al jugador)
	 * 
	 * @param idGame
	 * @param username
	 * @param ip
	 * @return GameRound
	 */
	public GameRound getGameInfoForPlayer(Long idGame, String username, String ip) {

		// Resultado
		GameRound result = null;

		// Partida - Juego
		Game game = null;

		// Se obtiene jugador buscando por nombre de usuario
		Player player = playerRepo.findPlayerByUsername(username);

		// Si el jugador existe y la IP es correcta
		if (player != null && ip.equals(player.getIp())) {

			// Se guarda la partida
			game = gameRepo.findGameById(idGame);

			// Se guarda el resultado (información de la ronda)
			result = new GameRound(game.getWordInProcessToGuess(), game.getLives(), game.getMistakes(), game.getHits(),
					game.getAttempts(), game.getLetters(), game.getWords(), game.getRoundMessage());
		}

		return result;
	}
	//  ----------------- GET -----------------	
	
	
	//  ----------------- POST -----------------	
	/**
	 * Método que inicia una partida
	 * 
	 * @param player
	 * @return Game
	 * @throws Exception 
	 */
	public Game startGame(Player player) {
		
		Game newGame = null;
		
		if (player != null) {
			// Se crea palabra aleatoria
			Word wordToGuess = selectRandomWord();
			
			// Se crea palabra con carácteres (*), de la misma longitud a la anterior
			Word wordInProcessToGuess = initWordInProcessToGuess(wordToGuess);
			
			// Se crea partida, incluyendo las palabras anteriores y el jugador pasado por parámetro.
			newGame = new Game(wordToGuess, wordInProcessToGuess, player);
			
			// Se establece el estado de partida: en progreso.
			newGame.setStatus(GameStatus.IN_PROGRESS);
			
			// Se guarda la partida en BBDD
			gameRepo.save(newGame);
		}
		
		// Se devuelve la partida
		return newGame;
	}
	
	
	/**
	 * Método que realiza un intento para buscar la palabra, indicando una letra
	 * 
	 * @param game
	 * @param letter
	 * @return boolean
	 * @throws Exception 
	 */
	public GameRound makeLetterAttempt(String username, Long idGame, Character letter, String ip) throws Exception {
		
		// Se limpia la letra, quitando acentos o mayúsculas
		letter = cleanString(letter.toString()).charAt(0);
				
		// Se busca y guarda la partida
		Game game = gameRepo.findGameById(idGame);
		
		// Se busca y guarda el jugador
		Player player = playerRepo.findPlayerByUsername(username);
		
		
		// Método que realiza las comprobaciones necesarias
		attemptChecks(game, player, ip);
		letterAttemptChecks(game, letter);

		
		if (gameCanContinue(game)) {
			
			// Se suma un intento
			game.setAttempts(game.getAttempts() + 1);
			
			// Se obtienen las palabras de la partida
			Word wordToGuess = game.getWordToGuess();
			Word wordInProcessToGuess = game.getWordInProcessToGuess();
			
			// Se registra la letra en la lista de letras usadas por el jugador.
			game.addLetterToList(letter);
						
			
			// Se crea una cadena auxiliar, partiendo de la palabra en proceso.
			StringBuilder auxWordInProcess = new StringBuilder(wordInProcessToGuess.getWord());

			// Si la palabra para adivinar contiene la letra pasada por parámetro...
			if (wordToGuess.getWord().contains(letter.toString())) {
				
				// Se recorre la palabra
				for (int i = 0; i < wordToGuess.getWord().length(); i++) {
					
					// Se guarda la letra recorrida
					Character letterInWord = wordToGuess.getWord().charAt(i);
					
					// Si la letra recorrida coincide con la letra pasada por parámetro
					if (letterInWord.equals(letter)) {
						
						// Se modifica la cadena auxiliar.
						// (se cambia letra de posición recorrida por letra pasada por parámetro).
						auxWordInProcess.setCharAt(i, letter);
					}
					
				}
				
				// Se modifica la palabra para adivinar, cambiándola por la cadena auxiliar.
				wordInProcessToGuess.setWord(auxWordInProcess.toString());
				
				// Se establece el estado de la ronda (ganada o acierto), según estado de la palabra en proceso
				setStatusVictoryOrSuccess(wordInProcessToGuess, game);
				
			// Si la palabra para adivinar NO contiene la letra pasada por parámetro...
			} else {
				
				// Se establece el estado de la ronda (perdida o fallo), según estado de la palabra en proceso
				setStatusMissOrGameOver(game);
				
			}
			
			// Se guarda la partida en BBDD
			gameRepo.save(game);
			
		} 
		
		// DTO. Resultado de la ronda
		GameRound gameRound = new GameRound(game.getWordInProcessToGuess(), 
				game.getLives(), game.getMistakes(), game.getHits(), 
				game.getAttempts(), game.getLetters(), game.getWords(), game.getRoundMessage());
		
		// Se devuelve el resultado de la ronda
		return gameRound;
	}
	
	
	/**
	 * Método que realiza un intento para buscar la palabra, indicando una letra
	 * 
	 * @param game
	 * @param letter
	 * @return boolean
	 * @throws Exception 
	 */
	public GameRound makeWordAttempt(String username, Long idGame, String word, String ip) throws Exception {
		
		// Se limpia la palabra, quitando acentos o mayúsculas
		word = cleanString(word);
				
		// Se busca y guarda la partida
		Game game = gameRepo.findGameById(idGame);
		
		// Se busca y guarda el jugador
		Player player = playerRepo.findPlayerByUsername(username);
		
		
		// Método que realiza las comprobaciones necesarias
		attemptChecks(game, player, ip);
		wordAttemptChecks(game, word);
		
		if (gameCanContinue(game)) {
			
			// Se suma un intento
			game.setAttempts(game.getAttempts() + 1);
			
			// Se obtienen las palabras de la partida
			Word wordToGuess = game.getWordToGuess();
			Word wordInProcessToGuess = game.getWordInProcessToGuess();
			
			// Se registra la palbra en la lista de palabras usadas por el jugador.
			game.addWordToList(word);		
			
			// Si la palabra para adivinar coincide con la palabra pasada por parámetro...
			if (wordToGuess.getWord().equals(word)) {
				
				// Se modifica la palabra para adivinar, cambiándola por la cadena auxiliar.
				wordInProcessToGuess.setWord(word);
								
				// Se establece el estado de la ronda (ganada)
				game.setStatus(GameStatus.VICTORY);
				
				// Se indica mensaje correspondiente
				game.setRoundMessage("HAS GANADO!!!");

				
			// Si la palabra para adivinar NO contiene la letra pasada por parámetro...
			} else {
				
				// Se establece el estado de la ronda (perdida o fallo), según estado de la palabra en proceso
				setStatusMissOrGameOver(game);
				
			}
			
			// Se guarda la partida en BBDD
			gameRepo.save(game);
			
		} 
		
		// DTO. Resultado de la ronda
		GameRound gameRound = new GameRound(game.getWordInProcessToGuess(), 
				game.getLives(), game.getMistakes(), game.getHits(), 
				game.getAttempts(), game.getLetters(), game.getWords(), game.getRoundMessage());
		
		// Se devuelve el resultado de la ronda
		return gameRound;
	}
	//  ----------------- POST -----------------	


	
	public boolean containsOnlyLetters(String text) {
		
		boolean result = true;
		
	    for (int i = 0; i < text.length(); i++) {
	        char c = text.charAt(i);
	        
	        // Si no está entre a y z
	        if (!(c >= 'a' && c <= 'z')) {
	            result = false;
	        }
	    }
	    
	    return result;
	}
	
	
	public void attemptChecks(Game game, Player player, String ip) throws Exception {
		
		// Si no se encuentra la partida, se muestra excepción
		if (game == null) {
			throw new Exception("ERROR. La partida indicada no existe.");
		}
		
		// Si no se encuentra el jugador, se muestra excepción
		if (player == null) {
			throw new Exception("ERROR. El jugador indicado no existe.");
		}
		
		// Si el jugador pasado por parámetro no coincide con el jugador de la partida, se muestra excepción
		if (!player.equals(game.getPlayer())) {
			throw new Exception("ERROR. El jugador (" + player.getUsername() + ") no tiene acceso a esta partida.");
		}
		
		// Se controla que un jugador desconocido no pueda acceder a la partida de otro jugador
		if (!ip.equals(player.getIp())) {
			throw new Exception("ERROR. No puedes acceder a esta partida.");
		}
		
	}
	
	public void letterAttemptChecks(Game game, Character letter) throws Exception {
					
		// Se controla que solo pueda insertar letras (no números ni caracteres especiales)
		if (!Character.isLetter(letter)) {
			throw new Exception("ERROR. No has introducido una letra, vuelve a intentarlo.");
		}
		
		// Se controla si la letra se ha usado en algún turno anterior
		if (game.getLetters().contains(letter)) {
			throw new Exception("ERROR. Ya has introducido la letra '" + letter + "', inténtalo de nuevo");
		}
		
	}
	
	public void wordAttemptChecks(Game game, String word) throws Exception {
		
		// Se controla que solo pueda insertar letras en la palabra (no números ni caracteres especiales)
		for (int i = 0; i < word.length(); i++) {
			if (!Character.isLetter(word.charAt(i))) {
				throw new Exception("ERROR. No has introducido una palabra, vuelve a intentarlo.");
			}
		}
		
		// Se controla si la letra se ha usado en algún turno anterior
		if (game.getWords().contains(word)) {
			throw new Exception("ERROR. Ya has introducido la palabra '" + word + "', inténtalo de nuevo");
		}
		
	}


	
	public boolean gameCanContinue(Game game) {
		boolean result = false;
		
		if (game.getStatus().equals(GameStatus.IN_PROGRESS) || game.getStatus().equals(GameStatus.SUCCESS) || game.getStatus().equals(GameStatus.MISS)) {
			result = true;
		}

		return result;
	}
	
	public void setStatusVictoryOrSuccess(Word wordInProcessToGuess, Game game) {
		
		// Se comprueba si la palabra para adivinar tiene el carácter oculto
		if (!wordInProcessToGuess.getWord().contains(hiddenCharacter)) {
			// Si no lo tiene, el jugador ha ganado la partida
			game.setStatus(GameStatus.VICTORY);
			
			// Se indica mensaje correspondiente
			game.setRoundMessage("HAS GANADO!!!");
		} else {
			// si lo tiene, el jugador ha acertado pero debe seguir jugando
			game.setStatus(GameStatus.SUCCESS);
			
			// Se suma un acierto
			game.setHits(game.getHits() + 1);
			
			// Se indica mensaje correspondiente
			game.setRoundMessage("Bien, has acertado la letra!");
		}
		
	}
	
	public void setStatusMissOrGameOver(Game game) {
		
		// Se incrementa el número de fallos del jugador
		game.setMistakes(game.getMistakes() + 1);				
		
		// Se resta una vida (intento disponible)
		game.setLives(game.getLives() - 1);
		
		// Se comprueba si quedan vidas
		if (game.getLives() == 0) {
			
			// Si no quedan vidas, el jugador ha perdido
			game.setStatus(GameStatus.GAME_OVER);
			
			// Se indica mensaje correspondiente
			game.setRoundMessage("Has perdido...");
		} else {
			// Si quedan vidas, solo se cambia el estado a fallo de palabra
			game.setStatus(GameStatus.MISS);
			
			// Se indica mensaje correspondiente
			game.setRoundMessage("Mala suerte, no has acertado...");
		}
		
	}
	
	
	
	
	
	/**
	 * Lista de palabras disponibles para el juego
	 */
	List<String> dictionary = new ArrayList<>(Arrays.asList("a", "abajo", "abandonar", "abrir", "absoluta", "absoluto",
			"abuelo", "acabar", "acaso", "acciones", "acción", "aceptar", "acercar", "acompañar", "acordar", "actitud",
			"actividad", "acto", "actual", "actuar", "acudir", "acuerdo", "adelante", "además", "adquirir", "advertir",
			"afectar", "afirmar", "agua", "ahora", "aire", "al", "alcanzar", "alejar", "alemana", "alemán", "algo",
			"alguien", "alguna", "alguno", "algún", "allá", "allí", "alma", "alta", "alto", "altura", "amar", "ambas",
			"ambos", "americana", "americano", "amiga", "amigo", "amor", "amplia", "amplio", "andar", "animal", "ante",
			"anterior", "antigua", "antiguo", "anunciar", "análisis", "aparecer", "apenas", "aplicar", "apoyar",
			"aprender", "aprovechar", "aquel", "aquella", "aquello", "aquí", "arma", "arriba", "arte", "asegurar",
			"aspecto", "asunto", "atenciones", "atención", "atreverse", "atrás", "aumentar", "aunque", "autor",
			"autora", "autoridad", "auténtica", "auténtico", "avanzar", "ayer", "ayuda", "ayudar", "azul", "añadir",
			"baja", "bajar", "barrio", "base", "bastante", "bastar", "beber", "bien", "blanca", "blanco", "boca",
			"brazo", "buen", "buscar", "caballo", "caber", "cabeza", "cabo", "cada", "cadena", "caer", "calle", "cama",
			"cambiar", "cambio", "caminar", "camino", "campaña", "campo", "cantar", "cantidad", "capaces", "capacidad",
			"capaz", "capital", "cara", "caracteres", "carne", "carrera", "carta", "carácter", "casa", "casar", "casi",
			"caso", "catalán", "causa", "celebrar", "central", "centro", "cerebro", "cerrar", "chica", "chico", "cielo",
			"ciencia", "ciento", "científica", "científico", "cierta", "cierto", "cinco", "cine", "circunstancia",
			"ciudad", "ciudadana", "ciudadano", "civil", "clara", "claro", "clase", "coche", "coger", "colocar",
			"color", "comentar", "comenzar", "comer", "como", "compañera", "compañero", "compañía", "completo",
			"comprar", "comprender", "comprobar", "comunes", "comunicaciones", "comunicación", "común", "concepto",
			"conciencia", "concreto", "condición", "condisiones", "conducir", "conjunto", "conocer", "conocimiento",
			"consecuencia", "conseguir", "conservar", "considerar", "consistir", "constante", "constituir", "construir",
			"contacto", "contar", "contemplar", "contener", "contestar", "continuar", "contra", "contrario", "control",
			"controlar", "convencer", "conversación", "convertir", "corazón", "correr", "corresponder", "corriente",
			"cortar", "cosa", "costumbre", "crear", "crecer", "creer", "crisis", "cruzar", "cuadro", "cual",
			"cualquier", "cuando", "cuanto", "cuarta", "cuarto", "cuatro", "cubrir", "cuenta", "cuerpo", "cuestiones",
			"cuestión", "cultura", "cultural", "cumplir", "cuya", "cuyo", "cuál", "cuánto", "célula", "cómo", "dato",
			"de", "deber", "decidir", "decir", "decisión", "declarar", "dedicar", "dedo", "defender", "defensa",
			"definir", "definitivo", "dejar", "demasiado", "democracia", "demostrar", "demás", "depender", "derecha",
			"derecho", "desaparecer", "desarrollar", "desarrollo", "desconocer", "descubrir", "desde", "desear",
			"deseo", "despertar", "después", "destino", "detener", "determinar", "diaria", "diario", "diez",
			"diferencia", "diferente", "dificultad", "difícil", "dinero", "dios", "diosa", "dirección", "directo",
			"director", "directora", "dirigir", "disponer", "distancia", "distinto", "diverso", "doble", "doctor",
			"doctora", "dolor", "donde", "dormir", "duda", "durante", "duro", "dónde", "e", "echar", "económico",
			"edad", "efecto", "ejemplo", "ejército", "el", "elección", "elegir", "elemento", "elevar", "ella",
			"empezar", "empresa", "en", "encender", "encima", "encontrar", "encuentro", "energía", "enfermedad",
			"enfermo", "enorme", "enseñar", "entender", "enterar", "entonces", "entrada", "entrar", "entre", "entregar",
			"enviar", "equipo", "error", "escapar", "escribir", "escritor", "escritora", "escuchar", "esfuerzo",
			"espacio", "espalda", "españa", "español", "española", "especial", "especie", "esperanza", "esperar",
			"espíritu", "esta", "establecer", "estado", "estar", "este", "esto", "estrella", "estructura", "estudiar",
			"estudio", "etapa", "europa", "europea", "europeo", "evidente", "evitar", "exacta", "exacto", "exigir",
			"existencia", "existir", "experiencia", "explicar", "expresión", "extender", "exterior", "extranjera",
			"extranjero", "extraño", "extremo", "falta", "faltar", "familia", "familiar", "famoso", "fenómeno",
			"fiesta", "figura", "fijar", "final", "flor", "fondo", "forma", "formar", "francesa", "francia", "francés",
			"frase", "frecuencia", "frente", "fría", "frío", "fuego", "fuente", "fuerte", "fuerza", "funcionar",
			"función", "fundamental", "futuro", "fácil", "físico", "fútbol", "ganar", "general", "gente", "gesto",
			"gobierno", "golpe", "gracia", "gran", "grande", "grave", "gritar", "grupo", "guardar", "guerra", "gustar",
			"gusto", "haber", "habitación", "habitual", "hablar", "hacer", "hacia", "hallar", "hasta", "hecha", "hecho",
			"hermana", "hermano", "hermosa", "hermoso", "hija", "hijo", "historia", "histórico", "hombre", "hombro",
			"hora", "humana", "humano", "idea", "iglesia", "igual", "imagen", "imaginar", "impedir", "imponer",
			"importancia", "importante", "importar", "imposible", "imágenes", "incluir", "incluso", "indicar",
			"individuo", "informaciones", "información", "informar", "inglesa", "inglés", "iniciar", "inmediata",
			"inmediato", "insistir", "instante", "intentar", "interesar", "intereses", "interior", "internacional",
			"interés", "introducir", "ir", "izquierda", "jamás", "jefa", "jefe", "joven", "juego", "jugador", "jugar",
			"juicio", "junto", "justo", "labio", "lado", "lanzar", "largo", "lector", "lectora", "leer", "lengua",
			"lenguaje", "lento", "levantar", "libertad", "libre", "libro", "limitar", "literatura", "llamar", "llegar",
			"llenar", "lleno", "llevar", "llorar", "lo", "loca", "loco", "lograr", "lucha", "luego", "lugar", "línea",
			"madre", "mala", "malo", "mandar", "manera", "manifestar", "mano", "mantener", "marcar", "marcha",
			"marchar", "marido", "masa", "matar", "materia", "material", "mayor", "mayoría", "mañana", "media",
			"mediante", "medida", "medio", "mejor", "memoria", "menor", "menos", "menudo", "mercado", "merecer", "mesa",
			"meter", "metro", "mi", "miedo", "miembro", "mientras", "militar", "millón", "ministra", "ministro",
			"minuto", "mirada", "mirar", "mismo", "mitad", "modelo", "moderna", "moderno", "modo", "momento", "moral",
			"morir", "mostrar", "motivo", "mover", "movimiento", "muchacha", "muchacho", "mucho", "muerte", "mujer",
			"mujeres", "mundial", "mundo", "máquina", "máximo", "médica", "médico", "método", "mí", "mínima", "mínimo",
			"música", "nacer", "nacional", "nada", "nadie", "natural", "naturaleza", "necesaria", "necesario",
			"necesidad", "necesitar", "negar", "negocio", "negra", "negro", "ni", "ninguna", "ninguno", "ningún",
			"nivel", "niña", "niño", "no", "noche", "nombre", "normal", "norteamericana", "norteamericano", "notar",
			"noticia", "novela", "nuestra", "nuestro", "nueva", "nuevo", "nunca", "número", "o", "objetivo", "objeto",
			"obligar", "obra", "observar", "obtener", "ocasiones", "ocasión", "ocho", "ocupar", "ocurrir", "oficial",
			"ofrecer", "olvidar", "operación", "opinión", "origen", "orígenes", "oscura", "oscuro", "otra", "otro",
			"paciente", "padre", "pagar", "palabra", "papel", "para", "parar", "parecer", "pared", "pareja", "parte",
			"participar", "particular", "partido", "partir", "pasa", "pasado", "pasar", "paso", "país", "países",
			"pecho", "pedir", "peligro", "pelo", "película", "pena", "pensamiento", "pensar", "peor", "pequeña",
			"pequeño", "perder", "perfecto", "periodista", "periódica", "periódico", "permanecer", "permitir", "pero",
			"perra", "perro", "persona", "personaje", "personal", "pertenecer", "pesar", "peso", "piedra", "piel",
			"pierna", "piso", "placer", "plan", "plantear", "plaza", "pleno", "poblaciones", "población", "pobre",
			"poca", "poco", "poder", "policía", "política", "político", "poner", "porque", "poseer", "posibilidad",
			"posible", "posiciones", "posición", "precio", "precisa", "preciso", "preferir", "pregunta", "preguntar",
			"prensa", "preocupar", "preparar", "presencia", "presentar", "presente", "presidente", "pretender",
			"primer", "primera", "primero", "principal", "principio", "privar", "probable", "problema", "proceso",
			"producir", "producto", "profesional", "profesor", "profesora", "profunda", "profundo", "programa",
			"pronta", "pronto", "propia", "propio", "proponer", "provocar", "proyecto", "prueba", "práctico", "próxima",
			"próximo", "publicar", "pueblo", "puerta", "pues", "puesto", "punto", "pura", "puro", "página", "pública",
			"público", "quedar", "querer", "quien", "quitar", "quizá", "quién", "radio", "rato", "razones", "razón",
			"real", "realidad", "realizar", "recibir", "reciente", "recoger", "reconocer", "recordar", "recorrer",
			"recuerdo", "recuperar", "reducir", "referir", "regresar", "relaciones", "relación", "religiosa",
			"religioso", "repetir", "representar", "resolver", "responder", "responsable", "respuesta", "resto",
			"resultado", "resultar", "reuniones", "reunir", "reunión", "revista", "reír", "rica", "rico", "riesgo",
			"rodear", "roja", "rojo", "romper", "ropa", "rostro", "rápida", "rápido", "régimen", "saber", "sacar",
			"sala", "salida", "salir", "sangre", "secreta", "secreto", "sector", "seguir", "segundo", "segura",
			"seguridad", "seguro", "según", "seis", "semana", "semejante", "sensaciones", "sensación", "sentar",
			"sentida", "sentido", "sentimiento", "sentir", "separar", "seria", "serie", "serio", "servicio", "servir",
			"sexo", "sexual", "señalar", "señor", "señora", "si", "sido", "siempre", "siete", "siglo", "significar",
			"siguiente", "silencio", "simple", "sino", "sistema", "sitio", "situaciones", "situación", "situar",
			"sobre", "social", "socialista", "sociedad", "sola", "solo", "soluciones", "solución", "sombra", "someter",
			"sonar", "sonreír", "sonrisa", "sorprender", "sostener", "su", "subir", "suceder", "suelo", "suerte",
			"sueño", "suficiente", "sufrir", "superar", "superior", "suponer", "surgir", "suya", "suyo", "sí", "sólo",
			"también", "tampoco", "tanta", "tanto", "tarde", "tarea", "televisiones", "televisión", "tema", "temer",
			"tender", "tener", "teoría", "tercer", "terminar", "texto", "tiempo", "tierra", "tipa", "tipo", "tirar",
			"tocar", "toda", "todavía", "todo", "tomar", "tono", "total", "trabajar", "trabajo", "traer", "tras",
			"tratar", "tres", "tu", "técnica", "técnico", "término", "título", "un", "unidad", "unir", "usar", "usted",
			"utilizar", "vacía", "vacío", "valer", "valor", "varias", "varios", "veces", "vecina", "vecino", "veinte",
			"velocidad", "vender", "venir", "ventana", "verano", "verdad", "verdadera", "verdadero", "verde", "vestir",
			"viaje", "vida", "vieja", "viejo", "viento", "violencia", "vista", "viva", "vivir", "vivo", "voces",
			"voluntad", "volver", "vuelta", "y", "ya", "yo", "zona", "árbol", "él", "época", "ésta", "éste", "éxito",
			"última", "último", "única", "único"));

			List<String> tempDictionary1 = new ArrayList<>(Arrays.asList("ahora", "absoluta", "acordar", "alcanzar", "apoyar", "animal"));
			List<String> tempDictionary2 = new ArrayList<>(Arrays.asList("árbol", "sílaba", "diré", "difícil", "anís"));

}



/** COPIA SAGRADA - makeAttempt */
///**
// * Método que realiza un intento para buscar la palabra, indicando una letra
// * 
// * @param game
// * @param letter
// * @return boolean
// * @throws Exception 
// */
//public GameRound makeAttempt(String username, Long idGame, Character letter, String ip) throws Exception {
//	
//	// Se limpia la letra, quitando acentos o mayúsculas
//	letter = cleanString(letter.toString()).charAt(0);
//			
//	// Se busca y guarda la partida
//	Game game = gameRepo.findGameById(idGame);
//	
//	// Se busca y guarda el jugador
//	Player player = playerRepo.findPlayerByUsername(username);
//	
//	
//	// Método que realiza las comprobaciones necesarias
//	attemptChecks(game, player, letter, ip);
//	
//	
//	if (game.getStatus().equals(GameStatus.IN_PROGRESS) || game.getStatus().equals(GameStatus.SUCCESS) || game.getStatus().equals(GameStatus.MISS)) {
//		
//		// Se suma un intento
//		game.setAttempts(game.getAttempts() + 1);
//		
//		// Se obtienen las palabras de la partida
//		Word wordToGuess = game.getWordToGuess();
//		Word wordInProcessToGuess = game.getWordInProcessToGuess();
//		
//		// Se registra la letra en la lista de letras usadas por el jugador.
//		game.addLetterToList(letter);
//		
//		
//		// Se crea una cadena auxiliar, partiendo de la palabra en proceso.
//		StringBuilder auxWordInProcess = new StringBuilder(wordInProcessToGuess.getWord());
//
//		// Si la palabra para adivinar contiene la letra pasada por parámetro...
//		if (wordToGuess.getWord().contains(letter.toString())) {
//			
//			// Se recorre la palabra
//			for (int i = 0; i < wordToGuess.getWord().length(); i++) {
//				
//				// Se guarda la letra recorrida
//				Character letterInWord = wordToGuess.getWord().charAt(i);
//				
//				// Si la letra recorrida coincide con la letra pasada por parámetro
//				if (letterInWord.equals(letter)) {
//					
//					// Se modifica la cadena auxiliar.
//					// (se cambia letra de posición recorrida por letra pasada por parámetro).
//					auxWordInProcess.setCharAt(i, letter);
//				}
//				
//			}
//			
//			// Se modifica la palabra para adivinar, cambiándola por la cadena auxiliar.
//			wordInProcessToGuess.setWord(auxWordInProcess.toString());
//			
//			
//			// Se comprueba si la palabra para adivinar tiene el carácter oculto
//			if (!wordInProcessToGuess.getWord().contains(hiddenCharacter)) {
//				// Si no lo tiene, el jugador ha ganado la partida
//				game.setStatus(GameStatus.VICTORY);
//				
//				// Se indica mensaje correspondiente
//				game.setRoundMessage("HAS GANADO!!!");
//			} else {
//				// si lo tiene, el jugador ha acertado pero debe seguir jugando
//				game.setStatus(GameStatus.SUCCESS);
//				
//				// Se suma un acierto
//				game.setHits(game.getHits() + 1);
//				
//				// Se indica mensaje correspondiente
//				game.setRoundMessage("Bien, has acertado la letra!");
//			}
//
//			
//		// Si la palabra para adivinar NO contiene la letra pasada por parámetro...
//		} else {
//			
//			// Se incrementa el número de fallos del jugador
//			game.setMistakes(game.getMistakes() + 1);				
//			
//			// Se resta una vida (intento disponible)
//			game.setLives(game.getLives() - 1);
//			
//			// Se comprueba si quedan vidas
//			if (game.getLives() == 0) {
//				
//				// Si no quedan vidas, el jugador ha perdido
//				game.setStatus(GameStatus.GAME_OVER);
//				
//				// Se indica mensaje correspondiente
//				game.setRoundMessage("Has perdido...");
//			} else {
//				// Si quedan vidas, solo se cambia el estado a fallo de palabra
//				game.setStatus(GameStatus.MISS);
//				
//				// Se indica mensaje correspondiente
//				game.setRoundMessage("Mala suerte, esa letra no existe...");
//			}
//			
//		}
//		
//		// Se guarda la partida en BBDD
//		gameRepo.save(game);
//		
//	} 
//	
//	// Resultado de la ronda
//	GameRound gameRound = new GameRound(game.getWordInProcessToGuess(), 
//			game.getLives(), game.getMistakes(), game.getHits(), 
//			game.getAttempts(), game.getLetters(), game.getRoundMessage());
//	
//	// Se devuelve el resultado de la ronda
//	return gameRound;
//}

