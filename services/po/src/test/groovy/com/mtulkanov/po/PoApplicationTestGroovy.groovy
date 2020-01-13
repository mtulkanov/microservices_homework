package com.mtulkanov.po

import com.mtulkanov.po.kafka.Event
import com.mtulkanov.po.kafka.KafkaGateway
import com.mtulkanov.po.kafka.KafkaGatewayImpl
import com.mtulkanov.po.order.ProductOrder
import org.springframework.kafka.core.KafkaTemplate
import spock.lang.Specification

class PoApplicationTestGroovy extends Specification {

    private static final ORDER_ID = "ORDER_ID"
    private static final SPECIFICATION_ID = "SPECIFICATION_ID"

    def "kafka template called with correct parameters"() {
        given:
        KafkaTemplate<String, Event> kafkaTemplate = Mock(KafkaTemplate.class)
        KafkaGateway kafkaService = new KafkaGatewayImpl(kafkaTemplate)
        ProductOrder order = new ProductOrder(
                ORDER_ID,
                SPECIFICATION_ID,
                1L,
                ProductOrder.SUSPENDED
        )

        when:
        kafkaService.orderCreated(order)

        then:
        1 * kafkaTemplate.send(
                KafkaGatewayImpl.OUTPUT_EVENT_TOPIC,
                new Event(Event.ORDER_CREATED, ORDER_ID)
        )
    }
}