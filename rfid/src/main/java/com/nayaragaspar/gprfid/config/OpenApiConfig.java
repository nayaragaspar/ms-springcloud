package com.nayaragaspar.gprfid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

        @Bean
        OpenAPI customOpenAPI() {
                Components components = new Components().addSecuritySchemes("ApiKey", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("x-api-key"));

                components.addSecuritySchemes("JWT Authorization", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization"));

                return new OpenAPI()
                                .components(components)
                                .addSecurityItem(new SecurityRequirement()
                                                .addList("ApiKey"))
                                .addSecurityItem(new SecurityRequirement()
                                                .addList("JWT Authorization"))
                                .info(new Info()
                                                .title("Documentação API de Antena RFID")
                                                .version("v1")
                                                .description("Documentação API de Antena RFID")
                                                .license(new License().name("Apache 2.0")));
        }
}
