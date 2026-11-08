package com.shayan.inventory.config;

import com.shayan.inventory.entity.Category;
import com.shayan.inventory.entity.Product;
import com.shayan.inventory.repository.CategoryRepository;
import com.shayan.inventory.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDatabase(CategoryRepository categoryRepo, ProductRepository productRepo) {
        return args -> {
            if (categoryRepo.count() > 0) return;

            Category electronics = categoryRepo.save(new Category("Electronics", "Electronic devices and components"));
            Category clothing = categoryRepo.save(new Category("Clothing", "Apparel and accessories"));
            Category books = categoryRepo.save(new Category("Books", "Physical and digital books"));
            Category sports = categoryRepo.save(new Category("Sports", "Sports equipment and gear"));

            productRepo.save(new Product("Wireless Mouse", "Ergonomic Bluetooth mouse with USB-C", new BigDecimal("29.99"), 150, "ELEC-001", electronics));
            productRepo.save(new Product("Mechanical Keyboard", "RGB mechanical keyboard with Cherry MX switches", new BigDecimal("89.99"), 75, "ELEC-002", electronics));
            productRepo.save(new Product("USB-C Hub", "7-in-1 USB-C docking station", new BigDecimal("49.99"), 200, "ELEC-003", electronics));
            productRepo.save(new Product("27\" Monitor", "4K IPS monitor with HDR support", new BigDecimal("349.99"), 30, "ELEC-004", electronics));
            productRepo.save(new Product("Webcam HD", "1080p webcam with built-in microphone", new BigDecimal("59.99"), 5, "ELEC-005", electronics));

            productRepo.save(new Product("Running Shoes", "Lightweight running shoes, unisex", new BigDecimal("79.99"), 120, "CLTH-001", clothing));
            productRepo.save(new Product("Winter Jacket", "Insulated waterproof winter jacket", new BigDecimal("149.99"), 45, "CLTH-002", clothing));
            productRepo.save(new Product("Baseball Cap", "Adjustable cotton baseball cap", new BigDecimal("19.99"), 300, "CLTH-003", clothing));

            productRepo.save(new Product("Clean Code", "Robert C. Martin — A handbook of agile software craftsmanship", new BigDecimal("39.99"), 60, "BOOK-001", books));
            productRepo.save(new Product("Design Patterns", "Gang of Four — Elements of reusable OO software", new BigDecimal("44.99"), 8, "BOOK-002", books));

            productRepo.save(new Product("Yoga Mat", "Non-slip exercise mat, 6mm thick", new BigDecimal("24.99"), 90, "SPRT-001", sports));
            productRepo.save(new Product("Resistance Bands Set", "5-piece resistance bands with handles", new BigDecimal("34.99"), 3, "SPRT-002", sports));
        };
    }
}
