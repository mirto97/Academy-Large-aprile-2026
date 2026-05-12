package com.academy.esercizio.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID univoco generato automaticamente", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Column(unique = true)
    private String name;

    private String department;

    // durata in anni
    private int years;

    // gli dico di mappare in riferimento alla proprietà course in student
    @OneToMany(mappedBy="course", cascade = CascadeType.ALL)
    // per evitare cicli, questo nel padre
    @JsonManagedReference
    private List<Student> students = new ArrayList<>();

    // convenience method
    public void add(Student s) {
        if (students == null) {
            students = new ArrayList<>();
        }
        students.add(s);
        s.setCourse(this);
    }
}
