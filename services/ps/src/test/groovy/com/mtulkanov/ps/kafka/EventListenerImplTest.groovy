package com.mtulkanov.ps.kafka

import com.mtulkanov.po.kafka.Event
import com.mtulkanov.ps.exceptions.UnknownEventException
import com.mtulkanov.ps.shipment.ShipmentService
import spock.lang.Specification

class EventListenerImplTest extends Specification {

    private static final String ORDER_ID = 'ORDER_ID'

    private ShipmentService shipmentService
    private EventListener eventListener

    def setup() {
        shipmentService = Mock()
        eventListener = new EventListenerImpl(shipmentService)
    }

    def 'should call shipment service to create a shipment'() {
        given:
        def event = new Event(Event.ORDER_CREATED, ORDER_ID)

        when:
        eventListener.listenOrders(event)

        then:
        1 * shipmentService.createShipment(ORDER_ID)
    }

    def 'should throw exception on unknown order events'() {
        given:
        def event = new Event("UNKNOWN_EVENT", ORDER_ID)

        when:
        eventListener.listenOrders(event)

        then:
        thrown(UnknownEventException)
    }
}
