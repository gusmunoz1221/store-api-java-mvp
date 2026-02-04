-- =============================================
--  CATEGORÍAS (Mates, Bombillas, Yerbas)
-- =============================================
INSERT INTO categories (name, description) VALUES
('Mates', 'Mates de todo tipo de material'),
('Bombillas', 'Bombillas de alpaca y acero'),
('Yerbas', 'Yerbas orgánicas y comerciales'),
('Termos', 'Termos de alta durabilidad');

-- =============================================
-- SUBCATEGORÍAS
-- =============================================
INSERT INTO subcategories (name, description, category_id) VALUES
('Calabaza', 'Mates tradicionales de calabaza', 1),
('Madera', 'Mates de algarrobo y palo santo', 1),
('Imperial', 'Mates forrados en cuero con virola', 1);

INSERT INTO subcategories (name, description, category_id) VALUES
('Pico de Loro', 'Bombillas curvas para mejor comodidad', 2),
('Resorte', 'Bombillas con filtro de resorte', 2);

INSERT INTO subcategories (name, description, category_id) VALUES
('Orgánica', 'Sin agroquímicos', 3),
('Compuesta', 'Con hierbas serranas', 3);

-- =============================================
-- PRODUCTOS (10 Items)
-- =============================================
INSERT INTO products (name, description, price, stock, url, subcategory_id) VALUES
('Mate Imperial Premium', 'Mate de calabaza forrado en cuero negro con virola de alpaca', 25000.00, 50, 'http://img.com/imp1.jpg', 3),
('Mate Camionero', 'Mate de boca ancha ideal para yerba uruguaya', 18000.00, 30, 'http://img.com/cam1.jpg', 1),
('Mate Torpedo', 'Forma de torpedo, cuero marrón', 19500.00, 40, 'http://img.com/torp1.jpg', 3),
('Mate Algarrobo Geométrico', 'Mate de madera pintado a mano', 8500.00, 100, 'http://img.com/alg1.jpg', 2),
('Bombilla Pico Loro Alpaca', 'Bombilla 100% alpaca cincelada', 12000.00, 200, 'http://img.com/bomb1.jpg', 4),
('Bombilla Plana Acero', 'Bombilla económica de acero inoxidable', 4500.00, 500, 'http://img.com/bomb2.jpg', 5),
('Yerba Mate "La Selva" 1kg', 'Yerba orgánica estacionamiento natural', 4200.00, 150, 'http://img.com/yerb1.jpg', 6),
('Yerba "Serrana" 500g', 'Con menta y peperina', 2100.00, 120, 'http://img.com/yerb2.jpg', 7),
('Termo Media Manija', 'Termo de acero 1 litro, mantiene 24hs', 65000.00, 20, 'http://img.com/termo1.jpg', NULL), -- Sin subcategoría por ahora
('Set Matero Completo', 'Bolso, mate, yerbero y azucarero', 45000.00, 10, 'http://img.com/set1.jpg', NULL);

-- =============================================
-- CARRITOS
-- =============================================
INSERT INTO carts (session_id, total_amount, created_at) VALUES
('session_user_123', 37000.00, NOW());
INSERT INTO cart_items (cart_id, product_id, quantity, unit_price) VALUES
(1, 1, 1, 25000.00), -- 1 Mate Imperial
(1, 5, 1, 12000.00); -- 1 Bombilla Alpaca
INSERT INTO carts (session_id, total_amount, created_at) VALUES
('session_user_456', 4200.00, NOW());
INSERT INTO cart_items (cart_id, product_id, quantity, unit_price) VALUES
(2, 7, 1, 4200.00); -- 1 Yerba

-- =============================================
-- ORDENES
-- =============================================
INSERT INTO orders (
    order_number, session_id, customer_name, customer_email,
    shipping_address, shipping_city, total_amount, cart_id, status, created_at
) VALUES (
    'ORD-2024-001', 'session_old_999', 'Juan Perez', 'juan@gmail.com',
    'Av. Corrientes 1234', 'CABA', 29200.00, 99, 'DELIVERED', '2025-01-10 10:00:00'
);
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(1, 1, 1, 25000.00), -- Mate Imperial
(1, 7, 1, 4200.00);  -- Yerba

INSERT INTO orders (
    order_number, session_id, customer_name, customer_email,
    shipping_address, shipping_city, total_amount, cart_id, status, created_at
) VALUES (
    'ORD-2024-002', 'session_user_888', 'Maria Gomez', 'maria@hotmail.com',
    'San Martin 500', 'Cordoba', 65000.00, 100, 'PENDING', NOW()
);

INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(2, 9, 1, 65000.00); -- Termo
-- ============================
-- SINCRONIZACIÓN DE SECUENCIAS
-- ============================
SELECT setval(pg_get_serial_sequence('categories', 'id'), COALESCE(MAX(id), 1)) FROM categories;
SELECT setval(pg_get_serial_sequence('subcategories', 'id'), COALESCE(MAX(id), 1)) FROM subcategories;
SELECT setval(pg_get_serial_sequence('products', 'id'), COALESCE(MAX(id), 1)) FROM products;
SELECT setval(pg_get_serial_sequence('carts', 'id'), COALESCE(MAX(id), 1)) FROM carts;
SELECT setval(pg_get_serial_sequence('orders', 'id'), COALESCE(MAX(id), 1)) FROM orders;
SELECT setval(pg_get_serial_sequence('order_items', 'id'), COALESCE(MAX(id), 1)) FROM order_items;