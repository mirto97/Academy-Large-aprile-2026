package com.academy.esercizio.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.Customizer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    // prima creavamo una classe per ogni bean
    // adesso usiamo i metodi annotati con @Bean per definire i bean direttamente nella classe di configurazione
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // post put e delete li permetto solo all'admin, mentre get è permesso a tutti
                .requestMatchers(HttpMethod.GET, "/api/students").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/students/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/students/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/students/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/students/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/auth/register").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
            
            return http.build();
    }
        
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        // questa classe è un esempio di come si poteva fare la configurazione prima di Spring Security 5.7
        // estendeva WebSecurityConfigurerAdapter e overrideava i metodi configure(HttpSecurity http) e configure(AuthenticationManagerBuilder auth)
        // non so che sta dicendo qui sopra, sto commento è uscito dall'ai, verificheremo...

        JdbcUserDetailsManager theUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // sto creando una query personalizzata per recuperare gli utenti e i ruoli dal database
        theUserDetailsManager.setUsersByUsernameQuery("select username, password, active from user where username = ?");
        theUserDetailsManager.setAuthoritiesByUsernameQuery("select u.username, r.role from user u join role r on u.id = r.user_id where u.username = ?");

        return theUserDetailsManager;
    }

    // questo bean è necessario per hashare le password in modo sicuro con BCrypt, altrimenti spring security non accetterebbe le password in chiaro e lancerebbe un'eccezione
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /* vecchio metodo con ruoli hardcoded in memoria, adesso useremo un database per gestire gli utenti e i ruoli
    @Bean
    public UserDetailsService userDetailsService() {
        // {noop} indica che la password non è codificata, ma in chiaro
        // senza noop spring security lancerebbe un'eccezione perché si aspetta una password codificata
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}123")
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password("{noop}123")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
    */

}
