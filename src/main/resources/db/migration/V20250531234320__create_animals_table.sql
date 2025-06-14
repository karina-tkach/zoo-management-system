CREATE TYPE animal_group_enum AS ENUM ('INVERTEBRATES', 'FISH', 'AMPHIBIANS', 'REPTILES', 'BIRDS', 'MAMMALS');
CREATE TYPE animal_gender_enum AS ENUM ('MALE', 'FEMALE', 'OTHER');
CREATE TYPE health_status_enum AS ENUM ('HEALTHY', 'SICK', 'NEEDS_CHECK_UP');

CREATE TABLE animals (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            species VARCHAR(255) NOT NULL,
                            animal_group animal_group_enum NOT NULL,
                            habitat_type habitat_type_enum NOT NULL,
                            gender animal_gender_enum NOT NULL,
                            date_of_birth DATE NOT NULL,
                            enclosure_id INT NOT NULL REFERENCES enclosures(id),
                            health_status health_status_enum NOT NULL DEFAULT 'HEALTHY',
                            image VARCHAR(255) NOT NULL,
                            last_checked_up_at TIMESTAMP NOT NULL DEFAULT NOW(),
                            last_fed_up_at TIMESTAMP NOT NULL DEFAULT NOW()
);

INSERT INTO animals (name, species, animal_group, habitat_type, gender, date_of_birth, enclosure_id, image)
VALUES
('Lora', 'Fennec Fox', 'MAMMALS', 'DESERT', 'FEMALE', '2021-03-15', 1, 'fennec_fox.jpg'),

('Fred', 'Thomsons Gazelle', 'MAMMALS', 'GRASSLAND', 'MALE', '2020-05-30', 2, 'thomsons_gazelle.jpg'),
('Jess', 'African Crowned Crane', 'BIRDS', 'GRASSLAND', 'FEMALE', '2019-09-12', 2, 'crowned_crane.jpg'),

('Nemo', 'Clownfish', 'FISH', 'AQUATIC', 'MALE', '2022-01-05', 3, 'clownfish.jpg'),
('Tina', 'Octopus', 'INVERTEBRATES', 'AQUATIC', 'FEMALE', '2021-11-02', 3, 'octopus.jpg'),

('Luke', 'Capuchin Monkey', 'MAMMALS', 'FOREST', 'MALE', '2018-08-19', 4, 'capuchin_monkey.jpg'),

('Emily', 'Red-Eyed Tree Frog', 'AMPHIBIANS', 'WETLAND', 'FEMALE', '2023-03-01', 5, 'tree_frog.jpg'),

('Lu', 'Snow Leopard', 'MAMMALS', 'MOUNTAIN', 'FEMALE', '2017-12-10', 6, 'show_leopard.jpg'),

('Gary', 'Zebra', 'MAMMALS', 'SAVANNA', 'MALE', '2019-04-25', 7, 'zebra.jpg'),

('Polly', 'Emperor Penguin', 'BIRDS', 'POLAR', 'FEMALE', '2020-02-14', 8, 'emperor_penguin.jpg'),
('Sandy', 'Desert Tortoise', 'REPTILES', 'DESERT', 'MALE', '2015-06-20', 9, 'desert_tortoise.jpg'),
('Gerry', 'Meadowlark', 'BIRDS', 'GRASSLAND', 'MALE', '2021-07-12', 10, 'meadowlark.jpg'),
('Coral', 'Clownfish', 'FISH', 'AQUATIC', 'FEMALE', '2022-04-10', 11, 'clownfish2.jpg'),
('Forest', 'Howler Monkey', 'MAMMALS', 'FOREST', 'MALE', '2019-11-05', 12, 'howler_monkey.jpg'),
('Marsh', 'American Bullfrog', 'AMPHIBIANS', 'WETLAND', 'OTHER', '2023-05-15', 13, 'bullfrog.jpg');