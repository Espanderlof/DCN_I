package com.duoc.tienda_ordenes.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class OrderTest {

    @Test
    @DisplayName("verifica la creacion de una orden con todos sus atributos")
    void createOrderTest() {
        // Given
        Order order = new Order();
        order.setId(1L);
        order.setUserId(100L);
        order.setUserName("juan perez");
        order.setTotal(new BigDecimal("1000.00"));
        order.setStatus(Order.OrderStatus.PENDIENTE);
        
        // When & Then
        assertEquals(1L, order.getId());
        assertEquals(100L, order.getUserId());
        assertEquals("juan perez", order.getUserName());
        assertEquals(new BigDecimal("1000.00"), order.getTotal());
        assertEquals(Order.OrderStatus.PENDIENTE, order.getStatus());
    }

    @Test
    @DisplayName("verifica que la fecha de creacion se establece automaticamente")
    void onCreateTest() {
        // Given
        Order order = new Order();
        
        // When
        order.onCreate();
        
        // Then
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
    }

    @Test
    @DisplayName("verifica que la fecha de actualizacion se modifica correctamente")
    void onUpdateTest() {
        // Given
        Order order = new Order();
        order.onCreate();
        LocalDateTime originalUpdateTime = order.getUpdatedAt();
        
        // When
        try {
            Thread.sleep(1); // pequeña pausa para asegurar diferencia en timestamps
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        order.onUpdate();
        
        // Then
        assertTrue(order.getUpdatedAt().isAfter(originalUpdateTime));
    }

    @Test
    @DisplayName("verifica la gestion de items de la orden")
    void orderItemsManagementTest() {
        // Given
        Order order = new Order();
        List<OrderItem> items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setProductId(1L);
        item.setProductName("producto test");
        item.setQuantity(2);
        item.setPrice(new BigDecimal("500.00"));
        item.setSubtotal(new BigDecimal("1000.00"));
        item.setOrder(order);
        items.add(item);
        
        // When
        order.setItems(items);
        
        // Then
        assertNotNull(order.getItems());
        assertEquals(1, order.getItems().size());
        assertEquals("producto test", order.getItems().get(0).getProductName());
    }

    @Test
    @DisplayName("verifica todos los estados posibles de la orden")
    void orderStatusTest() {
        // Given
        Order order = new Order();
        
        // When & Then
        order.setStatus(Order.OrderStatus.PENDIENTE);
        assertEquals(Order.OrderStatus.PENDIENTE, order.getStatus());
        
        order.setStatus(Order.OrderStatus.ENTREGADA);
        assertEquals(Order.OrderStatus.ENTREGADA, order.getStatus());
        
        order.setStatus(Order.OrderStatus.CANCELADA);
        assertEquals(Order.OrderStatus.CANCELADA, order.getStatus());
    }
}