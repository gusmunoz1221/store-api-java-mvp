-- ================================
-- USERS (ADMINS)
-- ================================
INSERT INTO users (firstname, lastname, email, password, role)
SELECT
    'Admin',
    'Principal',
    'admin@store.com',
    '$2a$12$T.b7gzauG7VT3Zh5Xiuv7umQjdDvkqkWuNiwRVZ47/OojQI7LpO6W',
    'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@store.com'
);

-- ================================
-- CATEGORIES
-- ================================
INSERT INTO categories (name, description)
SELECT 'Yerba Mate', 'Yerbas tradicionales y saborizadas'
WHERE NOT EXISTS (
    SELECT 1 FROM categories WHERE name = 'Yerba Mate'
);

-- ================================
-- SUBCATEGORIES
-- ================================
INSERT INTO subcategories (name, description, category_id)
SELECT
    'Premium',
    'Yerbas premium seleccionadas',
    c.id
FROM categories c
WHERE c.name = 'Yerba Mate'
AND NOT EXISTS (
    SELECT 1 FROM subcategories WHERE name = 'Premium'
);

-- ================================
-- PRODUCTS
-- ================================
INSERT INTO products (name, price, description, url, stock, subcategory_id)
SELECT
    'Yerba Mate Canarias 1kg',
    45000.00,
    'Yerba mate uruguaya premium',
    'https://example.com/canarias.jpg',
    10,
    s.id
FROM subcategories s
WHERE s.name = 'Premium'
AND NOT EXISTS (
    SELECT 1 FROM products WHERE name = 'Yerba Mate Canarias 1kg'
);

-- ================================
-- CARTS
-- ================================
INSERT INTO carts (session_id, total_amount)
SELECT
    'sess_mate_001',
    45000.00
WHERE NOT EXISTS (
    SELECT 1 FROM carts WHERE session_id = 'sess_mate_001'
);

-- ================================
-- CART ITEMS
-- ================================
INSERT INTO cart_items (quantity, unit_price, cart_id, product_id)
SELECT
    1,
    45000.00,
    c.id,
    p.id
FROM carts c
JOIN products p ON p.name = 'Yerba Mate Canarias 1kg'
WHERE c.session_id = 'sess_mate_001'
AND NOT EXISTS (
    SELECT 1
    FROM cart_items ci
    WHERE ci.cart_id = c.id
      AND ci.product_id = p.id
);

-- ================================
-- ORDERS
-- ================================
INSERT INTO orders (
    customer_name,
    customer_email,
    customer_phone,
    shipping_address,
    shipping_city,
    shipping_zip,
    total_amount,
    cart_id,
    status
)
SELECT
    'Facundo Arana',
    'facu@mate.com',
    NULL,
    'Av. Corrientes 1234',
    'CABA',
    '1043',
    45000.00,
    c.id,
    'PAID'
FROM carts c
WHERE c.session_id = 'sess_mate_001'
AND NOT EXISTS (
    SELECT 1 FROM orders WHERE customer_email = 'facu@mate.com'
);

-- ================================
-- ORDER ITEMS
-- ================================
INSERT INTO order_items (quantity, price, product_id, order_id)
SELECT
    1,
    45000.00,
    p.id,
    o.id
FROM orders o
JOIN carts c ON o.cart_id = c.id
JOIN products p ON p.name = 'Yerba Mate Canarias 1kg'
WHERE o.customer_email = 'facu@mate.com'
AND NOT EXISTS (
    SELECT 1
    FROM order_items oi
    WHERE oi.order_id = o.id
      AND oi.product_id = p.id
);



-- 5, 6, 7, 8, 9 y 10. ADICIONAL: PRODUCTS----------|||||||||||||||||||||||||||||||||||||||||||||||||||||------------------

-- ================================
-- 1. ADICIONAL: CATEGORIES
-- ================================
INSERT INTO categories (name, description)
SELECT 'Accesorios', 'Mates, bombillas y equipos de mate'
WHERE NOT EXISTS (
    SELECT 1 FROM categories WHERE name = 'Accesorios'
);

-- ================================
-- ================================
-- ================================

