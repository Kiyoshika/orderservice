package org.orderservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ProductEntity {
    public ProductEntity() {}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    public String name;
    public double unitPrice;
}
