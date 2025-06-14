CREATE TABLE gates
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(100) UNIQUE NOT NULL,
    location TEXT                NOT NULL
);

INSERT INTO gates (name, location)
VALUES ('North Entrance', 'Main Road - North Side'),
       ('South Entrance', 'Parking Lot - South Side'),
       ('East Entrance', 'Near the Childrens Playground'),
       ('West Entrance', 'Close to the Safari Zone'),
       ('Main Ticket Office', 'Opposite the Parking Lot'),
       ('Aquarium Gate', 'Next to the Marine Pavilion'),
       ('Aviary Gate', 'Near the Bird Exhibit'),
       ('Zoo Train Station Gate', 'Beside the Mini Railway'),
       ('Tropical Zone Gate', 'South Wing – Rainforest Area'),
       ('Reptile House Gate', 'Near the Reptile Pavilion'),
       ('Amphitheater Gate', 'Near the Open Stage'),
       ('Petting Zoo Gate', 'Close to the Farm Animal Zone'),
       ('Event Hall Gate', 'Next to the Conference Hall'),
       ('Emergency Exit Gate', 'West Side – Staff Only Access'),
       ('VIP Entrance', 'Private Access – Member Only Zone');