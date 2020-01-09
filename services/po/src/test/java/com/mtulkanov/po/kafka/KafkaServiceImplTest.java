package com.mtulkanov.po.kafka;

import com.mtulkanov.po.exceptions.EventNotRaisedException;
import com.mtulkanov.po.order.ProductOrder;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class KafkaServiceImplTest {

    private static final String ORDER_ID = "ORDER_ID";
    private static final String SPECIFICATION_ID = "SPECIFICATION_ID";

    @Test
    public void kafkaTemplateCalledWithCorrectParameters() {
        // given
        KafkaTemplate<String, Event> kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        KafkaService kafkaService = new KafkaServiceImpl(kafkaTemplate);
        ProductOrder order = new ProductOrder(
                ORDER_ID,
                SPECIFICATION_ID,
                1L,
                ProductOrder.SUSPENDED
        );

        // when
        kafkaService.orderCreated(order, null, null);

        // then
        ArgumentMatcher<Event> argumentMatcher = event ->
                event.getType().equals(Event.ORDER_CREATED)
                && event.getOrderId().equals(ORDER_ID);
        verify(kafkaTemplate).send(
                eq(KafkaServiceImpl.OUTPUT_EVENT_TOPIC),
                argThat(argumentMatcher)
        );
    }
}