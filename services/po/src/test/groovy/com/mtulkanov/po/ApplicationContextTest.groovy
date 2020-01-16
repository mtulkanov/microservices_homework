package com.mtulkanov.po

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import spock.lang.Specification

@SpringBootTest
class ApplicationContextTest extends Specification {

    @Autowired
    ApplicationContext context

    def 'context loads'() {
        expect:
        context != null
    }
}
