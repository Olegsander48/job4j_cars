CREATE TABLE auto_user (
    id       serial PRIMARY KEY,
    login    varchar NOT NULL UNIQUE,
    name     varchar NOT NULL,
    PASSWORD VARCHAR NOT NULL
);