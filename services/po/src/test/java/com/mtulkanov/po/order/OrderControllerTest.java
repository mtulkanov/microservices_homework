package com.mtulkanov.po.order;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OrderControllerTest {
    private static final Long ORDER_ID = 1L;
    private static final String SPECIFICATION_ID = "SPECIFICATION_ID";

    private ProductOrder order;
    private ProductOrderService service;
    private ProductOrderController controller;

    @Before
    public void setup() {
        order = new ProductOrder(
                ORDER_ID,
                SPECIFICATION_ID,
                1L,
                ProductOrder.SUSPENDED
        );

        service = mock(ProductOrderService.class);
        Mockito.when(service.orderProductBySpecificationId(SPECIFICATION_ID))
                .thenReturn(order);

        controller = new ProductOrderController(service);
    }

    @Test
    public void shouldCallOrderServiceWithCorrectParams() {
        // when
        controller.orderProductBySpecificationId(SPECIFICATION_ID);

        // then
        verify(service).orderProductBySpecificationId(SPECIFICATION_ID);
    }

    @Test
    public void shouldReturnProductOrder() {
        // when
        ProductOrder orderReturned = controller.orderProductBySpecificationId(SPECIFICATION_ID);

        // then
        assertEquals(order, orderReturned);
    }
}
