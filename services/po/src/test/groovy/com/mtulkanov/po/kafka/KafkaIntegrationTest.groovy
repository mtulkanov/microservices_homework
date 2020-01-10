package com.mtulkanov.po.kafka

import com.mtulkanov.po.order.ProductOrder
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
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import spock.lang.Specification

import static org.springframework.kafka.test.utils.KafkaTestUtils.getSingleRecord

@EmbeddedKafka(
        partitions = 1,
        topics = [KafkaServiceImpl.OUTPUT_EVENT_TOPIC]
)
@SpringBootTest(
        properties = ['spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}'],
        classes = [
                KafkaServiceImpl,
                KafkaAutoConfiguration,
        ]
)
@Slf4j
class KafkaIntegrationTest extends Specification {

    private static final String ORDER_ID = "ORDER_ID";
    private static final String SPECIFICATION_ID = "SPECIFICATION_ID";

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private EmbeddedKafkaBroker kafkaBroker;

    @Autowired
    private KafkaTemplate<String, Event> kafkaTemplate;

    def 'order created event should be sent to kafka'() {
        given:
        ProductOrder order = new ProductOrder(
                ORDER_ID,
                SPECIFICATION_ID,
                1L,
                ProductOrder.SUSPENDED
        );
        Consumer<String, Event> consumer = this.<String, Event>buildConsumer(
                StringDeserializer,
                JsonDeserializer
        )
        kafkaBroker.consumeFromAnEmbeddedTopic(consumer, KafkaServiceImpl.OUTPUT_EVENT_TOPIC);

        when:
        kafkaService.orderCreated(order);

        then:
        ConsumerRecord<String, Event> record = getSingleRecord(
                consumer,
                KafkaServiceImpl.OUTPUT_EVENT_TOPIC,
                500
        );
        Event event = record.value();

        ORDER_ID == event.getOrderId()
        Event.ORDER_CREATED == event.getType()
    }

    private <K, V> Consumer<K, V> buildConsumer(
            Class<? extends Deserializer> keyDeserializer,
            Class<? extends Deserializer> valueDeserializer
    ) {
        Map<String, Object> props = KafkaTestUtils.consumerProps(
                "testOrderCreatedEventWasSentToKafka",
                "true",
                kafkaBroker
        );
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);

        def factory = new DefaultKafkaConsumerFactory<K, V>(props);
        return factory.createConsumer();
    }
}
