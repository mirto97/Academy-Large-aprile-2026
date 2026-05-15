package com.academy.esercizio.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.academy.esercizio.exception.StudentGlobalException;
import com.academy.esercizio.exception.StudentNotFoundException;
import com.academy.esercizio.exception.UsernameAlreadyExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // questo gestisce tutte le eccezioni di tipo StudentNotFoundException
    // NON C'È BISOGNO DI INIETTARLO, è un gestore globale, basta che sia annotato con @RestControllerAdvice e che abbia un metodo annotato con @ExceptionHandler(StudentNotFoundException.class)
    // restituirà un oggetto StudentGlobalException con gli attributi che gli abbiamo assegnato
    // Il codice di stato HTTP sarà 404 NOT FOUND.
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<StudentGlobalException> StudentNotFound(StudentNotFoundException ex) {
        
        StudentGlobalException body = new StudentGlobalException(
            "Studente con id: " + ex.getId() + " non trovato",
            ex.getId(),
            LocalDateTime.now().toString()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleUsernameExists(UsernameAlreadyExistsException e) {
        // do errore 409, come richiesto
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

}
