package com.academy.esercizio.openAPIconfig;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student API")
                        .version("1.0.0")
                        .description("API REST per la gestione degli studenti")
                        .contact(new Contact()
                                .name("Andrea Mirto")
                                .email("andrea.mirto1997@gmail.com")));
    }
}