package com.academy.esercizio.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String surname;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private LocalDate dob;
    
    private String degree;

}