CREATE TABLE participates (
    id      serial PRIMARY KEY,
    user_id int NOT NULL REFERENCES auto_user (id),
    post_id int NOT NULL REFERENCES auto_post (id),
    UNIQUE (user_id, post_id)
);