package com.duoc.tienda_ordenes.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class OrderStatusDTOTest {

    @Test
    @DisplayName("verifica la creacion y modificacion del dto de estado de orden")
    void orderStatusDTOTest() {
        // Given
        OrderStatusDTO statusDTO = new OrderStatusDTO();
        
        // When
        statusDTO.setStatus(Order.OrderStatus.PENDIENTE);
        
        // Then
        assertNotNull(statusDTO.getStatus());
        assertEquals(Order.OrderStatus.PENDIENTE, statusDTO.getStatus());
    }
}