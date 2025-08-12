ALTER TABLE post
    ADD COLUMN car_id int REFERENCES car (id);
