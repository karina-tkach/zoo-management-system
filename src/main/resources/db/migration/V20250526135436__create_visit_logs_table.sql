CREATE TABLE visit_logs (
                            id SERIAL PRIMARY KEY,
                            gate_id INT NOT NULL REFERENCES gates(id) ON DELETE CASCADE,
                            ticket_id INT UNIQUE NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
                            entry_time TIMESTAMP NOT NULL DEFAULT NOW(),
                            notes TEXT
);

INSERT INTO visit_logs (gate_id, ticket_id, notes) VALUES
(1, 1, 'Normal entry'),
(2, 2, 'Late visitor');