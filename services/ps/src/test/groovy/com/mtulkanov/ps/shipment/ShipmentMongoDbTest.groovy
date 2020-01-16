package com.mtulkanov.ps.shipment


import com.mtulkanov.ps.kafka.EventListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ShipmentMongoDbTest extends Specification {

    private static final String ORDER_ID = 'ORDER_ID'
    private static final String SPECIFICATION_ID = 'SPECIFICATION_ID'

    @Autowired
    EventListener eventListener

//    @Autowired
//    ProductOrderRepository orderRepository
//
//    @Autowired
//    ShipmentRepository shipmentRepository
//
//    def 'should save shipment'() {
//        given:
//        def order = new ProductOrder(ORDER_ID, SPECIFICATION_ID, 1L, ProductOrder.SUSPENDED)
//        orderRepository.save(order)
//        def event = new Event(Event.ORDER_CREATED, ORDER_ID)
//
//        when:
//        eventListener.listenOrders(event)
//
//        then:
//        !shipmentRepository.findByOrderId(ORDER_ID).empty
//    }
}
