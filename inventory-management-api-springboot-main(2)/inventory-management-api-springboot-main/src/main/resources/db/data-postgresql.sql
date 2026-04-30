INSERT INTO rol (descripcion) VALUES ('ADMIN'), ('ALMACENERO') ON CONFLICT (descripcion) DO NOTHING;

INSERT INTO usuario (nombre, apellido, usuario, password, idrol, activo) VALUES
('Administrador', 'General', 'admin', '$2b$10$MmIhW1U/JMYQTOcb7qk.BemFzDI7ZroqAWSq1ieeM46wdXeiQgp26', (SELECT idrol FROM rol WHERE descripcion = 'ADMIN'), TRUE),
('Juan', 'Pérez', 'almacenero', '$2b$10$fPDlFiAcowYak4U92AxMluM.He1y4DK8kU.lgyUIadb1sDRILexxq', (SELECT idrol FROM rol WHERE descripcion = 'ALMACENERO'), TRUE)
ON CONFLICT (usuario) DO NOTHING;

INSERT INTO categoria (nombre, descripcion, activo) VALUES
('Bebidas', 'Productos líquidos', TRUE),
('Snacks', 'Aperitivos y galletas', TRUE),
('Limpieza', 'Artículos de aseo', TRUE)
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO unidad (codigo, nombre, factor_base, es_base) VALUES
('UND', 'Unidad', 1, TRUE),
('CAJA', 'Caja', 12, FALSE)
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO producto (sku, nombre, descripcion, categoria_id, codigo_barras, precio_lista, activo, unidad_id) VALUES
('BEB-001', 'Agua Mineral 500ml', 'Botella de agua natural', 1, '123456789001', 2.50, TRUE, 1),
('BEB-002', 'Jugo de Naranja 1L', 'Jugo natural sin azúcar', 1, '123456789002', 5.80, TRUE, 1),
('SNK-001', 'Galletas de Avena', 'Paquete de 12 unidades', 2, '223456789001', 4.00, TRUE, 1),
('LMP-001', 'Detergente Líquido 1L', 'Para ropa blanca y de color', 3, '323456789001', 12.90, TRUE, 1)
ON CONFLICT (sku) DO NOTHING;

INSERT INTO almacen (almacen_id, nombre, tipo, direccion, activo) VALUES
(1, 'Almacén Central', 'PRINCIPAL', 'Av. Industrial 123', TRUE),
(2, 'Depósito Secundario', 'SECUNDARIO', 'Jr. Comercio 456', TRUE)
ON CONFLICT (almacen_id) DO NOTHING;

SELECT setval(pg_get_serial_sequence('almacen', 'almacen_id'), COALESCE((SELECT MAX(almacen_id) FROM almacen), 1), TRUE);

INSERT INTO inventario (almacen_id, producto_id, cantidad, stock_min, stock_max) VALUES
(1, 1, 5, 10, 100),
(1, 2, 20, 15, 100),
(1, 3, 8, 15, 50),
(2, 4, 1, 5, 30)
ON CONFLICT (almacen_id, producto_id) DO NOTHING;
