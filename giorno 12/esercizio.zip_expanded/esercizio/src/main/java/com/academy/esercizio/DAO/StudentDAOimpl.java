package com.academy.esercizio.DAO;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.academy.esercizio.entity.Student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

// fu "repository" in jpa, ora è DAO perchè stiamo usando entity manager, non più spring data jpa
@Repository
public class StudentDAOimpl implements StudentDAO {

    // Spring non inietta un EntityManager reale, ma un proxy thread-safe. Ogni thread (ovvero ogni richiesta HTTP) riceve la propria istanza di EntityManager legata alla transazione corrente. Questo risolve il problema della concorrenza.
    // funzionerebbe anche @Autowired, ma è più corretto così per EntityManager (permette a Spring di gestire meglio il ciclo di vita dell'EntityManager)
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Student save(Student s) {
        entityManager.persist(s);
        return s;
    }

    @Override
    public Student findById(int id) {
        if (id > 0) {
            return entityManager.find(Student.class, id);
        }
        return null;
    }

    @Override
    public List<Student> findAll() {
        TypedQuery<Student> query = entityManager.createQuery(
            "SELECT s FROM Student s", Student.class
        );
        return query.getResultList();
    }

    @Override
    public void deleteById(int id) {
        Student student = findById(id);
        if (student != null) {
            entityManager.remove(student);
        }
    }

    @Override
    public Student update(Student s) {
        // "merge" salva o aggiorna l'entità a seconda dell'id
        //  se l'id è 0 o negativo, viene considerata una nuova entità e viene salvata; se l'id è positivo, viene considerata un'entità esistente e viene aggiornata
        return entityManager.merge(s);
    }

    @Override
    public List<Student> findByDegree(String degree) {
        TypedQuery<Student> query = entityManager.createQuery(
            "SELECT s FROM Student s WHERE s.degree = :degree", Student.class
        );
        query.setParameter("degree", degree);
        return query.getResultList();
    }

    @Override
    public List<Student> findBySurname(String surname) {
        TypedQuery<Student> query = entityManager.createQuery(
            "SELECT s FROM Student s WHERE s.surname = :surname", Student.class
        );
        query.setParameter("surname", surname);
        return query.getResultList();
    }
}
