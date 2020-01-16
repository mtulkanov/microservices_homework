package com.mtulkanov.ps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class ProductShipmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductShipmentApplication.class, args);
    }
}
