CREATE TABLE car (
    id        serial PRIMARY KEY,
    name      text,
    engine_id int NOT NULL REFERENCES engine (id)
);