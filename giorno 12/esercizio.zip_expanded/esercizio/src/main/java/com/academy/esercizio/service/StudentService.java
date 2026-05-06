package com.academy.esercizio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academy.esercizio.DAO.StudentDAO;
import com.academy.esercizio.entity.Student;

@Service
public class StudentService{
    
    // ho usato field injection per semplicità
    @Autowired
    private StudentDAO studentDAO;

    public List<Student> findAll() {
        return studentDAO.findAll();
    }

    public Optional<Student> findById(int id) {
        return Optional.ofNullable(studentDAO.findById(id));
    }

    public Student save(Student student) {
        return studentDAO.save(student);
    }

    public Optional<Student> update(int id, Student student) {
        // se esiste o no glielo chiedo al DAO, se esiste aggiorno e restituisco, altrimenti restituisco empty
        return Optional.ofNullable(studentDAO.findById(id))
                .map(existing -> {
                    student.setId(id);
                    return studentDAO.update(student);
                });
    }

    public boolean deleteById(int id) {
        if (studentDAO.findById(id) == null) {
            return false;
        }
        studentDAO.deleteById(id);
        return true;
    }

    public List<Student> findByDegree(String degree) {
        return studentDAO.findByDegree(degree) == null || studentDAO.findByDegree(degree).isEmpty()
        ? List.of()
        : studentDAO.findByDegree(degree);
    }

    public List<Student> findBySurname(String surname) {
        return studentDAO.findBySurname(surname) == null || studentDAO.findBySurname(surname).isEmpty()
        ? List.of()
        : studentDAO.findBySurname(surname);
    }
}
