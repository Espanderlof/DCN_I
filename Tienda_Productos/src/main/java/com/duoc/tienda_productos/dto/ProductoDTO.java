package com.duoc.tienda_productos.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String categoria;
    private String imagenUrl;
}