package com.mindfire.backend.config;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

@Configuration
public class SwaggerConfig {
    /**
     * Configures the OpenAPI documentation for managing authentication and user operations.
     */
    @Bean
    OpenAPI myCustomConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service")
                        .description("API Documentation for managing authentication and user operations")
                        .version("1.0.0")
                        .termsOfService("http://swagger.io/terms/")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Support")
                                .url("https://www.mindfiresolutions.com/")
                                .email("lokanathamlatesh@gmail.com"))
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0")))


                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization"))
                )


                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
