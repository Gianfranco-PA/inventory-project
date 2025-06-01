package com.gianfranco.products.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title       = "Product Service API",
                version     = "v1.0.0",
                description = "API para gestión de productos (CRUD + stock) de Sisinv",
                contact     = @Contact(name = "Gianfranco Perez", email = "gfperez26@outlook.com")
        )
)
public class OpenApiConfig {
}
