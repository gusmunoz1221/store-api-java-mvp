-- ==================================================================
-- 1. CATEGORÍAS
-- ==================================================================
INSERT INTO categories (name, description) VALUES
('Mates', 'Mates de calabaza, madera y vidrio'),
('Bombillas', 'Bombillas de alpaca, acero y bronce'),
('Yerbas', 'Yerbas orgánicas, compuestas y tradicionales'),
('Termos', 'Termos de acero inoxidable de alta retención'),
('Accesorios', 'Materas, yerberas y kits de limpieza');

-- ==================================================================
-- 2. SUBCATEGORÍAS
-- ==================================================================
-- Mates (ID 1)
INSERT INTO subcategories (name, description, category_id) VALUES
('Imperiales', 'Forrados en cuero con virola de alpaca', 1),
('Camioneros', 'Boca ancha, ideales para montañita', 1),
('Torpedos', 'Forma clásica uruguaya', 1),
('De Vidrio', 'Mates modernos forrados en cuero', 1);

-- Bombillas (ID 2)
INSERT INTO subcategories (name, description, category_id) VALUES
('Pico de Loro', 'Curvas para mayor comodidad', 2),
('Resorte', 'Filtro clásico de resorte', 2),
('Bombillón', 'Para mates grandes y yerba sin palo', 2);

-- Yerbas (ID 3)
INSERT INTO subcategories (name, description, category_id) VALUES
('Orgánica', 'Cultivo sin agroquímicos', 3),
('Barbacuá', 'Secado artesanal con leña', 3),
('Suave', 'Bajo contenido de polvo', 3);

-- ==================================================================
-- 3. PRODUCTOS (Más variedad)
-- ==================================================================
INSERT INTO products (name, description, price, stock, url, subcategory_id) VALUES
-- Mates
('Imperial Premium Negro', 'Calabaza gruesa, cuero vaqueta negro, virola cincelada', 35000.00, 25, 'http://img.com/imp-negro.jpg', 1),
('Imperial Croco Marrón', 'Cuero grabado tipo cocodrilo, color habano', 38500.00, 15, 'http://img.com/imp-croco.jpg', 1),
('Camionero Algarrobo', 'Madera de algarrobo torneada, virola acero', 18000.00, 50, 'http://img.com/cam-madera.jpg', 2),
('Torpedo Cincelado', 'Virola con guarda pampa', 29000.00, 30, 'http://img.com/torp-cincel.jpg', 3),
('Mate Vidrio Urbano', 'Interior de vidrio, exterior cuerina roja', 12000.00, 100, 'http://img.com/vidrio-rojo.jpg', 4),

-- Bombillas
('Bombilla Alpaca Rey', 'Caño grueso, filtro pala, 100% alpaca', 22000.00, 80, 'http://img.com/bomb-alpaca.jpg', 5),
('Pico de Loro Acero', 'Acero inoxidable 304, inalterable', 9500.00, 200, 'http://img.com/pico-acero.jpg', 5),
('Bombillón Uruguayo', 'Ideal para yerba canarias', 14500.00, 60, 'http://img.com/bombillon.jpg', 7),

-- Yerbas
('Yerba "La Selva" 1kg', 'Orgánica estacionamiento 24 meses', 5200.00, 150, 'http://img.com/yerba-selva.jpg', 8),
('Yerba "Ahhumada" 500g', 'Sabor barbacuá intenso', 3100.00, 90, 'http://img.com/yerba-barb.jpg', 9),
('Yerba "Suavecita" 1kg', 'Ideal para principiantes', 4500.00, 300, 'http://img.com/yerba-suave.jpg', 10),

-- Termos
('Termo 1.2L Manija', 'Pico cebador de alta precisión', 68000.00, 40, 'http://img.com/termo-12.jpg', NULL),
('Termo Bala 1L', 'Clásico, tapón doble acción', 42000.00, 60, 'http://img.com/termo-bala.jpg', NULL);

-- ==================================================================
-- 4. CARRITOS (Sessions UUID)
-- ==================================================================

-- Carrito 1: Usuario que abandono la compra (GUEST)
INSERT INTO carts (session_id, total_amount, created_at) VALUES
('session-5184df85-daf6-48a0-8c8b-49ffe9a19153', 40200.00, '2026-02-01 14:30:00');

INSERT INTO cart_items (cart_id, product_id, quantity, unit_price) VALUES
(1, 1, 1, 35000.00), -- Imperial
(1, 9, 1, 5200.00);  -- Yerba

