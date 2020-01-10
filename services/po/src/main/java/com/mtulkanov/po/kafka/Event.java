package com.mtulkanov.po.kafka;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
    public static final String ORDER_CREATED = "ORDER_CREATED";

    public Event() {}

    public Event(String type, String orderId) {
        this.type = type;
        this.orderId = orderId;
    }

    private String type;
    private String orderId;
}
