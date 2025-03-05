CREATE SCHEMA x6order;

CREATE TABLE x6order.order (
    id SERIAL PRIMARY KEY,
    userId INT NOT NULL,
    productId INT NOT NULL,
    quantity INT NOT NULL
);

CREATE TABLE x6order.meta_order_create (
    id INT NOT NULL PRIMARY KEY REFERENCES x6order.order(id),
    create_date TIMESTAMP WITH TIME ZONE
);

CREATE TABLE x6order.meta_order_update (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES x6order.order(id),
    update_date TIMESTAMP WITH TIME ZONE
);