-- FEATURE-2: Create orders

CREATE TABLE orders (
    id UUID PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_qty INTEGER NOT NULL,
    product_price DOUBLE PRECISION NOT NULL,
    product_description TEXT,
    created_time TIMESTAMP NOT NULL,
    updated_time TIMESTAMP NOT NULL
);

-- Index for searching by product name
CREATE INDEX idx_order_product_name ON orders (product_name);

-- Index for searching by creation time
CREATE INDEX idx_order_created_time ON orders (created_time);
