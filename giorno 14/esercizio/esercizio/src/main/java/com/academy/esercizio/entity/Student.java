package com.academy.esercizio.entity;

import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entità che rappresenta uno studente")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // aggiunto accessMode = READ_ONLY perché quando crei uno studente via POST non ha senso che l'utente lo passi nel body — Swagger lo mostrerà solo nelle risposte.
    @Schema(description = "ID univoco generato automaticamente", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Column(nullable=false)
    @Schema(description = "Nome dello studente", example = "Mario")
    private String name;

    @Column(nullable=false)
    @Schema(description = "Cognome dello studente", example = "Rossi")
    private String surname;

    @Column(nullable=false, unique=true)
    @Schema(description = "Indirizzo email dello studente", example = "mario.rossi@example.com")
    private String email;

    @Column(nullable=false)
    @Schema(description = "Data di nascita dello studente", example = "1990-01-01")
    private LocalDate dob;
    
    @Schema(description = "Laurea dello studente", example = "Ingegneria")
    private String degree;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="profile_id")
    private StudentProfile profile;

}