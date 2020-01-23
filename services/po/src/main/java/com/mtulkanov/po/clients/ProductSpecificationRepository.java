package com.mtulkanov.po.clients;

import com.mtulkanov.eurekaserver.pc.catalog.ProductSpecification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "pc",
        url = "${productSpecification.url}"
)
public interface ProductSpecificationRepository {

    @GetMapping(path = "/catalog/{specificationId}")
    ProductSpecification existsById(@PathVariable("specificationId") String specificationId);
}
