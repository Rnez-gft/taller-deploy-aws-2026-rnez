package com.practica.practica.service;

import com.practica.practica.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<Order> getAllOrders();

    Optional<Order> getOrderById(Long id);

    Order createOrder(Order order);

    Optional<Order> updateOrder(Long id, Order updatedOrder);

    boolean deleteOrder(Long id);
}