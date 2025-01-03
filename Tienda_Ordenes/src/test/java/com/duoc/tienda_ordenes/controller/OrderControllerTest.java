package com.duoc.tienda_ordenes.controller;

import com.duoc.tienda_ordenes.model.Order;
import com.duoc.tienda_ordenes.model.OrderStatusDTO;
import com.duoc.tienda_ordenes.service.OrderService;
import com.duoc.tienda_ordenes.dto.OrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    @DisplayName("verifica la creacion exitosa de una orden")
    void createOrderTest() throws Exception {
        // Given
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);
        orderDTO.setUserName("usuario test");
        orderDTO.setTotal(new BigDecimal("100.00"));

        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setUserName("usuario test");
        order.setTotal(new BigDecimal("100.00"));
        order.setStatus(Order.OrderStatus.PENDIENTE);

        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(order);

        // When & Then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.status").value("PENDIENTE"));
    }

    @Test
    @DisplayName("verifica la obtencion de todas las ordenes")
    void getAllOrdersTest() throws Exception {
        // Given
        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setStatus(Order.OrderStatus.PENDIENTE);

        when(orderService.getAllOrders()).thenReturn(Arrays.asList(order));

        // When & Then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDIENTE"));
    }

    @Test
    @DisplayName("verifica la obtencion de ordenes por usuario")
    void getUserOrdersTest() throws Exception {
        // Given
        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setStatus(Order.OrderStatus.PENDIENTE);

        when(orderService.getUserOrders(1L)).thenReturn(Arrays.asList(order));

        // When & Then
        mockMvc.perform(get("/api/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(1));
    }

    @Test
    @DisplayName("verifica la actualizacion exitosa del estado de una orden")
    void updateOrderStatusSuccessTest() throws Exception {
        // Given
        OrderStatusDTO statusDTO = new OrderStatusDTO();
        statusDTO.setStatus(Order.OrderStatus.ENTREGADA);

        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setStatus(Order.OrderStatus.ENTREGADA);

        when(orderService.updateOrderStatus(1L, Order.OrderStatus.ENTREGADA))
                .thenReturn(Optional.of(updatedOrder));

        // When & Then
        mockMvc.perform(patch("/api/orders/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ENTREGADA"));
    }

    @Test
    @DisplayName("verifica el manejo de orden no encontrada al actualizar estado")
    void updateOrderStatusNotFoundTest() throws Exception {
        // Given
        OrderStatusDTO statusDTO = new OrderStatusDTO();
        statusDTO.setStatus(Order.OrderStatus.ENTREGADA);

        when(orderService.updateOrderStatus(999L, Order.OrderStatus.ENTREGADA))
                .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(patch("/api/orders/999/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("verifica el manejo de estado invalido al actualizar orden")
    void updateOrderStatusBadRequestTest() throws Exception {
        // Given
        OrderStatusDTO statusDTO = new OrderStatusDTO();
        statusDTO.setStatus(Order.OrderStatus.ENTREGADA);

        when(orderService.updateOrderStatus(1L, Order.OrderStatus.ENTREGADA))
                .thenThrow(new IllegalArgumentException());

        // When & Then
        mockMvc.perform(patch("/api/orders/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isBadRequest());
    }
}