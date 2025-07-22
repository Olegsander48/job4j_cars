CREATE TABLE auto_user (
    id   serial PRIMARY KEY,
    login varchar NOT NULL UNIQUE,
    password varchar NOT NULL
);