package com.duoc.tienda_ordenes.model;

import com.duoc.tienda_ordenes.model.Order.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusDTO {
    private OrderStatus status;
}
