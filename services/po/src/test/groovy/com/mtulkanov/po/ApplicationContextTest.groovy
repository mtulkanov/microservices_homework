package com.mtulkanov.po

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

@SpringBootTest
class ApplicationContextTest extends Specification {

    ApplicationContext context

    def 'context loads'() {
        expect:
        context != null
    }
}
