package com.academy.esercizio.exception;

public class StudentNotFoundException extends RuntimeException {
    
    private final int id;
    // aggiungo l'id a tutti i costruttori

    public StudentNotFoundException(int id) {
        this.id = id;
    }

    public StudentNotFoundException(String message , int id) {
        super("Studente non trovato: " + message);
        this.id = id;
    }

    public StudentNotFoundException(String message, Throwable cause , int id) {
        super("Studente non trovato: " + message, cause);
        this.id = id;
    }

    public StudentNotFoundException(Throwable cause, int id) {
        super("Studente non trovato", cause);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
