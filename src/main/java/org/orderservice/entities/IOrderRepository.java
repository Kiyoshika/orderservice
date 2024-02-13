package org.orderservice.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<OrderEntity, Integer> {
    public OrderEntity findByCustomerIdAndTrackingNumber(String customerId, String trackingNumber);
    public OrderEntity findByCustomerIdAndStatus(String customerId, OrderStatusEnum status);
    public OrderEntity findByCustomerIdAndStatusAndTrackingNumber(String customerId, OrderStatusEnum status, String trackingNumber);
}
