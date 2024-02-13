package org.orderservice.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.query.Order;

import javax.annotation.processing.Generated;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    private String customerId;
    private OrderStatusEnum status;
    private String trackingNumber;
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

    public OrderStatusEnum getStatus() {
        return status;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public List<CartProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<CartProductEntity> products) {
        this.products = products;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(String lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
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

    public boolean removeProduct(String productName) {
        int removeIndex = this.getProductCartIndex(productName);
        if (removeIndex != -1) {
            this.products.remove(removeIndex);
            this.updateTimestamp();
            return true;
        }

        return false;
    }

    public boolean setQuantity(String productName, int quantity) {
        if (quantity <= 0) { return false; }
        int productIndex = this.getProductCartIndex(productName);
        if (productIndex != -1) {
            this.products.get(productIndex).setQuantity(quantity);
            this.updateTimestamp();
            return true;
        }

        return false;
    }

    public void setStatus(OrderStatusEnum newStatus) {
        this.status = newStatus;
        this.updateTimestamp();
    }

    public String placeOrder() {
        this.status = OrderStatusEnum.PLACED;
        this.updateTimestamp();
        this.trackingNumber = this.generateTrackingNumber();
        return this.trackingNumber;
    }

    private int getProductCartIndex(ProductEntity product) {
        for (int i = 0; i < this.products.size(); i++) {
            if (product.name.equals(this.products.get(i).getProduct().name)) {
                return i;
            }
        }

        return -1;
    }

    private int getProductCartIndex(String productName) {
        for (int i = 0; i < this.products.size(); i++) {
            if (productName.equals(this.products.get(i).getProduct().name)) {
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

    private String generateTrackingNumber() {
        String alphaList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int alphaListSize = alphaList.length();

        String numberList = "0123456789";
        int numberListSize = numberList.length();

        StringBuilder newTrackingNumber = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 25; i++) {
            switch (rand.nextInt(2)) {
                case 0: {
                    newTrackingNumber.append(alphaList.charAt(rand.nextInt(alphaListSize)));
                    break;
                }

                case 1: {
                    newTrackingNumber.append(numberList.charAt(rand.nextInt(numberListSize)));
                    break;
                }
            }
        }

        return newTrackingNumber.toString();
    }
}
