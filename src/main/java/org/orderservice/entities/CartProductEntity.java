package org.orderservice.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class CartProductEntity {
    public CartProductEntity() {}
    public CartProductEntity(ProductEntity product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = this.quantity * product.unitPrice;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    @OneToOne
    @Cascade(CascadeType.ALL)
    private ProductEntity product;
    private int quantity;
    private double totalPrice;

    public ProductEntity getProduct() {
        return this.product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public void addQuantity(int quantity) {
        if (quantity <= 0) { return; }
        this.quantity += quantity;
        this.totalPrice += quantity * this.product.unitPrice;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) { return; }
        this.quantity = quantity;
        this.totalPrice = quantity * this.product.unitPrice;
    }

    public void incrementQuantity() {
        this.quantity += 1;
        this.totalPrice += this.product.unitPrice;
    }
}
