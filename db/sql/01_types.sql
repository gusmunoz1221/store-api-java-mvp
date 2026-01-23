-- ================================
-- ORDER STATUS ENUM
-- ================================
CREATE TYPE order_status AS ENUM (
    'PENDING',
    'PAID',
    'CANCELLED',
    'SHIPPED'
);
