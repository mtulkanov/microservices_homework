package com.mtulkanov.eurekaserver.pc.catalog;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "catalog")
public interface ProductSpecificationRepository extends MongoRepository<ProductSpecification, String> {
}
