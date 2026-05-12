package com.academy.esercizio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academy.esercizio.entity.Course;
import com.academy.esercizio.repository.CourseRepository;

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

    public List<Object[]> countStudentsByCourse() {
        return repo.countStudentsByCourse();
    }
}
