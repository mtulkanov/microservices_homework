package com.mtulkanov.po.order;

import com.mtulkanov.po.clients.ProductSpecificationRepository;
import com.mtulkanov.po.exceptions.OrderNotFoundException;
import com.mtulkanov.po.exceptions.SpecificationNotFoundException;
import com.mtulkanov.po.kafka.Event;
import com.mtulkanov.po.kafka.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

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
        final ObjectId orderId = new ObjectId();
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
    public ProductOrder orderProductUsingTransaction(ProductOrder order, Event event) {
        ProductOrder orderCreated = orderRepository.save(order);
        Event eventCreated = eventRepository.save(event);
        log.info("Created {}", orderCreated);
        log.info("Created {}", eventCreated);
        return order;
    }

    private SuccessCallback<SendResult<String, Event>> successCallback(
            final String orderId
    ) {
        String message = "Event ORDER_CREATED for order {}" +
                " was successfully saved to kafka with offset {}";
        return sendResult -> log.info(
                message,
                orderId,
                sendResult.getRecordMetadata().offset()
        );
    }

    private FailureCallback failureCallback(final String orderId) {
        String message = String.format(
                "Saving ORDER_CREATE event for order %s failed because of exception",
                orderId
        );
        return throwable -> log.warn(message, throwable);
    }
}
