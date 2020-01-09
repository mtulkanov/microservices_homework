package com.mtulkanov.po.kafka;

import com.mtulkanov.po.order.ProductOrder;
import com.mtulkanov.po.exceptions.EventNotRaisedException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    public static final String OUTPUT_EVENT_TOPIC = "OUTPUT_EVENT";

    private final KafkaTemplate<String, Event> kafkaTemplate;

    @Override
    public void orderCreated(
            ProductOrder productOrder,
            SuccessCallback<SendResult<String, Event>> successCallback,
            FailureCallback failureCallback
    ) throws EventNotRaisedException {
        Event event = new Event(Event.ORDER_CREATED, productOrder.getId());
        kafkaTemplate.send(OUTPUT_EVENT_TOPIC, event);
    }
}
