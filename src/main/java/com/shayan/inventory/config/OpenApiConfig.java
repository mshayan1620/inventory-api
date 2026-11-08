package com.shayan.inventory.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventoryApiConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Management API")
                        .description("RESTful API for managing product inventory with categories, stock tracking, and search capabilities.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Muhammad Shayan")
                                .url("https://github.com/mshayan1620")));
    }
}
