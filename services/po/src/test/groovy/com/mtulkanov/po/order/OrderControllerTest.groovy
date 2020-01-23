package com.mtulkanov.po.order


import spock.lang.Specification

class OrderControllerTest extends Specification {

    private static final String ORDER_ID = 'ORDER_ID'
    private static final String SPECIFICATION_ID = 'SPECIFICATION_ID'

    private ProductOrder order
    private ProductOrderService service
    private ProductOrderController controller

    def setup() {
        order = new ProductOrder(
                ORDER_ID,
                SPECIFICATION_ID,
                1L,
                ProductOrder.SUSPENDED
        )

        service = Mock(ProductOrderService) {
            orderProductBySpecificationId(_ as String) >> order
        }

        controller = new ProductOrderController(service)
    }

    def 'should call order service with correct params'() {
        when:
        controller.orderProductBySpecificationId(SPECIFICATION_ID)

        then:
        1 * service.orderProductBySpecificationId(SPECIFICATION_ID)
    }

    def 'should return product order'() {
        when:
        ProductOrder orderReturned = controller.orderProductBySpecificationId(SPECIFICATION_ID)

        then:
        order == orderReturned
    }
}
