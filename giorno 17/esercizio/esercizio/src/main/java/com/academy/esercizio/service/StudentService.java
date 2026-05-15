package com.academy.esercizio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.academy.esercizio.entity.Exam;
import com.academy.esercizio.entity.Student;

public interface StudentService {

    Page<Student> findAll(Pageable pageable);

    Optional<Student> findById(int id);

    Student save(Student student);

    Optional<Student> update(int id, Student student);

    boolean deleteById(int id);

    List<Student> findByDegree(String degree);

    List<Student> findBySurname(String surname);

    Student changeCourse(int studentId, int courseId);

    void deleteExam(int studentId, int examId);

    Exam updateExam(int studentId, int examId, Exam updated);

    List<Student> findStudentsWithAverageGradeAbove(double threshold);
}
