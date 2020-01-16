package com.mtulkanov.ps.shipment;

import com.mtulkanov.po.exceptions.OrderNotFoundException;
import com.mtulkanov.po.order.ProductOrder;
import com.mtulkanov.ps.order.ProductOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private final ProductOrderRepository orderRepository;
    private final ShipmentRepository shipmentRepository;

    @Override
    public void createShipment(String orderId) {
        Optional<ProductOrder> orderOptional = orderRepository.findById(orderId);
        ProductOrder order = orderOptional.orElseThrow(OrderNotFoundException::new);
        log.info("Found order: {}", order);
        Shipment shipment = new Shipment(null, order.getId(), false);
        Shipment savedShipment = shipmentRepository.save(shipment);
        log.info("Created shipment: {}", savedShipment);
    }
}
