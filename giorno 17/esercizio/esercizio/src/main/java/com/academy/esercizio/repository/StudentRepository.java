package com.academy.esercizio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.academy.esercizio.entity.Student;

//@RepositoryRestResource(path = "students")
// potrei usare questa annotazione per esporre automaticamente un endpoint REST per questa risorsa
// e con path indico il nome dell'endpoint, che di default sarebbe lo stesso del nome della classe con la prima lettera minuscola e una "s" alla fine per il plurale. Qua l'ho scritto a fine didattico
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    
    // alcuni metodi posso anche non scriverli perchè già sono implementati
    // update nemmeno si scrive, perchè save fa sia insert che update a seconda se l'id è presente o no (come entitymanager.merge)
    Optional<Student> findById(int id);

    @Query("SELECT s FROM Student s WHERE s.degree = :degree")
    List<Student> findByDegree(String degree);
    
    @Query("SELECT s FROM Student s WHERE s.surname = :surname")
    List<Student> findBySurname(String surname);

    @Query("SELECT s FROM Student s WHERE s.name LIKE %:parte%")
    List<Student> findByNameContaining(String parte); 
    
    // JOIN su s.exams, raggruppa per studente, filtra con HAVING sulla media
    @Query("SELECT s FROM Student s JOIN s.exams e GROUP BY s HAVING AVG(e.grade) > :threshold")
    List<Student> findStudentsWithAverageGradeAbove(@Param("threshold") double threshold);
}
