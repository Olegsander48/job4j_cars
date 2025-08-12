ALTER TABLE car
    ADD COLUMN owner_id int UNIQUE REFERENCES owners(id);
