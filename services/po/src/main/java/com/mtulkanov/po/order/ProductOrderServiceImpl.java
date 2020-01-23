package com.mtulkanov.po.order;

import com.mtulkanov.po.clients.ProductSpecificationRepository;
import com.mtulkanov.po.exceptions.OrderNotFoundException;
import com.mtulkanov.po.exceptions.SpecificationNotFoundException;
import com.mtulkanov.po.kafka.Event;
import com.mtulkanov.po.kafka.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductOrderServiceImpl implements ProductOrderService {
    private final ProductOrderRepository orderRepository;
    private final ProductSpecificationRepository specificationRepository;
    private final EventRepository eventRepository;

    public ProductOrder orderProductBySpecificationId(String specificationId) {
        if (specificationRepository.existsById(specificationId) == null) {
            String message = "There is no product specification with Id: " + specificationId;
            log.error(message);
            throw new SpecificationNotFoundException(message);
        }
        final String orderId = new ObjectId().toHexString();
        ProductOrder productOrder = new ProductOrder(
                orderId,
                specificationId,
                1L,
                ProductOrder.SUSPENDED
        );
        Event event = new Event(Event.ORDER_CREATED, orderId);
        return orderProductUsingTransaction(productOrder, event);
    }

    public ProductOrder rejectOrder(String orderId) {
        Optional<ProductOrder> orderOptional = orderRepository.findById(orderId);
        orderOptional.ifPresent(order -> {
            order.setStatus(ProductOrder.REJECTED);
            orderRepository.save(order);
            log.info("Order {} was rejected", orderId);
        });
        return orderOptional.orElseThrow(OrderNotFoundException::new);
    }

    @Transactional
    // TODO move to repository
    public ProductOrder orderProductUsingTransaction(ProductOrder order, Event event) {
        ProductOrder orderCreated = orderRepository.save(order);
        Event eventCreated = eventRepository.save(event);
        log.info("Created {}", orderCreated);
        log.info("Created {}", eventCreated);
        return order;
    }
}
