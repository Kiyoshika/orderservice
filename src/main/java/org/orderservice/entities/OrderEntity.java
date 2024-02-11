package org.orderservice.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.annotation.processing.Generated;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    private String customerId;
    private OrderStatusEnum status;
    @OneToMany
    @Cascade(CascadeType.ALL)
    private List<CartProductEntity> products;
    private String createdTimestamp;
    private String lastUpdatedTimestamp;

    public OrderEntity() {}
    public OrderEntity(OrderStatusEnum status, String customerId) {
        this.status = status;
        this.customerId = customerId;
        this.products = new ArrayList<CartProductEntity>();
        this.createdTimestamp = this.getTimestamp();
        this.lastUpdatedTimestamp = this.createdTimestamp;
    }

    public void addProduct(ProductEntity product) {
        int productIndex = this.getProductCartIndex(product);
        if (productIndex != -1) {
            this.products.get(productIndex).incrementQuantity();
            this.updateTimestamp();
            return;
        }
        this.products.add(new CartProductEntity(product, 1));
        this.updateTimestamp();
    }

    public void removeProduct(ProductEntity product) {
        int removeIndex = this.getProductCartIndex(product);
        if (removeIndex != -1) {
            this.products.remove(removeIndex);
            this.updateTimestamp();
        }
    }

    public void setQuantity(ProductEntity product, int quantity) {
        if (quantity <= 0) { return; }
        int productIndex = this.getProductCartIndex(product);
        if (productIndex != -1) {
            this.products.get(productIndex).setQuantity(quantity);
            this.updateTimestamp();
        }
    }

    private int getProductCartIndex(ProductEntity product) {
        for (int i = 0; i < this.products.size(); i++) {
            if (product.name.equals(this.products.get(i).getProduct().name)) {
                return i;
            }
        }

        return -1;
    }

    private String getTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }

    private void updateTimestamp() {
        this.lastUpdatedTimestamp = this.getTimestamp();
    }
}
