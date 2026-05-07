package com.academy.esercizio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academy.esercizio.DAO.StudentDAO;
import com.academy.esercizio.entity.Student;

import jakarta.transaction.Transactional;

// I service siano quelli che espongono i metodi per la logica di business, e che chiamano i DAO per accedere ai dati
// in questo modo se un giorno volessi cambiare il modo in cui accedo ai dati (ad esempio passando da entity manager a spring data jpa) non dovrei cambiare nulla nei service, ma solo nei DAO

// la best practice vuole che i @Transactional siano sui service e NON nei DAO, perchè i service sono quelli che espongono i metodi per la logica di business, e che chiamano i DAO per accedere ai dati
// è più logico che siano i service a gestire le transazioni, e non i DAO che sono solo dei "data access object" e non dovrebbero preoccuparsi di logica di business o di transazioni
@Service
public class StudentServiceImpl implements StudentService {
    
    // ho usato field injection per semplicità
    @Autowired
    private StudentDAO studentDAO;

    @Override
    public List<Student> findAll() {
        return studentDAO.findAll();
    }

    @Override
    public Optional<Student> findById(int id) {
        return Optional.ofNullable(studentDAO.findById(id));
    }

    @Transactional
    @Override
    public Student save(Student student) {
        return studentDAO.save(student);
    }

    @Transactional
    @Override
    public Optional<Student> update(int id, Student student) {
        // se esiste o no glielo chiedo al DAO, se esiste aggiorno e restituisco, altrimenti restituisco empty
        return Optional.ofNullable(studentDAO.findById(id))
                .map(existing -> {
                    student.setId(id);
                    return studentDAO.update(student);
                });
    }

    @Transactional
    @Override
    public boolean deleteById(int id) {
        if (studentDAO.findById(id) == null) {
            return false;
        }
        studentDAO.deleteById(id);
        return true;
    }

    @Override
    public List<Student> findByDegree(String degree) {
        return studentDAO.findByDegree(degree) == null || studentDAO.findByDegree(degree).isEmpty()
        ? List.of()
        : studentDAO.findByDegree(degree);
    }

    @Override
    public List<Student> findBySurname(String surname) {
        return studentDAO.findBySurname(surname) == null || studentDAO.findBySurname(surname).isEmpty()
        ? List.of()
        : studentDAO.findBySurname(surname);
    }
}
