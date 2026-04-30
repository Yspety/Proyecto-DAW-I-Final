CREATE TABLE IF NOT EXISTS rol (
    idrol SERIAL PRIMARY KEY,
    descripcion VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS usuario (
    idusu SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    usuario VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    idrol INT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rolusuario FOREIGN KEY (idrol) REFERENCES rol(idrol) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS categoria (
    categoria_id SMALLSERIAL PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    ultima_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS unidad (
    unidad_id SMALLSERIAL PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(40) NOT NULL,
    factor_base NUMERIC(18,6) NOT NULL DEFAULT 1.000000,
    es_base BOOLEAN NOT NULL DEFAULT TRUE,
    ultima_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS producto (
    producto_id SERIAL PRIMARY KEY,
    sku VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(120) NOT NULL,
    descripcion VARCHAR(500),
    categoria_id SMALLINT NOT NULL,
    codigo_barras VARCHAR(50),
    precio_lista NUMERIC(18,2),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultima_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    unidad_id SMALLINT NOT NULL DEFAULT 1,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(categoria_id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_producto_unidad FOREIGN KEY (unidad_id) REFERENCES unidad(unidad_id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS almacen (
    almacen_id SMALLSERIAL PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    tipo VARCHAR(20) NOT NULL DEFAULT 'PRINCIPAL' CHECK (tipo IN ('PRINCIPAL','SECUNDARIO','TEMPORAL')),
    direccion VARCHAR(180),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    ultima_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS inventario (
    almacen_id SMALLINT NOT NULL,
    producto_id INT NOT NULL,
    cantidad NUMERIC(18,6) NOT NULL DEFAULT 0,
    stock_min NUMERIC(18,6) NOT NULL DEFAULT 0,
    stock_max NUMERIC(18,6),
    ultima_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (almacen_id, producto_id),
    FOREIGN KEY (almacen_id) REFERENCES almacen(almacen_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES producto(producto_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS inventario_movimiento (
    inventario_movimiento_id BIGSERIAL PRIMARY KEY,
    fecha_movimiento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    almacen_id SMALLINT NOT NULL,
    producto_id INT NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL CHECK (tipo_movimiento IN ('ENTRADA','SALIDA','AJUSTE')),
    cantidad NUMERIC(18,6) NOT NULL,
    costo_unitario NUMERIC(18,6),
    referencia VARCHAR(100),
    usuario VARCHAR(60),
    ultima_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT inventario_movimiento_ibfk_1 FOREIGN KEY (almacen_id) REFERENCES almacen(almacen_id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT inventario_movimiento_ibfk_2 FOREIGN KEY (producto_id) REFERENCES producto(producto_id) ON UPDATE CASCADE ON DELETE RESTRICT
);
