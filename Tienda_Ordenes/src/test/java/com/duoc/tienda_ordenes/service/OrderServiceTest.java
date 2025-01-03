package com.duoc.tienda_ordenes.service;

import com.duoc.tienda_ordenes.model.Order;
import com.duoc.tienda_ordenes.repository.OrderRepository;
import com.duoc.tienda_ordenes.dto.OrderDTO;
import com.duoc.tienda_ordenes.dto.OrderItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderDTO orderDTO;
    private Order order;

    @BeforeEach
    void setUp() {
        // Preparar OrderDTO para pruebas
        orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);
        orderDTO.setUserName("usuario test");
        orderDTO.setTotal(new BigDecimal("100.00"));
        
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId(1L);
        itemDTO.setProductName("producto test");
        itemDTO.setQuantity(2);
        itemDTO.setPrice(new BigDecimal("50.00"));
        itemDTO.setSubtotal(new BigDecimal("100.00"));
        orderDTO.setItems(Arrays.asList(itemDTO));

        // Preparar Order para respuestas del mock
        order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setUserName("usuario test");
        order.setTotal(new BigDecimal("100.00"));
        order.setStatus(Order.OrderStatus.PENDIENTE);
    }

    @Test
    @DisplayName("verifica la creacion exitosa de una orden")
    void createOrderTest() {
        // Given
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        Order createdOrder = orderService.createOrder(orderDTO);

        // Then
        assertNotNull(createdOrder);
        assertEquals(Order.OrderStatus.PENDIENTE, createdOrder.getStatus());
        assertEquals(orderDTO.getUserId(), createdOrder.getUserId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("verifica la obtencion de todas las ordenes")
    void getAllOrdersTest() {
        // Given
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findAll()).thenReturn(orders);

        // When
        List<Order> result = orderService.getAllOrders();

        // Then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("verifica la obtencion de ordenes por usuario")
    void getUserOrdersTest() {
        // Given
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findByUserId(1L)).thenReturn(orders);

        // When
        List<Order> result = orderService.getUserOrders(1L);

        // Then
        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).getUserId());
        verify(orderRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("verifica la actualizacion exitosa del estado de una orden")
    void updateOrderStatusSuccessTest() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        Optional<Order> result = orderService.updateOrderStatus(1L, Order.OrderStatus.ENTREGADA);

        // Then
        assertTrue(result.isPresent());
        assertEquals(Order.OrderStatus.ENTREGADA, result.get().getStatus());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("verifica la actualizacion fallida del estado de una orden inexistente")
    void updateOrderStatusNotFoundTest() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Order> result = orderService.updateOrderStatus(1L, Order.OrderStatus.ENTREGADA);

        // Then
        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }
}