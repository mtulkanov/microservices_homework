package com.mtulkanov.po.order

import com.mtulkanov.eurekaserver.pc.catalog.ProductSpecification
import com.mtulkanov.po.clients.ProductSpecificationRepository
import com.mtulkanov.po.exceptions.OrderNotFoundException
import com.mtulkanov.po.kafka.Event
import com.mtulkanov.po.kafka.KafkaGateway
import org.springframework.util.concurrent.FailureCallback
import org.springframework.util.concurrent.SuccessCallback
import spock.lang.Specification

class ProductOrderServiceImplTest extends Specification {

    private static final String ORDER_ID = "ORDER_ID"
    private static final String SPECIFICATION_ID = "SPECIFICATION_ID"

    private ProductOrder order
    private KafkaGateway kafkaService
    private ProductSpecificationRepository specificationRepository
    private ProductOrderRepository orderRepository
    private ProductOrderService orderService

    def setup() {
        def specification = new ProductSpecification()
        specificationRepository = Stub(ProductSpecificationRepository) {
            existsById(SPECIFICATION_ID) >> specification
        }

        order = new ProductOrder(
                ORDER_ID,
                SPECIFICATION_ID,
                1L,
                ProductOrder.SUSPENDED
        )

        orderRepository = Stub(ProductOrderRepository) {
            save(_ as ProductOrder) >> order
            findById(_ as String) >> Optional.of(order)
        }

        kafkaService = Mock()

        orderService = new ProductOrderServiceImpl(
                orderRepository,
                specificationRepository,
                kafkaService
        )
    }

    def 'should create suspended order'() {
        when:
        ProductOrder orderReturned = orderService.orderProductBySpecificationId(SPECIFICATION_ID)

        then:
        orderReturned.getStatus() == ProductOrder.SUSPENDED
    }

    def 'should fire event on order creation'() {
        when:
        orderService.orderProductBySpecificationId(SPECIFICATION_ID)

        then:
        1 * kafkaService.fire(
                _ as Event,
                _ as SuccessCallback,
                _ as FailureCallback
        )
    }

    def 'should reject order'() {
        when:
        ProductOrder rejectedOrder = orderService.rejectOrder(ORDER_ID)

        then:
        ProductOrder.REJECTED == rejectedOrder.getStatus()
    }

    def 'should throw correct exception on rejection if order was not found'() {
        given:
        String errorMessage = "Could not find order " + ORDER_ID
        def exception = new OrderNotFoundException(errorMessage)
        orderRepository = Stub(ProductOrderRepository) {
            orderRepository.findById(_ as String) >> { throw exception }
        }
        orderService = new ProductOrderServiceImpl(
                orderRepository,
                specificationRepository,
                kafkaService
        )

        when:
        orderService.rejectOrder(ORDER_ID)

        then:
        thrown(OrderNotFoundException)
    }
}
