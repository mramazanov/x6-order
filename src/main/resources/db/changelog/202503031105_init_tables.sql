CREATE SCHEMA x6order;

CREATE TABLE x6order.order (
    id SERIAL PRIMARY KEY,
    userId INT NOT NULL,
    create_date TIMESTAMP WITH TIME ZONE,
    update_date TIMESTAMP WITH TIME ZONE
);

CREATE TABLE x6order.products (
    id SERIAL PRIMARY KEY,
    order_id INT NOT NULL REFERENCES x6order.order(id),
    product_id INT NOT NULL,
    quantity INT NOT NULL
)