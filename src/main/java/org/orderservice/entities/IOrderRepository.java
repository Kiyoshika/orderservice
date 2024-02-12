package org.orderservice.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<OrderEntity, Integer> {
    public OrderEntity findByCustomerIdAndStatus(String customerId, OrderStatusEnum status);
    public OrderEntity findByCustomerIdAndStatusAndTrackingNumber(String customerid, OrderStatusEnum status, String trackingNumber);
}
