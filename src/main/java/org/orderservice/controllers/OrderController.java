package org.orderservice.controllers;

import org.orderservice.entities.IOrderRepository;
import org.orderservice.entities.OrderEntity;
import org.orderservice.entities.OrderStatusEnum;
import org.orderservice.entities.ProductEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrderController {
    private final IOrderRepository orderRepository;

    public OrderController(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping(value = "/cart/addProduct")
    public ResponseEntity addProductToCart(
            @RequestHeader("customer-id") String customerId,
            @RequestBody ProductEntity product) {
        // if customer has no cart created yet, create new one
        OrderEntity stagedOrder = this.orderRepository.findByCustomerIdAndStatus(customerId, OrderStatusEnum.STAGED);
        if (stagedOrder == null) {
            stagedOrder = new OrderEntity(OrderStatusEnum.STAGED, customerId);
        }
        stagedOrder.addProduct(product);
        this.orderRepository.save(stagedOrder);

        return new ResponseEntity(HttpStatus.OK);
    }
}
