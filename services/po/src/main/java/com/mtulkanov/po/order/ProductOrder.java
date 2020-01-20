package com.mtulkanov.po.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Nikita_Puzankov
 */
@Document
@Data
@AllArgsConstructor
public class ProductOrder {
    public static final String SUSPENDED = "SUSPENDED";
    public static final String REJECTED = "REJECTED";
    public static final String ACCEPTED = "ACCEPTED";

    @Id
    private ObjectId id;
    private String specificationId;
    private Long quantity;
    private String status;
}
