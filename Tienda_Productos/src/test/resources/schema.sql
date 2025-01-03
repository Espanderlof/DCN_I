DROP TABLE IF EXISTS productos;

CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    precio DECIMAL(19,2) NOT NULL,
    stock INTEGER NOT NULL,
    categoria VARCHAR(255) NOT NULL,
    imagen_url VARCHAR(255),
    activo BOOLEAN DEFAULT true NOT NULL,
    fecha_creacion TIMESTAMP,
    ultima_actualizacion TIMESTAMP
);

-- Datos de prueba
INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo, fecha_creacion, ultima_actualizacion)
VALUES 
('Producto Test Activo', 'Descripción test', 100.00, 10, 'Test', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Producto Test Inactivo', 'Descripción test', 200.00, 5, 'Test', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);