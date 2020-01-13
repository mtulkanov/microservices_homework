package com.mtulkanov.po.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.kafka.enable", havingValue = "true", matchIfMissing = true)
public class KafkaConfig {

    public static final String OUTPUT_EVENT_TOPIC = "OUTPUT_EVENT";

    @Bean
    public NewTopic outputEventTopic() {
        return new NewTopic(OUTPUT_EVENT_TOPIC, 1, (short) 1);
    }
}
