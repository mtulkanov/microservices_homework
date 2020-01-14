package com.mtulkanov.ps

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
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
