INSERT INTO usuarios (nombre, apellido, email, password, role, telefono, direccion, fecha_creacion, ultima_actualizacion, activo)
VALUES ('Admin', 'Sistema', 'admin@tienda.com', 'admin123', 'ADMIN', '+56912345678', 'Oficina Central', SYSTIMESTAMP, SYSTIMESTAMP, 1);

INSERT INTO usuarios (nombre, apellido, email, password, role, telefono, direccion, fecha_creacion, ultima_actualizacion, activo)
VALUES ('Juan', 'Pérez', 'juan@email.com', 'cliente123', 'CLIENTE', '+56987654321', 'Av. Principal 123', SYSTIMESTAMP, SYSTIMESTAMP, 1);

INSERT INTO usuarios (nombre, apellido, email, password, role, telefono, direccion, fecha_creacion, ultima_actualizacion, activo)
VALUES ('María', 'González', 'maria@email.com', 'cliente456', 'CLIENTE', '+56976543210', 'Calle Secundaria 456', SYSTIMESTAMP, SYSTIMESTAMP, 1);