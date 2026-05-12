package com.academy.esercizio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.academy.esercizio.entity.Course;
import com.academy.esercizio.entity.Exam;
import com.academy.esercizio.entity.Student;
import com.academy.esercizio.repository.RoleRepository;
import com.academy.esercizio.repository.StudentRepository;
import com.academy.esercizio.repository.UserRepository;


@Configuration
// aggiunto configuration per utilizzare la command line runner, come da richiesta dell'esercizio, per popolare il database all'avvio dell'applicazione
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EsercizioApplication.class);
	}

    // command line runner per popolare il database all'avvio dell'applicazione
    @Bean
    public CommandLineRunner initData(StudentRepository studentDAO, UserRepository userDAO, RoleRepository roleDAO) {

        return runner -> {
            
            // salvo 3 studenti

            Student s1 = new Student();
            s1.setName("Mario");        
            s1.setSurname("Rossi");
            s1.setEmail("mario.rossi@example.com");
            s1.setDob(LocalDate.of(1990, 1, 1));
            s1.setDegree("Ingegneria Informatica");

            // aggiungo esami
            s1.getExams().addAll(List.of(
                new Exam(0, "Matematica", 28, LocalDate.of(2024, 2, 10), false),
                new Exam(0, "Fisica", 30, LocalDate.of(2024, 3, 5),  true),
                new Exam(0, "Analisi", 26, LocalDate.of(2024, 5, 20), false)
            ));

            Course c1 = new Course();
            c1.setName("Ingegneria Informatica");
            c1.setDepartment("gigitech");
            c1.setYears(4);

            // assegno studente e corso
            // dovrebbe bastare così perchè ho fatto il metodo che segna entrambi
            c1.add(s1);
            
            // salva entrambi per il cascade
            studentDAO.save(s1);
            System.out.println("Studente salvato: " + s1.getName() + " " + s1.getSurname());

            Student s2 = new Student();
            s2.setName("Luigi");        
            s2.setSurname("Verdi");
            s2.setEmail("luigi.verdi@example.com");
            s2.setDob(LocalDate.of(1992, 5, 15));
            s2.setDegree("Matematica");

            s2.getExams().addAll(List.of(
                new Exam(0, "Storia", 27, LocalDate.of(2024, 1, 22), false),
                new Exam(0, "Inglese", 30, LocalDate.of(2024, 4, 8),  true)
            ));

            Course c2 = new Course();
            c2.setName("Matematica");
            c2.setDepartment("g+g");
            c2.setYears(4);

            c2.add(s2);

            studentDAO.save(s2);
            System.out.println("Studente salvato: " + s2.getName() + " " + s2.getSurname());

            Student s3 = new Student();
            s3.setName("Giulia");        
            s3.setSurname("Bianchi");   
            s3.setEmail("giulia.bianchi@example.com");
            s3.setDob(LocalDate.of(1991, 8, 20));
            s3.setDegree("Fisica");

            Course c3 = new Course();
            c3.setName("Fisica");
            c3.setDepartment("gigi=mc^2");
            c3.setYears(4);

            c3.add(s3);

            studentDAO.save(s3);
            System.out.println("Studente salvato: " + s3.getName() + " " + s3.getSurname());
            
            // li leggo tutti e stampo a console
            
            System.out.println("Tutti gli studenti:");
            studentDAO.findAll().forEach(student -> {
                System.out.println(student.getId() + ": " + student.getName() + " " + student.getSurname());
            });

        
        };
    }
}
