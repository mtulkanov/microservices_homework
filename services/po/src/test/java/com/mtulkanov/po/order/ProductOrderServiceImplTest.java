package com.mtulkanov.po.order;

import com.mtulkanov.eurekaserver.pc.catalog.ProductSpecification;
import com.mtulkanov.po.clients.ProductSpecificationRepository;
import com.mtulkanov.po.kafka.KafkaService;
import com.mtulkanov.po.exceptions.EventNotRaisedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

public class ProductOrderServiceImplTest {
    private static final Long ORDER_ID = 1L;
    private static final String SPECIFICATION_ID = "SPECIFICATION_ID";

    private ProductOrder order;
    private KafkaService kafkaService;
    private ProductOrderService orderService;

    @Before
    public void setup() {
        var specification = new ProductSpecification();
        var specificationRepository = mock(ProductSpecificationRepository.class);
        when(specificationRepository.existsById(SPECIFICATION_ID))
                .thenReturn(specification);

        order = new ProductOrder(
                ORDER_ID,
                SPECIFICATION_ID,
                1L,
                ProductOrder.SUSPENDED
        );

        var orderRepository = mock(ProductOrderRepository.class);
        when(orderRepository.save(any()))
                .thenReturn(order);
        when(orderRepository.findById(any()))
                .thenReturn(Optional.of(order));

        kafkaService = mock(KafkaService.class);

        orderService = new ProductOrderServiceImpl(
                orderRepository,
                specificationRepository,
                kafkaService
        );
    }

    @Test
    public void shouldCreateSuspendedOrder() {
        // when
        ProductOrder orderReturned = orderService.orderProductBySpecificationId(SPECIFICATION_ID);

        // then
        assertEquals(orderReturned.getStatus(), ProductOrder.SUSPENDED);
    }

    @Test
    public void shouldRaiseEventOnProductOrderCreation() throws EventNotRaisedException {
        // when
        orderService.orderProductBySpecificationId(SPECIFICATION_ID);

        // then
        verify(kafkaService).orderCreated(eq(order), isA(SuccessCallback.class), isA(FailureCallback.class));
    }

    @Test
    public void shouldRejectOrderWhenOrderCreationEventFailed() throws EventNotRaisedException {
        // given
        EventNotRaisedException exception = new EventNotRaisedException("Could not raise event \"OrderCreated\"");
        doThrow(exception)
                .when(kafkaService)
                .orderCreated(
                        any(),
                        isA(SuccessCallback.class),
                        isA(FailureCallback.class)
                );

        // when
        ProductOrder orderReturned = orderService.orderProductBySpecificationId(SPECIFICATION_ID);

        // then
        assertEquals(orderReturned.getStatus(), ProductOrder.REJECTED);
    }

    @Test
    public void shouldRejectOrder() {
        // when
        ProductOrder rejectedOrder = orderService.rejectOrder(ORDER_ID);

        // then
        assertEquals(ProductOrder.REJECTED, rejectedOrder.getStatus());
    }

    @Test
    public void shouldThrowCorrectExceptionIfOrderWasNotFoundBeforeRejection() {
        // given
    }
}
