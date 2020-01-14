package com.mtulkanov.ps.shipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
public class Shipment {

    @Id
    private String id;
    private String orderId;
    private boolean shipped;
}
