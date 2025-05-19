package ru.aston.matveenko_ym.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "User Service API",
                description = "User management service",
                version = "2.0.1",
                contact = @Contact(
                        name = "Matveenko Yury",
                        email = "korn1984@narod.ru",
                        url = "https://github.com/KoRn1984"
                )
        )
)
public class OpenAPIConfig {
}