package com.academy.esercizio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.esercizio.entity.Course;
import com.academy.esercizio.entity.Student;
import com.academy.esercizio.service.CourseService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired 
    private CourseService service;

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateNameAndDepartment(@PathVariable int id, @RequestBody String name, String department) {
        return service.updateNameAndDepartment(id, name, department)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Corso con id: "+id+" non trovato"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Course>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}/students")
    public List<Student> getStudents(@PathVariable int id) {

        Course course = service.getCourseWithStudents(id);

        return course.getStudents();
    }
    
    @GetMapping("/students-count")
    public List<Object[]> countStudentsByCourse() {
        return service.countStudentsByCourse();
    }
}
