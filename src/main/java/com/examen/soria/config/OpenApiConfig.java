package com.examen.soria.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BanQuito - Sistema de Ventanillas API")
                        .description("API para el manejo de efectivo en ventanillas del banco BanQuito. " +
                                "Permite a los cajeros iniciar turnos, procesar transacciones y cerrar turnos.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo BanQuito")
                                .email("desarrollo@banquito.com")
                                .url("https://banquito.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
} 