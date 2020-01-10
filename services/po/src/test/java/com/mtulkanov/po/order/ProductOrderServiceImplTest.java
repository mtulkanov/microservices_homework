package com.mtulkanov.po.order;

import com.mtulkanov.eurekaserver.pc.catalog.ProductSpecification;
import com.mtulkanov.po.clients.ProductSpecificationRepository;
import com.mtulkanov.po.exceptions.OrderNotFoundException;
import com.mtulkanov.po.kafka.KafkaService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductOrderServiceImplTest {
    private static final String ORDER_ID = "ORDER_ID";
    private static final String SPECIFICATION_ID = "SPECIFICATION_ID";

    private ProductOrder order;
    private KafkaService kafkaService;
    private ProductOrderRepository orderRepository;
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

        orderRepository = mock(ProductOrderRepository.class);
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
    public void shouldRaiseEventOnProductOrderCreation() {
        // when
        orderService.orderProductBySpecificationId(SPECIFICATION_ID);

        // then
        verify(kafkaService).orderCreated(eq(order), isA(SuccessCallback.class), isA(FailureCallback.class));
    }

    @Test
    public void shouldRejectOrder() {
        // when
        ProductOrder rejectedOrder = orderService.rejectOrder(ORDER_ID);

        // then
        assertEquals(ProductOrder.REJECTED, rejectedOrder.getStatus());
        verify(orderRepository).save(order);
    }

    @Test(expected = OrderNotFoundException.class)
    public void onRejectionShouldThrowCorrectExceptionIfOrderWasNotFound() {
        // given
        var exception = new OrderNotFoundException("Could not find order " + ORDER_ID);
        when(orderRepository.findById(eq(ORDER_ID)))
                .thenThrow(exception);

        // when
        orderService.rejectOrder(ORDER_ID);
    }
}
