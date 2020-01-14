package com.mtulkanov.ps.shipment;

import com.mtulkanov.po.exceptions.OrderNotFoundException;
import com.mtulkanov.po.order.ProductOrder;
import com.mtulkanov.po.order.ProductOrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ProductOrderRepository orderRepository;

    @Override
    public void createShipment(String orderId) {
        Optional<ProductOrder> orderOptional = orderRepository.findById(orderId);
        ProductOrder order = orderOptional.orElseThrow(OrderNotFoundException::new);
    }
}
