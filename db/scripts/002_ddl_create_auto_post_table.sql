CREATE TABLE auto_post (
    id   serial PRIMARY KEY,
    description varchar NOT NULL,
    created timestamp NOT NULL,
    auto_user_id int references auto_user(id) NOT NULL
);