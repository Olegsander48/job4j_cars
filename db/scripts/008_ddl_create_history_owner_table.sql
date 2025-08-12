CREATE TABLE history_owner (
    owner_id int NOT NULL REFERENCES owners (id),
    car_id   int NOT NULL REFERENCES car (id)
);