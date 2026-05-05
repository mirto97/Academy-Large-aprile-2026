package com.academy.esercizio.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.academy.esercizio.entity.Student;

@Repository
// potrei implementere JpaRepository (come facevamo una volta), ma farebbe tutto da solo e qua invece voglio utilizzare l'EntityManager, quindi creo un'interfaccia normale e poi la implemento con StudentDAOimpl
public interface StudentDAO {
    
    Student save(Student student);
    Student findById(int id);
    List<Student> findAll();
    void deleteById(int id);
    Student update(Student student);
    List<Student> findByDegree(String degree);

}
