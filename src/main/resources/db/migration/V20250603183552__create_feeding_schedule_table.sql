CREATE TABLE feeding_schedules (
                                   id SERIAL PRIMARY KEY,
                                   animal_id INTEGER NOT NULL REFERENCES animals(id) ON DELETE CASCADE,
                                   caretaker_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                   food_type VARCHAR(150) NOT NULL,
                                   time TIME NOT NULL,
                                   portion_size_grams INTEGER NOT NULL CHECK (portion_size_grams >= 0),
                                   is_done_today BOOLEAN NOT NULL DEFAULT FALSE,
                                   UNIQUE(animal_id, time),
                                   UNIQUE (caretaker_id, time)
);
INSERT INTO feeding_schedules (animal_id, caretaker_id, food_type, time, portion_size_grams)
VALUES
    (1, 2, 'Insects', '08:00', 300),
    (1, 2, 'Fruits', '16:00', 400),
    (2, 2, 'Grass and hay', '08:30', 800),
    (2, 2, 'Grass and hay', '16:30', 800),
    (3, 2, 'Grains and insects', '09:00', 300),
    (3, 2, 'Grains and insects', '17:00', 300),
    (4, 2, 'Pellets and algae', '09:30', 50),
    (4, 2, 'Pellets and algae', '17:30', 50),
    (5, 2, 'Crabs and shellfish', '10:00', 200),
    (5, 2, 'Crabs and shellfish', '18:00', 150),
    (6, 2, 'Fruits and insects', '10:30', 350),
    (6, 2, 'Fruits and insects', '18:30', 300),
    (7, 2, 'Flies and crickets', '11:00', 50),
    (7, 2, 'Flies and crickets', '19:00', 40),
    (8, 2, 'Meat and bones', '11:30', 500),
    (8, 2, 'Meat and bones', '19:30', 500),
    (9, 2, 'Grass and leaves', '12:00', 400),
    (9, 2, 'Grass and leaves', '20:00', 350);
