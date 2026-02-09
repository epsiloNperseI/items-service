CREATE DATABASE items_db;

\c items_db

CREATE TABLE IF NOT EXISTS items (
    id BIGSERIAL PRIMARY KEY,
    name TEXT
);

INSERT INTO items (name) VALUES
    ('Тестовый товар 1'),
    ('Тестовый товар 2'),
    ('Тестовый товар 3')
ON CONFLICT (id) DO NOTHING;
