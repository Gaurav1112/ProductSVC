package org.responsive.productsvc.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI productServiceOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Product Service API")
                .description("APIs for managing products and generating recommendations")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Responsive Systems")
                    .email("support@responsive.io")
                    .url("https://responsive.io"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://springdoc.org")))
            .externalDocs(new ExternalDocumentation()
                .description("GitHub Repository")
                .url("https://github.com/responsive/productsvc"));
    }
}
