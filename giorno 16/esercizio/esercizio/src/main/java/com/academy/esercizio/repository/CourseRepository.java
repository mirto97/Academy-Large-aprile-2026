package com.academy.esercizio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academy.esercizio.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    // join fetch snellisce la query
    @Query("SELECT c FROM Course c JOIN FETCH c.students WHERE c.id = :id")
    Optional<Course> getCourseWithStudents(@Param("id") int id);

    @Query("SELECT c.name, COUNT(s) FROM Course c JOIN c.students s GROUP BY c.name")
    List<Object[]> countStudentsByCourse();
    
}
