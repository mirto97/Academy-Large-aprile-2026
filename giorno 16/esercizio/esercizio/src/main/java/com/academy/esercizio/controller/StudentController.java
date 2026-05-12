package com.academy.esercizio.controller;

import java.util.Comparator;
import java.util.HashMap;
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

import com.academy.esercizio.entity.Exam;
import com.academy.esercizio.entity.Student;
import com.academy.esercizio.exception.StudentNotFoundException;
import com.academy.esercizio.service.StudentServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import tools.jackson.databind.json.JsonMapper;


// ho messo spring data REST quindi manco servirebbe, però implementiamo cose a mano per vedere come si fa, e anche perchè magari un giorno volessi fare qualcosa di più complesso di un semplice CRUD, e quindi non potrei usare spring data REST, ma dovrei implementare io gli endpoint a mano 
@RestController
@RequestMapping("/api/students")
@Tag(name = "Studenti", description = "Gestione degli studenti")
public class StudentController {

    private final StudentServiceImpl service;
    private final JsonMapper jsonMapper;

    //@Autowired costructor injection. l'autowired qui è opzionale, perchè c'è un solo costruttore
    public StudentController(StudentServiceImpl service, JsonMapper jsonMapper) {
        this.service = service;
        this.jsonMapper = jsonMapper;
    }

    @Operation(summary = "Ottieni esami studente", description = "Restituisce la lista degli esami di uno studente, ordinati per data")
    @ApiResponse(responseCode = "200", description = "Esami recuperati con successo")
    @ApiResponse(responseCode = "404", description = "Studente non trovato")
    @GetMapping("/{id}/exams")
    public ResponseEntity<List<Exam>> getExams(@PathVariable int id) {
        Student student = service.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Exam> sorted = student.getExams().stream()
            // utilizzo un comparator per ordinarli per data
            .sorted(Comparator.comparing(Exam::getExamDate))
            .toList();
        return ResponseEntity.ok(sorted);
    }

    @Operation(summary = "Aggiungi esame a studente", description = "Aggiunge un nuovo esame a uno studente. Il voto deve essere compreso tra 18 e 30")
    @ApiResponse(responseCode = "201", description = "Esame aggiunto con successo")
    @ApiResponse(responseCode = "400", description = "Voto non valido (deve essere tra 18 e 30) oppure lode senza voto 30")
    @ApiResponse(responseCode = "404", description = "Studente non trovato")
    @PostMapping("/{id}/exams")
    public ResponseEntity<Exam> addExam(@PathVariable int id, @Valid @RequestBody Exam exam) {
        Student student = service.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        if (exam.isHonors() && exam.getGrade() != 30)
            return ResponseEntity.badRequest().build();
        student.getExams().add(exam);
        service.save(student);
        return ResponseEntity.status(201).body(exam);
    }

    @Operation(summary = "Calcola media voti studente", description = "Calcola e restituisce la media dei voti degli esami di uno studente")
    @ApiResponse(responseCode = "200", description = "Media calcolata con successo")
    @ApiResponse(responseCode = "404", description = "Studente non trovato")
    @GetMapping("/{id}/average")
    public ResponseEntity<Map<String, Object>> getAverage(@PathVariable int id) {
        Student student = service.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Exam> exams = student.getExams();
        double avg = exams.stream().mapToInt(Exam::getGrade).average().orElse(0.0);
        
        // uso una mappa per gestire i risultati
        Map<String, Object> result = new HashMap<>();
        result.put("student", student.getName() + " " + student.getSurname());
        result.put("average", Math.round(avg * 100.0) / 100.0);
        result.put("totalExams", exams.size());
        
        return ResponseEntity.ok(result);
    }



    // patch aggiorna solo i campi presenti nel json, lasciando gli altri invariati, e restituisce lo studente aggiornato. Se lo studente non esiste, uso l'eccezione studentnotfound
    @Operation(summary = "Aggiorna studente parzialmente", description = "Aggiorna solo i campi presenti nel JSON, lasciando gli altri invariati")
    @ApiResponse(responseCode = "200", description = "Studente aggiornato con successo")
    @ApiResponse(responseCode = "404", description = "Studente non trovato")
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
    
    @Operation(summary = "Trova studenti per corso di laurea")
    @ApiResponse(responseCode = "200", description = "Lista studenti (vuota se nessuno trovato)")
    @GetMapping("/degree/{degree}")
    public ResponseEntity<List<Student>> findByDegree(@PathVariable String degree) {
        return service.findByDegree(degree).size() == 0 
        ? ResponseEntity.ok(List.of()) 
        : ResponseEntity.ok(service.findByDegree(degree));    
    }
    
    // endpoint per cercare studenti per cognome
    // deve restituire una lista vuota, non 404, se non trova nulla
    @Operation(summary = "Trova studenti per cognome")
    @ApiResponse(responseCode = "200", description = "Lista studenti (vuota se nessuno trovato)")
    @GetMapping("/surname/{surname}")
    public ResponseEntity<List<Student>> findBySurname(@PathVariable String surname) {
        return service.findBySurname(surname).size() == 0 
        ? ResponseEntity.ok(List.of()) 
        : ResponseEntity.ok(service.findBySurname(surname));
    }
    
    // endpoint get che restituisce solo il corso di laurea dello studente con quell'ID
    // come stringa JSON. Se lo studente non esiste, uso l'eccezione studentnotfound
    @Operation(summary = "Restituisce solo il corso di laurea dello studente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Corso di laurea trovato"),
        @ApiResponse(responseCode = "404", description = "Studente non trovato")
    })
    @GetMapping("/{id}/degree")
    public ResponseEntity<String> findDegreeById(@PathVariable int id) {
        return service.findById(id)
        .map(student -> ResponseEntity.ok(student.getDegree()))
        .orElseThrow(() -> new StudentNotFoundException(id));
    }

    // @PageableDefault definisce i valori di default se il client non passa parametri
    @Operation(summary = "Lista studenti paginata", description = "Restituisce tutti gli studenti con supporto a paginazione e ordinamento")
    @ApiResponse(responseCode = "200", description = "Lista restituita con successo")
    @GetMapping
    public ResponseEntity<Page<Student>> findAll( @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(summary = "Trova studente per ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Studente trovato"),
        @ApiResponse(responseCode = "404", description = "Studente non trovato")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Student> findById(@PathVariable int id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Operation(summary = "Crea un nuovo studente")
    @ApiResponse(responseCode = "201", description = "Studente creato con successo")
    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        Student saved = service.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Aggiorna completamente uno studente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Studente aggiornato"),
        @ApiResponse(responseCode = "404", description = "Studente non trovato")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable int id, @RequestBody Student student) {
        return service.update(id, student)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Operation(summary = "Elimina uno studente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Studente eliminato"),
        @ApiResponse(responseCode = "404", description = "Studente non trovato")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return service.deleteById(id)
                ? ResponseEntity.noContent().build() // response 204, andata a buon fine ma non c'è nulla da restituire
                : ResponseEntity.notFound().build();
    }
}