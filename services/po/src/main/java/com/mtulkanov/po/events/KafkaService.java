package com.mtulkanov.po.events;

import com.mtulkanov.po.order.ProductOrder;
import com.mtulkanov.po.exceptions.EventNotRaisedException;

public interface KafkaService {

    void orderCreated(ProductOrder productOrder) throws EventNotRaisedException;
}