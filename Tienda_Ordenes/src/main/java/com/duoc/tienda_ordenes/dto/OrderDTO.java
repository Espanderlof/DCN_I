package com.duoc.tienda_ordenes.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    private Long userId;
    private String userName;
    private List<OrderItemDTO> items;
    private BigDecimal total;
}
