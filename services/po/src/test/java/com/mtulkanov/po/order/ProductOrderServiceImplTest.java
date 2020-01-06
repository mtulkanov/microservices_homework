package com.mtulkanov.po.order;

import com.mtulkanov.eurekaserver.pc.catalog.ProductSpecification;
import com.mtulkanov.po.clients.ProductSpecificationRepository;
import com.mtulkanov.po.events.EventService;
import com.mtulkanov.po.exceptions.EventNotRaisedException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

public class ProductOrderServiceImplTest {
    private static final String ORDER_ID = "ORDER_ID";
    private static final String SPECIFICATION_ID = "SPECIFICATION_ID";

    private ProductOrder order;
    private EventService eventService;
    private ProductOrderService service;

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

        eventService = mock(EventService.class);

        service = new ProductOrderServiceImpl(
                orderRepository,
                specificationRepository,
                eventService
        );
    }

    @Test
    public void shouldCreateSuspendedOrder() {
        // when
        ProductOrder orderReturned = service.orderProductBySpecificationId(SPECIFICATION_ID);

        // then
        assertTrue(orderReturned.getStatus().equals(ProductOrder.SUSPENDED));
    }

    @Test
    public void shouldRaiseEventOnProductOrderCreation() throws EventNotRaisedException {
        // when
        service.orderProductBySpecificationId(SPECIFICATION_ID);

        // then
        verify(eventService).orderCreated(order);
    }

    @Test
    public void shouldRejectOrderWhenOrderCreationEventFailed() throws EventNotRaisedException {
        // given
        EventNotRaisedException exception = new EventNotRaisedException("Could not raise event \"OrderCreated\"");
        doThrow(exception)
                .when(eventService)
                .orderCreated(any());

        // when
        ProductOrder orderReturned = service.orderProductBySpecificationId(SPECIFICATION_ID);

        // then
        assertTrue(orderReturned.getStatus().equals(ProductOrder.REJECTED));
    }
}
