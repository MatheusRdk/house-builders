package project.house.builders.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
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