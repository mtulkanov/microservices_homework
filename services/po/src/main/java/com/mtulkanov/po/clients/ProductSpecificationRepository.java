package com.mtulkanov.po.clients;

import com.mtulkanov.eurekaserver.pc.catalog.ProductSpecification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "pc")
public interface ProductSpecificationRepository {

    @RequestMapping(method = RequestMethod.GET, path = "/catalog/{specificationId}")
    ProductSpecification existsById(@PathVariable("specificationId") String specificationId);
}
