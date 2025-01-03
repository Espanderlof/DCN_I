package com.duoc.tienda_ordenes.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

class OrderItemTest {

    @Test
    @DisplayName("verifica la creacion de un item de orden con todos sus atributos")
    void createOrderItemTest() {
        // Given
        OrderItem item = new OrderItem();
        Order order = new Order();
        
        // When
        item.setId(1L);
        item.setOrder(order);
        item.setProductId(100L);
        item.setProductName("producto prueba");
        item.setQuantity(2);
        item.setPrice(new BigDecimal("500.00"));
        item.setSubtotal(new BigDecimal("1000.00"));
        
        // Then
        assertEquals(1L, item.getId());
        assertNotNull(item.getOrder());
        assertEquals(100L, item.getProductId());
        assertEquals("producto prueba", item.getProductName());
        assertEquals(2, item.getQuantity());
        assertEquals(new BigDecimal("500.00"), item.getPrice());
        assertEquals(new BigDecimal("1000.00"), item.getSubtotal());
    }

    @Test
    @DisplayName("verifica la relacion bidireccional entre order e item")
    void orderRelationshipTest() {
        // Given
        OrderItem item = new OrderItem();
        Order order = new Order();
        
        // When
        item.setOrder(order);
        
        // Then
        assertNotNull(item.getOrder());
        assertEquals(order, item.getOrder());
    }
}