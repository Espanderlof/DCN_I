package com.duoc.tienda_ordenes.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.Arrays;

class OrderDTOTest {

    @Test
    @DisplayName("verifica la creacion y manipulacion de un dto de orden")
    void orderDTOTest() {
        // Given
        OrderDTO orderDTO = new OrderDTO();
        OrderItemDTO itemDTO = new OrderItemDTO();
        
        // When
        orderDTO.setUserId(1L);
        orderDTO.setUserName("usuario test");
        orderDTO.setTotal(new BigDecimal("100.00"));
        orderDTO.setItems(Arrays.asList(itemDTO));
        
        // Then
        assertEquals(1L, orderDTO.getUserId());
        assertEquals("usuario test", orderDTO.getUserName());
        assertEquals(new BigDecimal("100.00"), orderDTO.getTotal());
        assertNotNull(orderDTO.getItems());
        assertEquals(1, orderDTO.getItems().size());
    }
}