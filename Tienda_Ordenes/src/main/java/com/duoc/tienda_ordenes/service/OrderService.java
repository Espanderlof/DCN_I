package com.duoc.tienda_ordenes.service;

import com.duoc.tienda_ordenes.model.Order;
import com.duoc.tienda_ordenes.model.OrderItem;
import com.duoc.tienda_ordenes.repository.OrderRepository;
import com.duoc.tienda_ordenes.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        order.setUserName(orderDTO.getUserName());
        order.setTotal(orderDTO.getTotal());
        order.setStatus(Order.OrderStatus.PENDIENTE);

        List<OrderItem> items = orderDTO.getItems().stream().map(itemDTO -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(itemDTO.getProductId());
            item.setProductName(itemDTO.getProductName());
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            item.setSubtotal(itemDTO.getSubtotal());
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public Optional<Order> updateOrderStatus(Long orderId, Order.OrderStatus status) {
        return orderRepository.findById(orderId).map(order -> {
            order.setStatus(status);
            return orderRepository.save(order);
        });
    }
}