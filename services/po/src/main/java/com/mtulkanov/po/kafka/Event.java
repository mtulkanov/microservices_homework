package com.mtulkanov.po.kafka;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Event {
    public static final String ORDER_CREATED = "ORDER_CREATED";

    public Event() {}

    public Event(String type, ObjectId orderId) {
        this.type = type;
        this.orderId = orderId;
    }

    public Event(ObjectId id, String type, ObjectId orderId) {
        this.id = id;
        this.type = type;
        this.orderId = orderId;
    }

    @Id
    private ObjectId id;
    private ObjectId orderId;
    private String type;
}
