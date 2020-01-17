package com.mtulkanov.ps.shipment;

import com.mtulkanov.po.exceptions.OrderNotFoundException;
import com.mtulkanov.po.order.ProductOrder;
import com.mtulkanov.po.order.ProductOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private static final List<String> IGNORE_ORDER_STATUSES = List.of(
            ProductOrder.ACCEPTED,
            ProductOrder.REJECTED
    );

    private final ProductOrderRepository orderRepository;
    private final ShipmentRepository shipmentRepository;

    @Override
    public void createShipment(String orderId) {
        Optional<ProductOrder> orderOptional = orderRepository.findById(orderId);
        ProductOrder order = orderOptional.orElseThrow(OrderNotFoundException::new);
        if (IGNORE_ORDER_STATUSES.contains(order.getStatus())) {
            return;
        }
        log.info("Found order: {}", order);
        Shipment shipment = new Shipment(null, order.getId(), false);
        Shipment savedShipment = shipmentRepository.save(shipment);
        log.info("Created shipment: {}", savedShipment);
    }
}
