package com.mtulkanov.ps.shipment


import spock.lang.Specification

class ShipmentServiceImplTest extends Specification {

    private static final String ORDER_ID = 'ORDER_ID'
    private static final String SPECIFICATION_ID = 'SPECIFICATION_ID'

    def 'should save new shipment to repository'() {
        given:
        ShipmentRepository shipmentRepository = Mock()
        ShipmentService shipmentService = new ShipmentServiceImpl(shipmentRepository)

        when:
        shipmentService.createShipment(ORDER_ID)

        then:
        1 * shipmentRepository.save(_ as Shipment)
    }
}
