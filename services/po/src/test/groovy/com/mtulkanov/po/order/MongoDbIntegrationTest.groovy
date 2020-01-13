package com.mtulkanov.po.order

import com.mtulkanov.po.order.ProductOrder
import com.mtulkanov.po.order.ProductOrderRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
@Slf4j
class MongoDbIntegrationTest extends Specification {

    private static final String ORDER_ID = 'ORDER_ID'
    private static final String SPECIFICATION_ID = 'SPECIFICATION_ID'

    @Autowired
    private ProductOrderRepository repository

    def 'should save order to database'() {
        given:
        def order = new ProductOrder(
                ORDER_ID,
                SPECIFICATION_ID,
                1L,
                ProductOrder.SUSPENDED
        )

        when:
        def savedOrder = repository.save(order)
        log.info("Saved order: ${savedOrder}")

        then:
        savedOrder.getId() != null
    }
}
