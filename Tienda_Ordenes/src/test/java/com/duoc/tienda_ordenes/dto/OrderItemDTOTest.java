package com.duoc.tienda_ordenes.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

class OrderItemDTOTest {

    @Test
    @DisplayName("verifica la creacion y manipulacion de un dto de item de orden")
    void orderItemDTOTest() {
        // Given
        OrderItemDTO itemDTO = new OrderItemDTO();
        
        // When
        itemDTO.setProductId(1L);
        itemDTO.setProductName("producto test");
        itemDTO.setQuantity(2);
        itemDTO.setPrice(new BigDecimal("50.00"));
        itemDTO.setSubtotal(new BigDecimal("100.00"));
        
        // Then
        assertEquals(1L, itemDTO.getProductId());
        assertEquals("producto test", itemDTO.getProductName());
        assertEquals(2, itemDTO.getQuantity());
        assertEquals(new BigDecimal("50.00"), itemDTO.getPrice());
        assertEquals(new BigDecimal("100.00"), itemDTO.getSubtotal());
    }
}