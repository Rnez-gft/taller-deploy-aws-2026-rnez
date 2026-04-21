package com.practica.practica.service.impl;

import com.practica.practica.model.Order;
import com.practica.practica.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final List<Order> orders = new ArrayList<>();
    private long currentId = 1;

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();
    }

    @Override
    public Order createOrder(Order order) {
        order.setId(currentId++);
        orders.add(order);
        return order;
    }

    @Override
    public Optional<Order> updateOrder(Long id, Order updatedOrder) {
        Optional<Order> existing = getOrderById(id);
        if (existing.isPresent()) {
            Order order = existing.get();
            order.setCustomerName(updatedOrder.getCustomerName());
            order.setDescription(updatedOrder.getDescription());
            order.setTotal(updatedOrder.getTotal());
            return Optional.of(order);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteOrder(Long id) {
        return orders.removeIf(order -> order.getId().equals(id));
    }
}