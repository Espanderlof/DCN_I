DROP TABLE IF EXISTS orden_items;
DROP TABLE IF EXISTS ordenes;

CREATE TABLE ordenes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    total DECIMAL(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP,
    ultima_actualizacion TIMESTAMP
);

CREATE TABLE orden_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(19,2) NOT NULL,
    subtotal DECIMAL(19,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES ordenes(id)
);