package com.mtulkanov.ps.shipment

import com.mtulkanov.po.exceptions.OrderNotFoundException
import com.mtulkanov.po.order.ProductOrder
import com.mtulkanov.po.order.ProductOrderRepository
import spock.lang.Specification

class ShipmentServiceImplTest extends Specification {

    private static final String ORDER_ID = 'ORDER_ID'

    def 'should throw exception if order was not found'() {
        given:
        ProductOrderRepository orderRepository = Stub(ProductOrderRepository) {
            findById(_ as String) >> Optional.empty()
        }
        ShipmentService shipmentService = new ShipmentServiceImpl(orderRepository)

        when:
        shipmentService.createShipment(ORDER_ID)

        then:
        thrown(OrderNotFoundException)
    }
}
