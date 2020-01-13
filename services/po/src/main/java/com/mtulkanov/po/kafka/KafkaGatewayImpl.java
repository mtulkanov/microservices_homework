package com.mtulkanov.po.kafka;

import com.mtulkanov.po.order.ProductOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

import static com.mtulkanov.po.kafka.KafkaConfig.OUTPUT_EVENT_TOPIC;

@Service
@RequiredArgsConstructor
public class KafkaGatewayImpl implements KafkaGateway {

    private final KafkaOperations<String, Event> kafkaTemplate;

    @Override
    public void orderCreated(
            ProductOrder productOrder,
            SuccessCallback<SendResult<String, Event>> successCallback,
            FailureCallback failureCallback
    ) {
        Event event = new Event(Event.ORDER_CREATED, productOrder.getId());
        kafkaTemplate.send(OUTPUT_EVENT_TOPIC, event);
    }

    @Override
    public void orderCreated(ProductOrder productOrder) {
        orderCreated(productOrder, null, null);
    }

    @Override
    public void fire(
            Event event,
            SuccessCallback<SendResult<String, Event>> successCallback,
            FailureCallback failureCallback
    ) {
        ListenableFuture<SendResult<String, Event>> future =
                kafkaTemplate.send(OUTPUT_EVENT_TOPIC, event);
        future.addCallback(successCallback, failureCallback);
    }

    @Override
    public void fire(Event event) {
        kafkaTemplate.send(OUTPUT_EVENT_TOPIC, event);
    }
}
