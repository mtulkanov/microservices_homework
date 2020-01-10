package com.mtulkanov.po.order

import com.mtulkanov.eurekaserver.pc.catalog.ProductSpecification
import com.mtulkanov.po.clients.ProductSpecificationRepository
import com.mtulkanov.po.kafka.Event
import com.mtulkanov.po.kafka.KafkaService
import com.mtulkanov.po.kafka.KafkaServiceImpl
import org.springframework.util.concurrent.FailureCallback
import org.springframework.util.concurrent.SuccessCallback
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.mockito.ArgumentMatchers.isA
import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.verify

class ProductOrderServiceImplTest extends Specification {

    private static final String ORDER_ID = "ORDER_ID"
    private static final String SPECIFICATION_ID = "SPECIFICATION_ID"

    private ProductOrder order
    private KafkaService kafkaService
    private ProductOrderRepository orderRepository
    private ProductOrderService orderService

    def setup() {
        def specification = new ProductSpecification();
        ProductSpecificationRepository specificationRepository = Stub {
            existsById(SPECIFICATION_ID) >> specification;
        }

        order = new ProductOrder(
                ORDER_ID,
                SPECIFICATION_ID,
                1L,
                ProductOrder.SUSPENDED
        );

        ProductOrderRepository orderRepository = Stub {
            save(_) >> order;
            findById(_) >> order
        }

        KafkaService kafkaService = Mock();

        orderService = new ProductOrderServiceImpl(
                orderRepository,
                specificationRepository,
                kafkaService
        );
    }

    def 'should create suspended order'() {
        when:
        ProductOrder orderReturned = orderService.orderProductBySpecificationId(SPECIFICATION_ID)

        then:
        orderReturned.getStatus() == ProductOrder.SUSPENDED
    }

    def 'should fire event on order creation'() {
        when:
        orderService.orderProductBySpecificationId(SPECIFICATION_ID);

        then:
//        verify(kafkaService).orderCreated(eq(order), isA(SuccessCallback.class), isA(FailureCallback.class));
        1 * kafkaService.fire(
            KafkaServiceImpl.OUTPUT_EVENT_TOPIC,
            _ as Event,
            _ as FailureCallback,
            _ as FailureCallback
        )
    }
}
