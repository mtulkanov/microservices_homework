package com.mtulkanov.ps.kafka;

import com.mtulkanov.po.kafka.Event;
import com.mtulkanov.ps.exceptions.UnknownEventException;
import com.mtulkanov.ps.shipment.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class EventListenerImpl implements EventListener {

    private final ShipmentService shipmentService;

    @Override
    @KafkaListener
    public void listenOrders(Event event) {
        if (event.getType().equals(Event.ORDER_CREATED)) {
            shipmentService.createShipment(event.getOrderId());
            return;
        }
        throw new UnknownEventException("Unknown event type: " + event.getType());
    }
}
