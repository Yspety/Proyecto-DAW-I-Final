DROP DATABASE IF EXISTS erp_productos;
CREATE DATABASE erp_productos CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE erp_productos;

-- ===================================================
-- TABLAS DE USUARIOS Y ROLES (Adaptadas)
-- ===================================================

CREATE TABLE rol (
    idrol INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(30) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- ========================================
-- TABLA usuario (Sistema de autenticación)
-- ========================================
CREATE TABLE usuario (
    idusu INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    usuario VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    idrol INT NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1, -- Añadido campo 'activo' para consistencia
    creado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Añadido campo 'creado_en' para consistencia
    
    CONSTRAINT FK_RolUsuario FOREIGN KEY (idrol) 
        REFERENCES rol(idrol) -- Corregido: REFERENCIAS a la tabla 'rol'
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ===================================================
-- DATOS BASE: USUARIOS Y ROLES (Adaptados)
-- ===================================================

-- Inserción de Roles
INSERT INTO rol (descripcion) VALUES
('ADMIN'),
('ALMACENERO');

-- Inserción de Usuarios
-- Contraseñas codificadas con BCrypt (cost=10):
--   admin      -> 1234
--   almacenero -> abcd
INSERT INTO usuario (nombre, apellido, usuario, password, idrol, activo) VALUES
('Administrador', 'General', 'admin',
    '$2b$10$MmIhW1U/JMYQTOcb7qk.BemFzDI7ZroqAWSq1ieeM46wdXeiQgp26',
    (SELECT idrol FROM rol WHERE descripcion = 'ADMIN'), 1),
('Juan', 'Pérez', 'almacenero',
    '$2b$10$fPDlFiAcowYak4U92AxMluM.He1y4DK8kU.lgyUIadb1sDRILexxq',
    (SELECT idrol FROM rol WHERE descripcion = 'ALMACENERO'), 1);

-- ===================================================
-- CATEGORÍAS
-- ===================================================

CREATE TABLE categoria (
    categoria_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL,
    descripcion VARCHAR(255),
    activo TINYINT(1) NOT NULL DEFAULT 1,
    ultima_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (categoria_id),
    UNIQUE KEY idx_categoria_nombre (nombre)
) ENGINE=InnoDB;

INSERT INTO categoria (nombre, descripcion, activo) VALUES
('Bebidas', 'Productos líquidos', 1),
('Snacks', 'Aperitivos y galletas', 1),
('Limpieza', 'Artículos de aseo', 1);

-- ===================================================
-- UNIDADES
-- ===================================================

CREATE TABLE unidad (
    unidad_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(10) NOT NULL,
    nombre VARCHAR(40) NOT NULL,
    factor_base DECIMAL(18,6) NOT NULL DEFAULT 1.000000,
    es_base TINYINT(1) NOT NULL DEFAULT 1,
    ultima_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (unidad_id),
    UNIQUE KEY idx_unidad_codigo (codigo)
) ENGINE=InnoDB;

INSERT INTO unidad (codigo, nombre, factor_base, es_base) VALUES
('UND', 'Unidad', 1, 1),
('CAJA', 'Caja', 12, 0);

-- ===================================================
-- PRODUCTOS
-- ===================================================

CREATE TABLE producto (
    producto_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    sku VARCHAR(30) NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    descripcion VARCHAR(500),
    categoria_id SMALLINT UNSIGNED NOT NULL,
    codigo_barras VARCHAR(50),
    precio_lista DECIMAL(18,2),
    activo TINYINT(1) NOT NULL DEFAULT 1,
    creado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultima_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,
    unidad_id SMALLINT UNSIGNED NOT NULL DEFAULT 1,
    PRIMARY KEY (producto_id),
    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (categoria_id) REFERENCES categoria (categoria_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_producto_unidad
        FOREIGN KEY (unidad_id) REFERENCES unidad (unidad_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    UNIQUE KEY idx_producto_sku (sku)
) ENGINE=InnoDB;

INSERT INTO producto (sku, nombre, descripcion, categoria_id, codigo_barras, precio_lista, activo)
VALUES
('BEB-001', 'Agua Mineral 500ml', 'Botella de agua natural', 1, '123456789001', 2.50, 1),
('BEB-002', 'Jugo de Naranja 1L', 'Jugo natural sin azúcar', 1, '123456789002', 5.80, 1),
('SNK-001', 'Galletas de Avena', 'Paquete de 12 unidades', 2, '223456789001', 4.00, 1),
('LMP-001', 'Detergente Líquido 1L', 'Para ropa blanca y de color', 3, '323456789001', 12.90, 1);

-- ===================================================
-- ALMACENES
-- ===================================================

CREATE TABLE almacen (
    almacen_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL,
    tipo ENUM('PRINCIPAL','SECUNDARIO','TEMPORAL') NOT NULL DEFAULT 'PRINCIPAL',
    direccion VARCHAR(180),
    activo TINYINT(1) NOT NULL DEFAULT 1,
    ultima_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (almacen_id)
) ENGINE=InnoDB;

INSERT INTO almacen (nombre, tipo, direccion, activo) VALUES
('Almacén Central', 'PRINCIPAL', 'Av. Industrial 123', 1),
('Depósito Secundario', 'SECUNDARIO', 'Jr. Comercio 456', 1);

-- ===================================================
-- INVENTARIO
-- ===================================================

CREATE TABLE inventario (
    almacen_id SMALLINT UNSIGNED NOT NULL,
    producto_id INT UNSIGNED NOT NULL,
    cantidad DECIMAL(18,6) NOT NULL DEFAULT 0,
    stock_min DECIMAL(18,6) NOT NULL DEFAULT 0,
    stock_max DECIMAL(18,6),
    ultima_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (almacen_id, producto_id),
    FOREIGN KEY (almacen_id) REFERENCES almacen (almacen_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES producto (producto_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

INSERT INTO inventario (almacen_id, producto_id, cantidad, stock_min, stock_max)
VALUES
(1, 1, 5, 10, 100),
(1, 2, 20, 15, 100),
(1, 3, 8, 15, 50),
(2, 4, 1, 5, 30);

-- ===================================================
-- MOVIMIENTOS DE INVENTARIO
-- ===================================================

CREATE TABLE inventario_movimiento (
    inventario_movimiento_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    fecha_movimiento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    almacen_id SMALLINT UNSIGNED NOT NULL,
    producto_id INT UNSIGNED NOT NULL,
    tipo_movimiento ENUM('ENTRADA','SALIDA','AJUSTE') NOT NULL,
    cantidad DECIMAL(18,6) NOT NULL,
    costo_unitario DECIMAL(18,6),
    referencia VARCHAR(100),
    usuario VARCHAR(60), -- Nota: Este campo se podría usar para guardar el ID o el nombre de usuario que realiza el movimiento
    ultima_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (inventario_movimiento_id),
    FOREIGN KEY (almacen_id) REFERENCES almacen (almacen_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (producto_id) REFERENCES producto (producto_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;