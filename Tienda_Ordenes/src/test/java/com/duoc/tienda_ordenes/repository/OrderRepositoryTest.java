package com.duoc.tienda_ordenes.repository;

import com.duoc.tienda_ordenes.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("verifica la busqueda de ordenes por id de usuario")
    void findByUserIdTest() {
        // Given
        Order order = new Order();
        order.setUserId(1L);
        order.setUserName("usuario test");
        order.setTotal(new BigDecimal("100.00"));
        order.setStatus(Order.OrderStatus.PENDIENTE);
        orderRepository.save(order);

        // When
        List<Order> foundOrders = orderRepository.findByUserId(1L);

        // Then
        assertFalse(foundOrders.isEmpty());
        assertEquals(1L, foundOrders.get(0).getUserId());
    }

    @Test
    @DisplayName("verifica que no se encuentran ordenes para un usuario inexistente")
    void findByUserIdNotFoundTest() {
        // When
        List<Order> foundOrders = orderRepository.findByUserId(999L);

        // Then
        assertTrue(foundOrders.isEmpty());
    }
}