package com.mtulkanov.po.order;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Nikita_Puzankov
 */
@RepositoryRestResource(path = "order")
public interface ProductOrderRepository extends MongoRepository<ProductOrder, String> {
}
