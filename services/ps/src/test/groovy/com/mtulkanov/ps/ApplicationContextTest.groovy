package com.mtulkanov.ps

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.kafka.test.context.EmbeddedKafka
import spock.lang.Specification

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = [
            "listeners=PLAINTEXT://localhost:9092",
            "port=9092"
        ]
)
class ApplicationContextTest extends Specification {

    @Autowired
    ApplicationContext context

    def 'context loads'() {
        expect:
        context != null
    }
}
