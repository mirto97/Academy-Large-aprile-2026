package com.academy.esercizio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.esercizio.DAO.StudentDAO;
import com.academy.esercizio.entity.Student;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    // ho usato field injection per semplicità
    @Autowired
    private StudentDAO studentDAO;
    
    // mappo un endpoint Get che mi restituisce tutti gli studenti
    @GetMapping
    public ResponseEntity<List <Student>> findAll() {
        List<Student> s = studentDAO.findAll();
        System.out.println("Restituiti " + s.size() + " studenti");
        return ResponseEntity.ok(s);
    }

    // endpoint che restituisce un singolo studente dato l'id, altrimenti 404
    @GetMapping("/{id}")
    public ResponseEntity<Student> findById(@PathVariable int id) {
        Student s = studentDAO.findById(id);
        if (s != null) {
            System.out.println("Restituito studente con id " + id);
            return ResponseEntity.ok(s);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // endpoint post per caricare nuovo studente
    @PostMapping
    public ResponseEntity<Student> update(@RequestBody Student student) {
        Student s = studentDAO.save(student);
        System.out.println("Studente aggiunto con id " + s.getId());
        return ResponseEntity.status(201).body(s);
    }

    // endpoint put per aggiornare un studente esistente, altrimenti 404
    @PutMapping("/{id}")    
    public ResponseEntity<Student> update(@PathVariable int id, @RequestBody Student student) {
        Student existingStudent = studentDAO.findById(id);
        if (existingStudent != null) {
            student.setId(id); // assicuro che l'id del corpo della richiesta sia quello dell'entità esistente
            Student updatedStudent = studentDAO.update(student);
            System.out.println("Studente con id " + id + " aggiornato");
            return ResponseEntity.ok(updatedStudent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // endpoint delete per cancellare uno studente dato l'id, altrimenti 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (id > 0 && studentDAO.findById(id) != null) {
            studentDAO.deleteById(id);
            System.out.println("Studente con id " + id + " cancellato");
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}