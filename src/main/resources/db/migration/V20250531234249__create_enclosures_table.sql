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
                                                                   ('Savanna Stretch', 'Zone G3', 'SAVANNA', 800),
                                                                   ('Polar Peaks', 'Zone H2', 'POLAR', 600),
                                                                   ('Desert Oasis', 'Zone A2', 'DESERT', 550),
                                                                   ('Grassland Grove', 'Zone B4', 'GRASSLAND', 700),
                                                                   ('Aquatic Reef', 'Zone C5', 'AQUATIC', 350),
                                                                   ('Forest Canopy', 'Zone D6', 'FOREST', 1100),
                                                                   ('Wetland Marsh', 'Zone E3', 'WETLAND', 500),
                                                                   ('Mountain Ridge', 'Zone F6', 'MOUNTAIN', 980),
                                                                   ('Savanna Plains', 'Zone G4', 'SAVANNA', 820);