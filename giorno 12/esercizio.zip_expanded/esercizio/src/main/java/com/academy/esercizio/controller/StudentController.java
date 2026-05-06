package com.academy.esercizio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.academy.esercizio.entity.Student;
import com.academy.esercizio.exception.StudentNotFoundException;
import com.academy.esercizio.service.StudentService;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired // field injection per semplicità
    private StudentService service;

    // endpoint get che restituisce solo  il corso di laurea dello studente con quell'ID
    // come stringa JSON. Se lo studente non esiste, uso l'eccezione studentnotfound
    @GetMapping("/{id}/degree")
    public ResponseEntity<String> findDegreeById(@PathVariable int id) {
        return service.findById(id)
                .map(student -> ResponseEntity.ok(student.getDegree()))
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    // endpoint per cercare studenti per cognome
    // deve restituire una lista vuota, non 404, se non trova nulla
    @GetMapping("/find")
    public ResponseEntity<List<Student>> findBySurname(@RequestParam String surname) {
        return service.findBySurname(surname).size() == 0 
        ? ResponseEntity.ok(List.of()) 
        : ResponseEntity.ok(service.findBySurname(surname));
    }
    

    @GetMapping
    public ResponseEntity<List<Student>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> findById(@PathVariable int id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        Student saved = service.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable int id, @RequestBody Student student) {
        return service.update(id, student)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return service.deleteById(id)
                ? ResponseEntity.noContent().build() // response 204, andata a buon fine ma non c'è nulla da restituire
                : ResponseEntity.notFound().build();
    }
}