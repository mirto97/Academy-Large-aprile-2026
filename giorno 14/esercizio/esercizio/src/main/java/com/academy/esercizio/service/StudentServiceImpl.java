package com.academy.esercizio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.academy.esercizio.entity.Student;
import com.academy.esercizio.repository.StudentRepository;

import jakarta.transaction.Transactional;

// I service siano quelli che espongono i metodi per la logica di business, e che chiamano i DAO per accedere ai dati
// in questo modo se un giorno volessi cambiare il modo in cui accedo ai dati (ad esempio passando da entity manager a spring data jpa) non dovrei cambiare nulla nei service, ma solo nei DAO

// la best practice vuole che i @Transactional siano sui service e NON nei DAO, perchè i service sono quelli che espongono i metodi per la logica di business, e che chiamano i DAO per accedere ai dati
// è più logico che siano i service a gestire le transazioni, e non i DAO che sono solo dei "data access object" e non dovrebbero preoccuparsi di logica di business o di transazioni
@Service
public class StudentServiceImpl implements StudentService {
    
    // ho usato field injection per semplicità
    @Autowired
    private StudentRepository repo;

    @Override
    public Optional<Student> findById(int id) {
        return repo.findById(id);
    }

    @Transactional
    @Override
    public Student save(Student student) {
        return repo.save(student);
    }

    @Transactional
    @Override
    public Optional<Student> update(int id, Student student) {
        // se esiste o no glielo chiedo al DAO, se esiste aggiorno e restituisco, altrimenti restituisco empty
        return Optional.ofNullable(repo.findById(id))
                .map(existing -> {
                    student.setId(id);
                    return repo.save(student);
                });
    }

    @Transactional
    @Override
    public boolean deleteById(int id) {
        if (repo.findById(id) == null) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }

    @Override
    public List<Student> findByDegree(String degree) {
        return repo.findByDegree(degree) == null || repo.findByDegree(degree).isEmpty()
        ? List.of()
        : repo.findByDegree(degree);
    }

    @Override
    public List<Student> findBySurname(String surname) {
        return repo.findBySurname(surname) == null || repo.findBySurname(surname).isEmpty()
        ? List.of()
        : repo.findBySurname(surname);
    }

    // metodo che restituisce la paginazione degli studenti, con i parametri page e size, che indicano rispettivamente il numero di pagina e il numero di elementi per pagina
    // uguale al findAll() ma con la paginazione, quindi restituisce una Page<Student> invece di una List<Student>
    @Override
    public Page<Student> findAll(Pageable pageable) {
        return repo.findAll(pageable);  
    }
}
