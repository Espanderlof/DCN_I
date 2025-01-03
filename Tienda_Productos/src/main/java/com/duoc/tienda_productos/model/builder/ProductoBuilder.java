package com.duoc.tienda_productos.model.builder;

import com.duoc.tienda_productos.model.Producto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductoBuilder {
    private Producto producto;

    public ProductoBuilder() {
        producto = new Producto();
    }

    public ProductoBuilder conNombre(String nombre) {
        producto.setNombre(nombre);
        return this;
    }

    public ProductoBuilder conDescripcion(String descripcion) {
        producto.setDescripcion(descripcion);
        return this;
    }

    public ProductoBuilder conPrecio(BigDecimal precio) {
        producto.setPrecio(precio);
        return this;
    }

    public ProductoBuilder conStock(Integer stock) {
        producto.setStock(stock);
        return this;
    }

    public ProductoBuilder conCategoria(String categoria) {
        producto.setCategoria(categoria);
        return this;
    }

    public ProductoBuilder conImagenUrl(String imagenUrl) {
        producto.setImagenUrl(imagenUrl);
        return this;
    }

    public ProductoBuilder activo(Boolean activo) {
        producto.setActivo(activo);
        return this;
    }

    public Producto build() {
        producto.setFechaCreacion(LocalDateTime.now());
        producto.setUltimaActualizacion(LocalDateTime.now());
        return producto;
    }
}