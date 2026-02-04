-- 1. Categorías y Subcategorías
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255)
);

CREATE TABLE subcategories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    category_id BIGINT REFERENCES categories(id)
);

-- 2. Productos
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    price NUMERIC(38, 2),
    description VARCHAR(255),
    url VARCHAR(255),
    stock INTEGER NOT NULL,
    subcategory_id BIGINT REFERENCES subcategories(id)
);

-- 3. Carritos
CREATE TABLE carts (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) NOT NULL UNIQUE,
    total_amount NUMERIC(38, 2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cart_items (
    id BIGSERIAL PRIMARY KEY,
    quantity INTEGER,
    unit_price NUMERIC(38, 2),
    cart_id BIGINT NOT NULL REFERENCES carts(id),
    product_id BIGINT NOT NULL REFERENCES products(id)
);

-- 4. Ordenes (Pedidos)
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    customer_name VARCHAR(255),
    customer_email VARCHAR(255),
    customer_phone VARCHAR(255),
    order_number VARCHAR(255) NOT NULL,
    session_id VARCHAR(255) NOT NULL,
    shipping_address VARCHAR(255),
    shipping_city VARCHAR(255),
    shipping_zip VARCHAR(255),
    total_amount NUMERIC(38, 2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL -- Enum como String
);

CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    quantity INTEGER NOT NULL,
    price NUMERIC(38, 2),
    product_id BIGINT NOT NULL REFERENCES products(id),
    order_id BIGINT NOT NULL REFERENCES orders(id)
);

-- ================================
-- ADMINS
-- ================================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);