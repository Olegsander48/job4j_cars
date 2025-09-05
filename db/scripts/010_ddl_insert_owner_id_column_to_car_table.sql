ALTER TABLE car
    ADD COLUMN owner_id int REFERENCES owners (id);
