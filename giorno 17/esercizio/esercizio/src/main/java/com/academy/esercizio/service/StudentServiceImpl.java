package com.academy.esercizio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.academy.esercizio.entity.Course;
import com.academy.esercizio.entity.Exam;
import com.academy.esercizio.entity.Student;
import com.academy.esercizio.exception.StudentNotFoundException;
import com.academy.esercizio.repository.CourseRepository;
import com.academy.esercizio.repository.StudentRepository;

import jakarta.transaction.Transactional;

// I service siano quelli che espongono i metodi per la logica di business, e che chiamano i DAO per accedere ai dati
// in questo modo se un giorno volessi cambiare il modo in cui accedo ai dati (ad esempio passando da entity manager a spring data jpa) non dovrei cambiare nulla nei service, ma solo nei DAO

// la best practice vuole che i @Transactional siano sui service e NON nei DAO, perchè i service sono quelli che espongono i metodi per la logica di business, e che chiamano i DAO per accedere ai dati
// è più logico che siano i service a gestire le transazioni, e non i DAO che sono solo dei "data access object" e non dovrebbero preoccuparsi di logica di business o di transazioni
@Service
public class StudentServiceImpl implements StudentService {
    
    private final StudentRepository repo;
    private final CourseRepository cRepo;

    //@Autowired
    public StudentServiceImpl(StudentRepository repo, CourseRepository cRepo) {
        this.repo = repo;
        this.cRepo = cRepo;
    }

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

    @Transactional
    @Override
    public Student changeCourse(int studentId, int courseId) {
        Student student = repo.findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException(studentId));

        Course newCourse = cRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Corso non trovato: " + courseId));

        student.setCourse(newCourse);   // aggiorna la FK
        return repo.save(student);
    }

    @Transactional
    @Override
    public void deleteExam(int studentId, int examId) {
        Student student = repo.findById(studentId).orElseThrow(() -> new StudentNotFoundException(studentId));

        // cerca l'esame nella lista dello studente, se lo trova lo rimuove
        boolean removed = student.getExams().removeIf(e -> e.getId() == examId);
        // se non lo trova lancia eccezione
        if (!removed) {
            throw new RuntimeException("Esame id: " + examId + " non trovato per studente id: " + studentId);
        }

        repo.save(student); // orphanRemoval triggera il delete sull'esame
    }

    @Transactional
    @Override
    public Exam updateExam(int studentId, int examId, Exam updated) {
        Student student = repo.findById(studentId).orElseThrow(() -> new StudentNotFoundException(studentId));

        Exam exam = student.getExams().stream()
                .filter(e -> e.getId() == examId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Esame id: " + examId + " non trovato per studente id: " + studentId));

        // aggiorno i campi dell'esame trovato in lista (stesso oggetto gestito da hibernate)
        exam.setGrade(updated.getGrade());
        exam.setHonors(updated.isHonors());
        exam.setSubject(updated.getSubject());
        exam.setExamDate(updated.getExamDate());

        repo.save(student);
        return exam;
    }

    @Override
    public List<Student> findStudentsWithAverageGradeAbove(double threshold) {
        return repo.findStudentsWithAverageGradeAbove(threshold);
    }
}
