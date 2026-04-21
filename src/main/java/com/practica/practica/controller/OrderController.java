package com.practica.practica.controller;

import com.practica.practica.model.Order;
import com.practica.practica.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        logger.info("GET /orders - fetching all orders");

        List<Order> orders = orderService.getAllOrders();

        logger.info("GET /orders - returned {} orders", orders.size());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        logger.info("GET /orders/{} - fetching order", id);

        Optional<Order> order = orderService.getOrderById(id);

        if (order.isPresent()) {
            logger.info("GET /orders/{} - found order", id);
            return ResponseEntity.ok(order.get());
        } else {
            logger.warn("GET /orders/{} - order not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        logger.info("POST /orders - creating order: {}", order);

        Order created = orderService.createOrder(order);

        logger.info("POST /orders - created order with id {}", created.getId());
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        logger.info("PUT /orders/{} - updating order", id);

        Optional<Order> updated = orderService.updateOrder(id, order);

        if (updated.isPresent()) {
            logger.info("PUT /orders/{} - updated successfully", id);
            return ResponseEntity.ok(updated.get());
        } else {
            logger.warn("PUT /orders/{} - order not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        logger.info("DELETE /orders/{} - deleting order", id);

        boolean deleted = orderService.deleteOrder(id);

        if (deleted) {
            logger.info("DELETE /orders/{} - deleted successfully", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("DELETE /orders/{} - order not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}