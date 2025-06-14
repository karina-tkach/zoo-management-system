CREATE TABLE excursions (
                           id SERIAL PRIMARY KEY,
                           topic VARCHAR(255) NOT NULL,
                           guide_id INTEGER NOT NULL,
                           description TEXT NOT NULL,
                           date DATE NOT NULL,
                           start_time TIME NOT NULL,
                           duration_minutes INTEGER NOT NULL,
                           max_participants INTEGER NOT NULL,
                           booked_count INTEGER NOT NULL DEFAULT 0,
                           FOREIGN KEY (guide_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION cleanup_old_excursions()
RETURNS TRIGGER AS $$
BEGIN
DELETE FROM excursions WHERE date < CURRENT_DATE;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_cleanup_old_excursions
    AFTER INSERT OR UPDATE ON excursions
                        FOR EACH STATEMENT
                        EXECUTE FUNCTION cleanup_old_excursions();

INSERT INTO excursions (topic, guide_id, description, date, start_time, duration_minutes, max_participants, booked_count)
VALUES
    ('Safari Tour', 4, 'Explore the African Savannah zone', CURRENT_DATE + INTERVAL '10 days', '10:00', 90, 20, 2),
    ('Reptile Walkthrough', 4, 'Learn about lizards and snakes', CURRENT_DATE + INTERVAL '8 days', '13:30', 60, 15, 1),
    ('Bird Watching', 4, 'Observe exotic birds and their habits', CURRENT_DATE + INTERVAL '15 days', '09:00', 45, 10, 1),
    ('Nocturnal Animals Tour', 11, 'Discover the behaviors of night-active animals', CURRENT_DATE + INTERVAL '12 days', '18:00', 60, 15, 1),
    ('Tropical Forest Trek', 11, 'Experience the diversity of tropical ecosystems', CURRENT_DATE + INTERVAL '17 days', '11:00', 75, 18, 1),
    ('Penguin Parade', 17, 'Watch penguins in their naturalistic habitat', CURRENT_DATE + INTERVAL '9 days', '14:30', 30, 25, 0),
    ('Zoo Behind-the-Scenes', 17, 'Learn how we care for the animals daily', CURRENT_DATE + INTERVAL '11 days', '12:00', 90, 12, 0),
    ('Feeding Time Show', 17, 'See how and what animals eat', CURRENT_DATE + INTERVAL '13 days', '15:00', 45, 20, 0),
    ('Endangered Species Focus', 11, 'Explore how we protect endangered animals', CURRENT_DATE + INTERVAL '14 days', '10:30', 60, 16, 0),
    ('Wetlands Walk', 4, 'Discover animals living in swampy environments', CURRENT_DATE + INTERVAL '16 days', '13:00', 50, 14, 0),
    ('Animal Training Demo', 17, 'See how we train animals for health checks', CURRENT_DATE + INTERVAL '18 days', '11:30', 40, 10, 0),
    ('Insect World Exploration', 4, 'Learn about the smallest inhabitants of the zoo', CURRENT_DATE + INTERVAL '19 days', '09:30', 30, 8, 0),
    ('Big Cats Encounter', 4, 'Observe lions, tigers, and leopards up close', CURRENT_DATE + INTERVAL '20 days', '16:00', 60, 20, 0),
    ('Aquatic Life Tour', 11, 'Dive into the lives of aquatic species', CURRENT_DATE + INTERVAL '21 days', '12:30', 50, 18, 0),
    ('Zoo Junior Guide Program', 17, 'Interactive tour for kids to become mini-guides', CURRENT_DATE + INTERVAL '22 days', '10:00', 60, 12, 0);