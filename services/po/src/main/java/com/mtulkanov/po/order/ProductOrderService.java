package com.mtulkanov.po.order;

public interface ProductOrderService {
    ProductOrder orderProductBySpecificationId(String specificationId);

    ProductOrder rejectOrder(Long orderId);
}