-- Subcategoría: Suave (dentro de Yerba Mate)
INSERT INTO subcategories (name, description, category_id)
SELECT 'Suave', 'Yerbas de sabor amable y molienda equilibrada', c.id
FROM categories c WHERE c.name = 'Yerba Mate'
AND NOT EXISTS (SELECT 1 FROM subcategories WHERE name = 'Suave');

-- Subcategoría: Mates (dentro de Accesorios)
INSERT INTO subcategories (name, description, category_id)
SELECT 'Mates', 'Recipientes de calabaza, madera y cerámica', c.id
FROM categories c WHERE c.name = 'Accesorios'
AND NOT EXISTS (SELECT 1 FROM subcategories WHERE name = 'Mates');

-- Subcategoría: Bombillas (dentro de Accesorios)
INSERT INTO subcategories (name, description, category_id)
SELECT 'Bombillas', 'Bombillas de alpaca, acero y caña', c.id
FROM categories c WHERE c.name = 'Accesorios'
AND NOT EXISTS (SELECT 1 FROM subcategories WHERE name = 'Bombillas');

-- ================================
-- ================================

-- 5. Yerba Playadito
INSERT INTO products (name, price, description, url, stock, subcategory_id)
SELECT 'Yerba Mate Playadito 1kg', 32000.00, 'Tradicional suave de Colonia Liebig', 'https://example.com/playadito.jpg', 50, s.id
FROM subcategories s WHERE s.name = 'Suave'
AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Yerba Mate Playadito 1kg');

-- 6. Yerba Rosamonte (Usa subcategoría Premium que ya tenías)
INSERT INTO products (name, price, description, url, stock, subcategory_id)
SELECT 'Yerba Mate Rosamonte Plus', 38000.00, 'Selección especial 12 meses estacionamiento', 'https://example.com/rosamonte.jpg', 20, s.id
FROM subcategories s WHERE s.name = 'Premium'
AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Yerba Mate Rosamonte Plus');

-- 7. Yerba CBSé
INSERT INTO products (name, price, description, url, stock, subcategory_id)
SELECT 'Yerba Mate CBSé Hierbas', 28000.00, 'Ideal para el mate de la tarde', 'https://example.com/cbse.jpg', 60, s.id
FROM subcategories s WHERE s.name = 'Suave'
AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Yerba Mate CBSé Hierbas');

-- 8. Mate Imperial
INSERT INTO products (name, price, description, url, stock, subcategory_id)
SELECT 'Mate Imperial Calabaza', 85000.00, 'Cuero vaqueta y virola de alpaca cincelada', 'https://example.com/imperial.jpg', 5, s.id
FROM subcategories s WHERE s.name = 'Mates'
AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Mate Imperial Calabaza');

-- 9. Mate Camionero (Este reemplaza al User para llegar a 10)
INSERT INTO products (name, price, description, url, stock, subcategory_id)
SELECT 'Mate Camionero Uruguayo', 72000.00, 'Boca ancha ideal para yerbas sin palo', 'https://example.com/camionero.jpg', 8, s.id
FROM subcategories s WHERE s.name = 'Mates'
AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Mate Camionero Uruguayo');

-- 10. Bombilla Pico de Loro
INSERT INTO products (name, price, description, url, stock, subcategory_id)
SELECT 'Bombilla Pico de Loro', 15000.00, 'Acero inoxidable reforzado', 'https://example.com/picoloro.jpg', 30, s.id
FROM subcategories s WHERE s.name = 'Bombillas'
AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Bombilla Pico de Loro');
-- ============================
-- SINCRONIZACIÓN DE SECUENCIAS
-- ============================
SELECT setval(pg_get_serial_sequence('categories', 'id'), COALESCE(MAX(id), 1)) FROM categories;
SELECT setval(pg_get_serial_sequence('subcategories', 'id'), COALESCE(MAX(id), 1)) FROM subcategories;
SELECT setval(pg_get_serial_sequence('products', 'id'), COALESCE(MAX(id), 1)) FROM products;
SELECT setval(pg_get_serial_sequence('carts', 'id'), COALESCE(MAX(id), 1)) FROM carts;
SELECT setval(pg_get_serial_sequence('orders', 'id'), COALESCE(MAX(id), 1)) FROM orders;
SELECT setval(pg_get_serial_sequence('order_items', 'id'), COALESCE(MAX(id), 1)) FROM order_items;