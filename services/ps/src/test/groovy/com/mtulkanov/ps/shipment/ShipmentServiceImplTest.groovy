package com.mtulkanov.ps.shipment

import com.mtulkanov.po.exceptions.OrderNotFoundException
import com.mtulkanov.po.order.ProductOrder
import com.mtulkanov.po.order.ProductOrderRepository
import spock.lang.Specification

class ShipmentServiceImplTest extends Specification {

    private static final String ORDER_ID = 'ORDER_ID'
    private static final String SPECIFICATION_ID = 'SPECIFICATION_ID'

    def 'should throw exception if order was not found'() {
        given:
        ProductOrderRepository orderRepository = Stub(ProductOrderRepository) {
            findById(_ as String) >> Optional.empty()
        }
        ShipmentRepository shipmentRepository = Stub()
        ShipmentService shipmentService = new ShipmentServiceImpl(
                orderRepository,
                shipmentRepository
        )

        when:
        shipmentService.createShipment(ORDER_ID)

        then:
        thrown(OrderNotFoundException)
    }

    def 'should save new shipment to repository'() {
        given:
        def order = new ProductOrder(ORDER_ID, SPECIFICATION_ID, 1L, ProductOrder.SUSPENDED)
        ProductOrderRepository orderRepository = Stub(ProductOrderRepository) {
            findById(_ as String) >> Optional.of(order)
        }
        ShipmentRepository shipmentRepository = Mock()
        ShipmentService shipmentService = new ShipmentServiceImpl(
                orderRepository,
                shipmentRepository
        )

        when:
        shipmentService.createShipment(ORDER_ID)

        then:
        1 * shipmentRepository.save(_ as Shipment)
    }

    def 'should ignore rejected orders'() {
        given:
        def order = new ProductOrder(ORDER_ID, SPECIFICATION_ID, 1L, ProductOrder.REJECTED)
        ProductOrderRepository orderRepository = Stub(ProductOrderRepository) {
            findById(_ as String) >> Optional.of(order)
        }
        ShipmentRepository shipmentRepository = Mock()
        ShipmentService shipmentService = new ShipmentServiceImpl(
                orderRepository,
                shipmentRepository
        )

        when:
        shipmentService.createShipment(ORDER_ID)

        then:
        0 * shipmentRepository.save(_ as Shipment)
    }

    def 'should ignore accepted orders'() {
        given:
        def order = new ProductOrder(ORDER_ID, SPECIFICATION_ID, 1L, ProductOrder.ACCEPTED)
        ProductOrderRepository orderRepository = Stub(ProductOrderRepository) {
            findById(_ as String) >> Optional.of(order)
        }
        ShipmentRepository shipmentRepository = Mock()
        ShipmentService shipmentService = new ShipmentServiceImpl(
                orderRepository,
                shipmentRepository
        )

        when:
        shipmentService.createShipment(ORDER_ID)

        then:
        0 * shipmentRepository.save(_ as Shipment)
    }
}
