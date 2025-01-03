package com.duoc.tienda_productos.service;

import com.duoc.tienda_productos.model.Producto;
import com.duoc.tienda_productos.repository.ProductoRepository;
import com.duoc.tienda_productos.dto.ProductoDTO;
import com.duoc.tienda_productos.model.builder.ProductoBuilder;
import com.duoc.tienda_productos.config.ProductoCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    private final ProductoCache productoCache = ProductoCache.getInstance();

    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = productoRepository.findByActivoTrue();
        // Actualiza el caché con todos los productos
        productos.forEach(productoCache::addProducto);
        return productos;
    }

    public Optional<Producto> obtenerProductoPorId(Long id) {
        // Primero busca en el caché
        Optional<Producto> productoCache = this.productoCache.getProducto(id);
        if (productoCache.isPresent()) {
            return productoCache.filter(Producto::getActivo);
        }

        // Si no está en caché, busca en la base de datos
        Optional<Producto> productoDB = productoRepository.findById(id)
                .filter(Producto::getActivo);
        
        // Si se encuentra en la DB, añádelo al caché
        productoDB.ifPresent(this.productoCache::addProducto);
        
        return productoDB;
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        // Obtén los productos de la base de datos
        List<Producto> productos = productoRepository.findByCategoria(categoria);
        
        // Actualiza el caché con los productos encontrados
        productos.forEach(productoCache::addProducto);
        
        return productos;
    }

    public List<Producto> buscarPorNombre(String nombre) {
        // Obtén los productos de la base de datos
        List<Producto> productos = productoRepository.findByNombreContainingIgnoreCase(nombre);
        
        // Actualiza el caché con los productos encontrados
        productos.forEach(productoCache::addProducto);
        
        return productos;
    }

    public Producto crearProducto(ProductoDTO productoDTO) {
        Producto producto = new ProductoBuilder()
            .conNombre(productoDTO.getNombre())
            .conDescripcion(productoDTO.getDescripcion())
            .conPrecio(productoDTO.getPrecio())
            .conStock(productoDTO.getStock())
            .conCategoria(productoDTO.getCategoria())
            .conImagenUrl(productoDTO.getImagenUrl())
            .activo(true)
            .build();
        
        Producto productoGuardado = productoRepository.save(producto);
        productoCache.addProducto(productoGuardado);
        return productoGuardado;
    }

    public Optional<Producto> actualizarProducto(Long id, ProductoDTO productoDTO) {
        return productoRepository.findById(id)
            .map(productoExistente -> {
                Producto productoActualizado = new ProductoBuilder()
                    .conNombre(productoDTO.getNombre() != null ? productoDTO.getNombre() : productoExistente.getNombre())
                    .conDescripcion(productoDTO.getDescripcion() != null ? productoDTO.getDescripcion() : productoExistente.getDescripcion())
                    .conPrecio(productoDTO.getPrecio() != null ? productoDTO.getPrecio() : productoExistente.getPrecio())
                    .conStock(productoDTO.getStock() != null ? productoDTO.getStock() : productoExistente.getStock())
                    .conCategoria(productoDTO.getCategoria() != null ? productoDTO.getCategoria() : productoExistente.getCategoria())
                    .conImagenUrl(productoDTO.getImagenUrl() != null ? productoDTO.getImagenUrl() : productoExistente.getImagenUrl())
                    .activo(productoExistente.getActivo())
                    .build();
                productoActualizado.setId(id);
                Producto savedProduct = productoRepository.save(productoActualizado);
                productoCache.addProducto(savedProduct);
                return savedProduct;
            });
    }

    public Optional<Producto> actualizarStock(Long id, Integer cantidad) {
        return productoRepository.findById(id)
            .map(producto -> {
                producto.setStock(cantidad);
                Producto savedProduct = productoRepository.save(producto);
                productoCache.addProducto(savedProduct);
                return savedProduct;
            });
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
        productoCache.removeProducto(id);
    }
}