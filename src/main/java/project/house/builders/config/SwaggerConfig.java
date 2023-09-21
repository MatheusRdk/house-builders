package project.house.builders.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .in(SecurityScheme.In.HEADER)
                                .bearerFormat("JWT")))
                .info(new Info()
                        .title("House project management API")
                        .description("CRUD operations for house, architects and engineers")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Matheus Rudek")
                                .email("matheus_rudek@hotmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Github project")
                        .url("https://github.com/MatheusRdk/house-builders"));
    }
}