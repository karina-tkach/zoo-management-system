CREATE TABLE visit_logs (
                            id SERIAL PRIMARY KEY,
                            gate_id INT NOT NULL REFERENCES gates(id) ON DELETE CASCADE,
                            ticket_id INT UNIQUE NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
                            entry_time TIMESTAMP NOT NULL DEFAULT NOW(),
                            notes TEXT
);

INSERT INTO visit_logs (gate_id, ticket_id, entry_time, notes) VALUES
                                                                   (1, 1, CURRENT_DATE + INTERVAL '10 hours', 'Normal entry at 10:00'),
                                                                   (2, 4, CURRENT_DATE + INTERVAL '11 hours', 'Entry at 11:00'),
                                                                   (3, 9, CURRENT_DATE + INTERVAL '12 hours', 'Entry at noon');
