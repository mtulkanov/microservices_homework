package com.mtulkanov.po.order

import com.mtulkanov.eurekaserver.pc.catalog.ProductSpecification
import com.mtulkanov.po.clients.ProductSpecificationRepository
import com.mtulkanov.po.exceptions.OrderNotFoundException
import com.mtulkanov.po.kafka.Event
import com.mtulkanov.po.kafka.EventRepository

import org.bson.types.ObjectId
import spock.lang.Specification

class ProductOrderServiceImplTest extends Specification {

    private static final String ORDER_ID = 'ORDER_ID'
    private static final String SPECIFICATION_ID = 'SPECIFICATION_ID'

    private ProductOrder order
    private ProductSpecificationRepository specificationRepository
    private ProductOrderRepository orderRepository
    private ProductOrderService orderService
    private EventRepository eventRepository

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

        def event = new Event(Event.ORDER_CREATED, ORDER_ID)

        eventRepository = Mock(EventRepository) {
            save(_ as Event) >> event
        }

        orderService = new ProductOrderServiceImpl(
                orderRepository,
                specificationRepository,
                eventRepository
        )
    }

    def 'should create suspended order'() {
        when:
        ProductOrder orderReturned = orderService.orderProductBySpecificationId(SPECIFICATION_ID)

        then:
        orderReturned.getStatus() == ProductOrder.SUSPENDED
    }

    def 'should create event on order creation'() {
        when:
        orderService.orderProductBySpecificationId(SPECIFICATION_ID)

        then:
        1 * eventRepository.save(_ as Event)
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
                eventRepository
        )

        when:
        orderService.rejectOrder(ORDER_ID)

        then:
        thrown(OrderNotFoundException)
    }
}
