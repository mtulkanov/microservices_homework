package com.mtulkanov.po.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductOrderController {

    private final ProductOrderService productOrderService;

    @PutMapping("/catalog/{specificationId}/order")
    public ProductOrder orderProductBySpecificationId(@PathVariable String specificationId) {
        return productOrderService.orderProductBySpecificationId(specificationId);
    }
}
