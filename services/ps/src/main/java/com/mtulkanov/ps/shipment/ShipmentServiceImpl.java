package com.mtulkanov.ps.shipment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    @Override
    public void createShipment(String orderId) {
        Shipment shipment = new Shipment(null, orderId, false);
        Shipment savedShipment = shipmentRepository.save(shipment);
        log.info("Created shipment: {}", savedShipment);
    }
}
