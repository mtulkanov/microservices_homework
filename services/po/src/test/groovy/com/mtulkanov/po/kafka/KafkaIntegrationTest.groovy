package com.mtulkanov.po.kafka

import lombok.extern.slf4j.Slf4j
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import spock.lang.Specification

import static org.springframework.kafka.test.utils.KafkaTestUtils.getSingleRecord

@EmbeddedKafka(
        partitions = 1,
        topics = [KafkaGatewayImpl.OUTPUT_EVENT_TOPIC]
)
@SpringBootTest(
        properties = ['spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}'],
        classes = [
                KafkaGatewayImpl,
                KafkaAutoConfiguration,
        ]
)
@Slf4j
class KafkaIntegrationTest extends Specification {

    private static final String ORDER_ID = "ORDER_ID"

    @Autowired
    private KafkaGateway kafkaGateway

    @Autowired
    private EmbeddedKafkaBroker kafkaBroker

    def 'order created event should be sent to kafka'() {
        given:
        def event = new Event(Event.ORDER_CREATED, ORDER_ID)
        Consumer<String, Event> consumer = this.<String, Event> buildConsumer(
                StringDeserializer,
                JsonDeserializer
        )
        kafkaBroker.consumeFromAnEmbeddedTopic(consumer, KafkaGatewayImpl.OUTPUT_EVENT_TOPIC)

        when:
        kafkaGateway.fire(event, null, null)

        then:
        ConsumerRecord<String, Event> record = getSingleRecord(
                consumer,
                KafkaGatewayImpl.OUTPUT_EVENT_TOPIC,
                500
        )
        Event comsumedEvent = record.value()

        ORDER_ID == comsumedEvent.getOrderId()
        Event.ORDER_CREATED == comsumedEvent.getType()
    }

    private <K, V> Consumer<K, V> buildConsumer(
            Class<? extends Deserializer> keyDeserializer,
            Class<? extends Deserializer> valueDeserializer
    ) {
        Map<String, Object> props = KafkaTestUtils.consumerProps(
                "testOrderCreatedEventWasSentToKafka",
                "true",
                kafkaBroker
        )
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*")
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer)

        def factory = new DefaultKafkaConsumerFactory<K, V>(props)
        return factory.createConsumer()
    }
}
