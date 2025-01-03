package com.duoc.tienda_productos.config;

import com.duoc.tienda_productos.model.Producto;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductoCache {
    private static volatile ProductoCache instance;
    private final Map<Long, Producto> cache;
    private static final int CACHE_SIZE = 100;

    private ProductoCache() {
        cache = new ConcurrentHashMap<>();
    }

    public static ProductoCache getInstance() {
        if (instance == null) {
            synchronized (ProductoCache.class) {
                if (instance == null) {
                    instance = new ProductoCache();
                }
            }
        }
        return instance;
    }

    public void addProducto(Producto producto) {
        if (cache.size() >= CACHE_SIZE) {
            Optional<Long> firstKey = cache.keySet().stream().findFirst();
            firstKey.ifPresent(cache::remove);
        }
        cache.put(producto.getId(), producto);
    }

    public Optional<Producto> getProducto(Long id) {
        return Optional.ofNullable(cache.get(id));
    }

    public List<Producto> getProductosPorCategoria(String categoria) {
        return cache.values().stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    public List<Producto> getProductosPorNombre(String nombre) {
        return cache.values().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void removeProducto(Long id) {
        cache.remove(id);
    }

    public void clearCache() {
        cache.clear();
    }

    public int getCacheSize() {
        return cache.size();
    }

    public Map<Long, Producto> getAllCachedProducts() {
        return new ConcurrentHashMap<>(cache);
    }
}