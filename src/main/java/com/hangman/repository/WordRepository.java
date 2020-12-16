package com.hangman.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hangman.entity.Word;

@Repository(value = "wordRepository")
public interface WordRepository extends CrudRepository<Word, Long> {

//	/**
//	 * Método que busca una palabra por id
//	 * 
//	 * @param id
//	 * @return Word
//	 */
//	public Word findWordById(Long idWord);

}
