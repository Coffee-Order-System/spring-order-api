CREATE TABLE products
(
    product_id   BINARY(16) PRIMARY KEY,
    product_name VARCHAR(50) NOT NULL,
    category     VARCHAR(50) NOT NULL,
    price        BIGINT      NOT NULL,
    description  TEXT,
    created_at   DATETIME(6) NOT NULL,
    updated_at   DATETIME(6) NOT NULL
);

CREATE TABLE orders
(
    order_id     BINARY(16) PRIMARY KEY,
    email        VARCHAR(255) NOT NULL,
    address      VARCHAR(255) NOT NULL,
    postcode     VARCHAR(12)  NOT NULL,
    total_price  BIGINT       NOT NULL,
    order_status VARCHAR(50)  NOT NULL,
    created_at   DATETIME(6) NOT NULL,
    updated_at   DATETIME(6) NOT NULL
);

CREATE TABLE order_items
(
    order_item_id BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_id      BINARY(16) NOT NULL,
    product_id    BINARY(16) NOT NULL,
    category      VARCHAR(50) NOT NULL,
    price         BIGINT      NOT NULL,
    quantity      INT         NOT NULL,
    created_at    DATETIME(6) NOT NULL,
    updated_at    DATETIME(6) NOT NULL,
    INDEX (order_id),
    CONSTRAINT fk_order_items_to_order FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_to_product FOREIGN KEY (product_id) REFERENCES products (product_id)
);