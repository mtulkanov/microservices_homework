package com.mtulkanov.po.kafka;

import com.mtulkanov.po.order.ProductOrder;
import com.mtulkanov.po.exceptions.EventNotRaisedException;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

public interface KafkaService {

    void orderCreated(
            ProductOrder productOrder,
            SuccessCallback<SendResult<String, Event>> successCallback,
            FailureCallback failureCallback
    ) throws EventNotRaisedException;
}