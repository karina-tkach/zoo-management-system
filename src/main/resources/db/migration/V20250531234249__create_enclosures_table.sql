CREATE TYPE habitat_type_enum AS ENUM ('DESERT', 'GRASSLAND', 'AQUATIC', 'FOREST', 'WETLAND', 'MOUNTAIN', 'POLAR', 'SAVANNA');

CREATE TABLE enclosures (
                                 id SERIAL PRIMARY KEY,
                                 name VARCHAR(100) NOT NULL,
                                 location VARCHAR(200) NOT NULL,
                                 environment_type habitat_type_enum NOT NULL,
                                 area_m2 INT NOT NULL CHECK (area_m2 > 0)
);

INSERT INTO enclosures (name, location, environment_type, area_m2) VALUES
                                                                   ('Desert Dwellers', 'Zone A1', 'DESERT', 500),
                                                                   ('Grassy Plains', 'Zone B3', 'GRASSLAND', 750),
                                                                   ('Aquarium World', 'Zone C2', 'AQUATIC', 300),
                                                                   ('Rainforest Retreat', 'Zone D4', 'FOREST', 1200),
                                                                   ('Wetland Wonders', 'Zone E1', 'WETLAND', 450),
                                                                   ('Mountain Heights', 'Zone F5', 'MOUNTAIN', 950),
                                                                   ('Savanna Stretch', 'Zone G3', 'SAVANNA', 800);