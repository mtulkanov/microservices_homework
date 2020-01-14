package com.mtulkanov.ps.kafka;

import com.mtulkanov.po.kafka.Event;

public interface EventListener {

    void listenOrders(Event event);
}
