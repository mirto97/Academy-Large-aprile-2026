package com.academy.esercizio;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.academy.esercizio.DAO.StudentDAO;
import com.academy.esercizio.entity.Student;


@Configuration
// aggiunto configuration per utilizzare la command line runner, come da richiesta dell'esercizio, per popolare il database all'avvio dell'applicazione
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EsercizioApplication.class);
	}

    // command line runner per popolare il database all'avvio dell'applicazione
    @Bean
    public CommandLineRunner initData(StudentDAO studentDAO) {

        return runner -> {

            // salvo 3 studenti

            Student s1 = new Student();
            s1.setName("Mario");        
            s1.setSurname("Rossi");
            s1.setEmail("mario.rossi@example.com");
            s1.setDob(LocalDate.of(1990, 1, 1));
            s1.setDegree("Informatica");

            studentDAO.save(s1);
            System.out.println("Studente salvato: " + s1.getName() + " " + s1.getSurname());

            Student s2 = new Student();
            s2.setName("Luigi");        
            s2.setSurname("Verdi");
            s2.setEmail("luigi.verdi@example.com");
            s2.setDob(LocalDate.of(1992, 5, 15));
            s2.setDegree("Matematica");

            studentDAO.save(s2);
            System.out.println("Studente salvato: " + s2.getName() + " " + s2.getSurname());

            Student s3 = new Student();
            s3.setName("Giulia");        
            s3.setSurname("Bianchi");   
            s3.setEmail("giulia.bianchi@example.com");
            s3.setDob(LocalDate.of(1991, 8, 20));
            s3.setDegree("Fisica");

            studentDAO.save(s3);
            System.out.println("Studente salvato: " + s3.getName() + " " + s3.getSurname());

            // li leggo tutti e stampo a console

            System.out.println("Tutti gli studenti:");
            studentDAO.findAll().forEach(student -> {
                System.out.println(student.getId() + ": " + student.getName() + " " + student.getSurname());
            });

            // aggiorno il corso di laurea del 1° studente e lo stampo a console

            s1.setDegree("Ingegneria Informatica");
            studentDAO.update(s1);
            System.out.println("Studente aggiornato: " + s1.getName() + " " + s1.getSurname() + " - Nuovo corso di laurea: " + s1.getDegree());

            // cancello il 3° studente e stampo la conferma a console

            studentDAO.deleteById(s3.getId());
            System.out.println("Studente cancellato: " + s3.getName() + " " + s3.getSurname() + " (ID: " + s3.getId() + ")");

            // faccio una query per corso di laurea e stampo a console i risultati

            String degreeToQuery = "Ingegneria Informatica";
            System.out.println("Studenti iscritti al corso di laurea '" + degreeToQuery + "':");
            studentDAO.findByDegree(degreeToQuery).forEach(student -> {
                System.out.println(student.getId() + ": " + student.getName() + " " + student.getSurname());
            });
        };
    }

}
