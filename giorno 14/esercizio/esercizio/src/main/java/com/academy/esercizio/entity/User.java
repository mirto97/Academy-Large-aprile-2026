package com.academy.esercizio.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Schema(description = "Entità che rappresenta un utente per l'autenticazione e l'autorizzazione")   
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID univoco generato automaticamente", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Column(nullable=false, unique=true)
    @Schema(description = "Username dell'utente", example = "gigi")
    private String username;

    @Column(nullable=false)
    @Size(min=60, max=60)
    @Schema(description = "Password dell'utente (in bcrypt)", example = "123")
    private String password;

    @Column(nullable=false)
    @Schema(description = "Indica se l'utente è attivo o meno", example = "true")
    private boolean active;
    
}
