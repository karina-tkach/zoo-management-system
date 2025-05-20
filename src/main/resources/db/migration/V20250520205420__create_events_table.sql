CREATE TABLE events (
                        id SERIAL PRIMARY KEY,
                        title VARCHAR(150) NOT NULL,
                        description TEXT NOT NULL,
                        date DATE NOT NULL,
                        start_time TIME NOT NULL,
                        duration_minutes INTEGER NOT NULL,
                        location VARCHAR(150) NOT NULL,
                        image VARCHAR(255)
);

INSERT INTO events (title, description, date, start_time, duration_minutes, location, image)
VALUES
    (
        'Meet the Vet',
        'An interactive lecture for kids about animal health in the zoo.',
        '2025-06-10',
        '11:00:00',
        90,
        'Conference Hall #1',
        'veterinary_event.jpg'
    ),
    (
        'Night at the Zoo',
        'A night tour with flashlights, learning about nocturnal animals.',
        '2025-07-15',
        '21:00:00',
        120,
        'Main Entrance',
        'night_zoo_tour.jpg'
    ),
    (
        'Open Day',
        'Free admission for all visitors and special activities throughout the zoo.',
        '2025-08-01',
        '09:00:00',
        480,
        'Entire Zoo Area',
        'open_day_banner.jpg'
    );

