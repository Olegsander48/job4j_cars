CREATE TABLE advertisement (
    id   serial PRIMARY KEY,
    car_brand varchar NOT NULL,
    body_type varchar NOT NULL,
    photo_id int
);