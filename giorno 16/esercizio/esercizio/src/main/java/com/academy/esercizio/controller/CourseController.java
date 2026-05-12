package com.academy.esercizio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.esercizio.entity.Course;
import com.academy.esercizio.entity.Student;
import com.academy.esercizio.service.CourseService;


@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired 
    private CourseService service;

    // non gestisco le eccezioni quindi faccio finta che le response siano tutte ok

    @GetMapping
    public ResponseEntity<List<Course>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}/studenti")
    public List<Student> getStudents(@PathVariable int id) {

        Course course = service.getCourseWithStudents(id);

        return course.getStudents();
    }
    
    @GetMapping("/students-count")
    public List<Object[]> countStudentsByCourse() {
        return service.countStudentsByCourse();
    }
}
