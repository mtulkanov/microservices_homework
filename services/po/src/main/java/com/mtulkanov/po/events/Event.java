package com.mtulkanov.po.events;

import lombok.Data;

@Data
public class Event {
    public static final String ORDER_CREATED = "ORDER_CREATED";

    private final String type;
    private final String orderId;
}
