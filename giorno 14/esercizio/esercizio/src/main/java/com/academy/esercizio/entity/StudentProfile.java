package com.academy.esercizio.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entità per le informazioni dello studente")
public class StudentProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID univoco generato automaticamente", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Schema(description = "Profilo linkedin")
    private String linkedin;

    @Schema(description = "Descrizione")
    private String bio;

    @Schema(description = "Url della foto profilo")
    private String photo_url;

    @Column(nullable=false)
    @Schema(description = "Data e ora di creazione")
    private LocalDateTime createdAt; 

    @OneToOne(mappedBy = "profile")
    @JsonIgnore // per evitare la serializzazione e i cicli
    private Student student;


}
