package com.mtulkanov.po.order;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * TODO: This is autogenerated Java-Doc.
 *
 * @author Nikita_Puzankov
 */
@RepositoryRestResource(exported = false)
interface ProductOrderRepository extends MongoRepository<ProductOrder, String> {
}
