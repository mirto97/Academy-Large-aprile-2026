package com.academy.esercizio;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.academy.esercizio.entity.Role;
import com.academy.esercizio.entity.Student;
import com.academy.esercizio.entity.User;
import com.academy.esercizio.repository.RoleRepository;
import com.academy.esercizio.repository.StudentRepository;
import com.academy.esercizio.repository.UserRepository;
import com.academy.esercizio.security.PasswordHasher;


@Configuration
// aggiunto configuration per utilizzare la command line runner, come da richiesta dell'esercizio, per popolare il database all'avvio dell'applicazione
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EsercizioApplication.class);
	}

    // command line runner per popolare il database all'avvio dell'applicazione
    @Bean
    public CommandLineRunner initData(StudentRepository studentDAO, UserRepository userDAO, RoleRepository roleDAO, PasswordHasher encoder) {

        return runner -> {
            
            // salvo 3 studenti

            Student s1 = new Student();
            s1.setName("Mario");        
            s1.setSurname("Rossi");
            s1.setEmail("mario.rossi@example.com");
            s1.setDob(LocalDate.of(1990, 1, 1));
            s1.setDegree("Ingegneria Informatica");
            
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

            // Crea nel database due tabelle per la sicurezza Spring: users (username VARCHAR, password
            // VARCHAR, enabled BOOLEAN) e authorities (username VARCHAR, authority VARCHAR).
            // Popolale con almeno 2 utenti: admin/ROLE_ADMIN e user/ROLE_USER
        
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.hash("123"));
            admin.setActive(true);
            
            User user = new User();
            user.setUsername("user");
            user.setPassword(encoder.hash("123")) ;
            user.setActive(true);
        
            userDAO.save(admin);
            userDAO.save(user);
            System.out.println("Utenti salvati: " + admin.getUsername() + ", " + user.getUsername());   
            
            Role adminRole = new Role();
            adminRole.setUser_id(admin.getId());
            adminRole.setRole("ROLE_ADMIN");
        
            // do anche il ruolo di user all'admin, come visto nel video
            // è buon metodo su grandi sistemi così puoi dare solo i ruoli che vuoi senza dare tutti quelli precedenti a chi ha quelli più alti
            Role adminRole2 = new Role();
            adminRole2.setUser_id(admin.getId());
            adminRole2.setRole("ROLE_USER");
        
            Role userRole = new Role();
            userRole.setUser_id(user.getId());
            userRole.setRole("ROLE_USER");
        
            roleDAO.save(adminRole);
            roleDAO.save(adminRole2);
            roleDAO.save(userRole);
            System.out.println("Ruoli salvati: " + adminRole.getRole() + " per " + admin.getUsername() + ", " + adminRole2.getRole() + " per " + admin.getUsername() + ", " + userRole.getRole() + " per " + user.getUsername());
        };
    }
}