-- Carrito 2: Usuario recurrente (Logueado o Guest recurrente)
INSERT INTO carts (session_id, total_amount, created_at) VALUES
('session-9f8c1234-a1b2-c3d4-e5f6-7890abcdef12', 14500.00, '2026-02-10 09:00:00');

INSERT INTO cart_items (cart_id, product_id, quantity, unit_price) VALUES
(2, 8, 1, 14500.00); -- Bombillón

-- ==================================================================
-- 5. ORDENES (Año 2026 - Estados ENUM)
-- ==================================================================

-- Orden 1: Completada y enviada (SHIPPED)
INSERT INTO orders (
    order_number, session_id, customer_name, customer_email,
    shipping_address, shipping_city, shipping_zip, total_amount, status, created_at
) VALUES (
    'ORD-2026-001',
    'session-5184df85-daf6-48a0-8c8b-49ffe9a19153', -- Mismo session del carrito abandonado (volvió y compró)
    'Juan Perez', 'juan.perez@email.com',
    'Av. Corrientes 1234, 5B', 'CABA', '1045',
    73200.00,
    'SHIPPED',
    '2026-01-15 10:30:00'
);
-- Items Orden 1
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(1, 1, 1, 35000.00), -- Imperial
(1, 12, 1, 68000.00); -- Termo

-- Orden 2: Pagada pero no enviada (PAID)
INSERT INTO orders (
    order_number, session_id, customer_name, customer_email,
    shipping_address, shipping_city, shipping_zip, total_amount, status, created_at
) VALUES (
    'ORD-2026-002',
    'session-7c9e6679-7425-40de-944b-e07fc1f90ae7',
    'Maria Gonzalez', 'maria.gonzalez@hotmail.com',
    'San Martin 400', 'Córdoba', '5000',
    18000.00,
    'PAID',
    '2026-02-09 18:45:00'
);
-- Items Orden 2
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(2, 3, 1, 18000.00); -- Camionero Algarrobo

-- Orden 3: Cancelada por el usuario (CANCELLED)
INSERT INTO orders (
    order_number, session_id, customer_name, customer_email,
    shipping_address, shipping_city, shipping_zip, total_amount, status, created_at
) VALUES (
    'ORD-2026-003',
    'session-3b3206e9-22c6-4d56-8051-229641775e54',
    'Lucas Rodriguez', 'lucas.rod@gmail.com',
    'Belgrano 202', 'Rosario', '2000',
    42000.00,
    'CANCELLED',
    '2026-02-05 11:20:00'
);
-- Items Orden 3
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(3, 13, 1, 42000.00); -- Termo Bala

-- Orden 4: Reciente, pendiente de pago (PENDING)
INSERT INTO orders (
    order_number, session_id, customer_name, customer_email,
    shipping_address, shipping_city, shipping_zip, total_amount, status, created_at
) VALUES (
    'ORD-2026-004',
    'session-9f8c1234-a1b2-c3d4-e5f6-7890abcdef12',
    'Sofia Martinez', 'sofi.mar@outlook.com',
    'Mitre 850', 'Mendoza', '5500',
    14500.00,
    'PENDING',
    NOW() -- Fecha actual real del sistema
);
-- Items Orden 4
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(4, 8, 1, 14500.00); -- Bombillón

-- ==================================================================
-- 6. SINCRONIZACIÓN (Para que los IDs autoincrementales sigan desde aquí)
-- ==================================================================
SELECT setval(pg_get_serial_sequence('categories', 'id'), COALESCE(MAX(id), 1)) FROM categories;
SELECT setval(pg_get_serial_sequence('subcategories', 'id'), COALESCE(MAX(id), 1)) FROM subcategories;
SELECT setval(pg_get_serial_sequence('products', 'id'), COALESCE(MAX(id), 1)) FROM products;
SELECT setval(pg_get_serial_sequence('carts', 'id'), COALESCE(MAX(id), 1)) FROM carts;
SELECT setval(pg_get_serial_sequence('cart_items', 'id'), COALESCE(MAX(id), 1)) FROM cart_items;
SELECT setval(pg_get_serial_sequence('orders', 'id'), COALESCE(MAX(id), 1)) FROM orders;
SELECT setval(pg_get_serial_sequence('order_items', 'id'), COALESCE(MAX(id), 1)) FROM order_items;