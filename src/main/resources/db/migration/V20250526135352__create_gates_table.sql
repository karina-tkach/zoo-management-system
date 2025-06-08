CREATE TABLE gates (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) UNIQUE NOT NULL,
                       location TEXT NOT NULL
);

INSERT INTO gates (name, location) VALUES
                                       ('North Entrance', 'Main Road - North Side'),
                                       ('South Entrance', 'Parking Lot - South Side');