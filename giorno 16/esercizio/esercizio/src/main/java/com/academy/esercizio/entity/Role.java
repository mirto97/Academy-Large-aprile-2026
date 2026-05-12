package com.academy.esercizio.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@Schema(description = "Entità che rappresenta un ruolo per l'autorizzazione")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID univoco generato automaticamente", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Column(nullable=false)
    @Schema(description = "ID dell'utente a cui è associato il ruolo", example = "1")
    private int user_id;

    @Column(nullable=false)
    @Schema(description = "Ruolo dell'utente (es. ROLE_USER, ROLE_ADMIN)", example = "ROLE_USER")
    private String role;
    
}
