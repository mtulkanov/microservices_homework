package com.mtulkanov.eurekaserver.pc.catalog;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class ProductSpecification {

    public ProductSpecification() {
    }

    public ProductSpecification(String id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Id
    private String id;
    private String name;
    private Long price;
}
