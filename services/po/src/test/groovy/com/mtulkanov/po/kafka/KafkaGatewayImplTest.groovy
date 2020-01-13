package com.mtulkanov.po.kafka

import org.springframework.kafka.core.KafkaOperations
import spock.lang.Specification

class KafkaGatewayImplTest extends Specification {

    private static final String ORDER_ID = "ORDER_ID"

    def 'should call kafka template with correct parameters'() {
        given:
        KafkaOperations<String, Event> kafkaTemplate = Mock()
        KafkaGateway kafkaService = new KafkaGatewayImpl(kafkaTemplate)
        Event event = new Event(Event.ORDER_CREATED, ORDER_ID)

        when:
        kafkaService.fire(event)

        then:
        1 * kafkaTemplate.send(KafkaGatewayImpl.OUTPUT_EVENT_TOPIC, event)
    }
}
