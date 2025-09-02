CREATE TABLE car (
    id        serial PRIMARY KEY,
    brand     text,
    model     text,
    car_body  text,
    engine_id int NOT NULL REFERENCES engine (id)
);