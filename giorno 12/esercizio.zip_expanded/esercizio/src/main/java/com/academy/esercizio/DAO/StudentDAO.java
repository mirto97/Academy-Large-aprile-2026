package com.academy.esercizio.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.academy.esercizio.entity.Student;

@Repository
// potrei implementere JpaRepository (come facevamo una volta con spring data jpa)
// ma farebbe tutto da solo e qua invece voglio utilizzare EntityManager
public interface StudentDAO {
    
    Student save(Student student);
    Student findById(int id);
    List<Student> findAll();
    void deleteById(int id);
    Student update(Student student);

    // se fosse spring data jpa, basterebbe il nome del metodo, ma qua devo scrivere la query a mano
    // avrei potuto scrivere le query jpql in questo modo, ma siccome non stiamo usando spring data jpa, le scrivo direttamente nell'impl

    // @Query("SELECT s FROM Student s WHERE s.degree = :degree")
    List<Student> findByDegree(String degree);
    
    // @Query("SELECT s FROM Student s WHERE s.surname = :surname")
    List<Student> findBySurname(String surname);

}
