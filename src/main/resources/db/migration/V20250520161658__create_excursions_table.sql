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

INSERT INTO excursions (topic, guide_id, description, date, start_time, duration_minutes, max_participants)
VALUES
    ('Safari Tour', 4, 'Explore the African Savannah zone', CURRENT_DATE + INTERVAL '5 days', '10:00', 90, 20),
    ('Reptile Walkthrough', 4, 'Learn about lizards and snakes', CURRENT_DATE + INTERVAL '3 days', '13:30', 60, 15),
    ('Bird Watching', 4, 'Observe exotic birds and their habits', CURRENT_DATE + INTERVAL '1 day', '09:00', 45, 10);