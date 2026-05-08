package com.academy.esercizio.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.academy.esercizio.entity.Student;
import com.academy.esercizio.exception.StudentNotFoundException;
import com.academy.esercizio.service.StudentServiceImpl;

import tools.jackson.databind.json.JsonMapper;


// ho messo spring data REST quindi manco servirebbe, però implementiamo cose a mano per vedere come si fa, e anche perchè magari un giorno volessi fare qualcosa di più complesso di un semplice CRUD, e quindi non potrei usare spring data REST, ma dovrei implementare io gli endpoint a mano 
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentServiceImpl service;
    private final JsonMapper jsonMapper;

    //@Autowired costructor injection. l'autowired qui è opzionale, perchè c'è un solo costruttore
    public StudentController(StudentServiceImpl service, JsonMapper jsonMapper) {
        this.service = service;
        this.jsonMapper = jsonMapper;
    }

    // patch aggiorna solo i campi presenti nel json, lasciando gli altri invariati, e restituisce lo studente aggiornato. Se lo studente non esiste, uso l'eccezione studentnotfound
    @PatchMapping("/{id}")
    public ResponseEntity<Student> patch(@PathVariable int id, @RequestBody Map<String, Object> json) {
        // prendo lo studente dal database, se non c'è lancio l'eccezione studentnotfound
        Student existing = service.findById(id)
        .orElseThrow(() -> new StudentNotFoundException(id));
        
        // aggiorno solo i campi presenti nel json, lasciando gli altri invariati
        jsonMapper.updateValue(existing, json);
        
        // salvo lo studente aggiornato e restituisco la risposta
        Student updated = service.save(existing);
        return ResponseEntity.ok(updated);
    }
    
    // in caso non trova niente restituisco sempre una lista vuota, non 404, perchè se cerco per laurea e non c'è nessuno con quella laurea, non è che il server non ha trovato la risorsa, ma semplicemente ha trovato una risorsa che è una lista vuota
    
    @GetMapping("/degree/{degree}")
    public ResponseEntity<List<Student>> findByDegree(@PathVariable String degree) {
        return service.findByDegree(degree).size() == 0 
        ? ResponseEntity.ok(List.of()) 
        : ResponseEntity.ok(service.findByDegree(degree));    
    }
    
    // endpoint per cercare studenti per cognome
    // deve restituire una lista vuota, non 404, se non trova nulla
    @GetMapping("/surname/{surname}")
    public ResponseEntity<List<Student>> findBySurname(@PathVariable String surname) {
        return service.findBySurname(surname).size() == 0 
        ? ResponseEntity.ok(List.of()) 
        : ResponseEntity.ok(service.findBySurname(surname));
    }
    
    // endpoint get che restituisce solo il corso di laurea dello studente con quell'ID
    // come stringa JSON. Se lo studente non esiste, uso l'eccezione studentnotfound
    @GetMapping("/{id}/degree")
    public ResponseEntity<String> findDegreeById(@PathVariable int id) {
        return service.findById(id)
        .map(student -> ResponseEntity.ok(student.getDegree()))
        .orElseThrow(() -> new StudentNotFoundException(id));
    }

    // @PageableDefault definisce i valori di default se il client non passa parametri
    @GetMapping
    public ResponseEntity<Page<Student>> findAll( @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
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