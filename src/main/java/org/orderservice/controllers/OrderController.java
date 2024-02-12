package org.orderservice.controllers;

import org.hibernate.query.Order;
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

    /* TODO: view order (GET) */

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

    @PostMapping(value = "/cart/removeProduct")
    public ResponseEntity removeProductFromCart(
            @RequestHeader("customer-id") String customerId,
            String productName) {
        OrderEntity stagedOrder = this.orderRepository.findByCustomerIdAndStatus(customerId, OrderStatusEnum.STAGED);
        if (stagedOrder == null) {
            return new ResponseEntity("No staged order found for this customer.", HttpStatus.NOT_FOUND);
        }
        if (!stagedOrder.removeProduct(productName)) {
            return new ResponseEntity("Product not found in cart.", HttpStatus.NOT_FOUND);
        }
        this.orderRepository.save(stagedOrder);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/cart/updateQuantity")
    public ResponseEntity updateCartQuantity(
            @RequestHeader("customer-id") String customerId,
            String productName,
            int newQuantity) {
        if (newQuantity < 1) {
            return new ResponseEntity("New quantity must be >= 1.", HttpStatus.BAD_REQUEST);
        }
        OrderEntity stagedOrder = this.orderRepository.findByCustomerIdAndStatus(customerId, OrderStatusEnum.STAGED);
        if (stagedOrder == null) {
            return new ResponseEntity("No staged order found for this customer.", HttpStatus.NOT_FOUND);
        }
        if (!stagedOrder.setQuantity(productName, newQuantity)) {
            return new ResponseEntity("Product not found in cart.", HttpStatus.NOT_FOUND);
        }
        this.orderRepository.save(stagedOrder);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/cart/place")
    public ResponseEntity placeOrder(@RequestHeader("customer-id") String customerId) {
        OrderEntity stagedOrder = this.orderRepository.findByCustomerIdAndStatus(customerId, OrderStatusEnum.STAGED);
        if (stagedOrder == null) {
            return new ResponseEntity("No staged order found for this customer.", HttpStatus.NOT_FOUND);
        }
        String trackingNumber = stagedOrder.placeOrder();
        this.orderRepository.save(stagedOrder);

        return new ResponseEntity(trackingNumber, HttpStatus.OK);
    }

    @PostMapping(value = "/cancel")
    public ResponseEntity cancelOrder(
            @RequestHeader("customer-id") String customerId,
            String trackingNumber) {
        OrderEntity placedOrder = this.orderRepository.findByCustomerIdAndStatusAndTrackingNumber(customerId, OrderStatusEnum.PLACED, trackingNumber);
        if (placedOrder == null) {
            return new ResponseEntity("No placed order found with this tracking number and customer.", HttpStatus.NOT_FOUND);
        }
        placedOrder.setStatus(OrderStatusEnum.CANCELLED);
        this.orderRepository.save(placedOrder);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/deliver")
    public ResponseEntity deliverOrder(
            @RequestHeader("customer-id") String customerId,
            String trackingNumber) {
        OrderEntity placedOrder = this.orderRepository.findByCustomerIdAndStatusAndTrackingNumber(customerId, OrderStatusEnum.PLACED, trackingNumber);
        if (placedOrder == null) {
            return new ResponseEntity("No placed order found with this tracking number and customer.", HttpStatus.NOT_FOUND);
        }
        placedOrder.setStatus(OrderStatusEnum.DELIVERED);
        this.orderRepository.save(placedOrder);

        return new ResponseEntity(HttpStatus.OK);
    }
}
