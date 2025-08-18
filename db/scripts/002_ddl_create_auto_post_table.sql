CREATE TABLE post (
    id           serial PRIMARY KEY,
    description  varchar                       NOT NULL,
    created      timestamp                     NOT NULL,
    photo_path   varchar,
    auto_user_id int REFERENCES auto_user (id) NOT NULL
);