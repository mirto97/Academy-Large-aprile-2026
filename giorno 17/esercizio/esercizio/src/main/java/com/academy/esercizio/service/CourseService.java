package com.academy.esercizio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academy.esercizio.entity.Course;
import com.academy.esercizio.repository.CourseRepository;

import jakarta.transaction.Transactional;

@Service
// non implemento l'interfaccia per velocizzare il processo, tanto non è un progetto reale
public class CourseService {

    @Autowired // field injection, per semplicità
    private CourseRepository repo;

    public List<Course> findAll() {
        return repo.findAll();
    }

    public Course getCourseWithStudents(int id) {
        return repo.getCourseWithStudents(id)
                .orElseThrow(() -> new RuntimeException("Corso non trovato"));
    }

    // lista di vettore di oggetti perchè sono i vari corsi con gli studenti
    public List<Object[]> countStudentsByCourse() {
        return repo.countStudentsByCourse();
    }

    @Transactional
    public Optional<Course> updateNameAndDepartment(int id, String name, String department) {
    return repo.findById(id)
            .map(existing -> {
                existing.setName(name);
                existing.setDepartment(department);
                return repo.save(existing); // aggiorna solo i campi modificati
            });
    }

    // cancella corso, lancia eccezione se ha studenti (opzione a)
    @Transactional
    public void delete(int id) {
        Course course = repo.findById(id).orElseThrow(() -> new RuntimeException("Corso non trovato con id: " + id));

        if (!course.getStudents().isEmpty()) {
            throw new IllegalStateException("Impossibile cancellare il corso " + id + ": perchè ha ancora " + course.getStudents().size() + " studenti");
        }
        repo.delete(course);
    }
}
