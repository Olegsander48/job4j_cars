CREATE TABLE owners (
    id      serial PRIMARY KEY,
    name    text,
    user_id int NOT NULL UNIQUE REFERENCES auto_user (id)
);