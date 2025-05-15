package com.arunbalachandran.queryelasticspringai.service;

import com.arunbalachandran.queryelasticspringai.entity.Order;
import com.arunbalachandran.queryelasticspringai.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
   
    @Autowired
    private OrderRepository orderRepository;
    
    @Transactional
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Order getOrderById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Order not found with id: %s", id)));
    }
}