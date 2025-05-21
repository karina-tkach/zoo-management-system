CREATE TABLE tickets (
                         id SERIAL PRIMARY KEY,
                         uuid UUID NOT NULL,
                         full_name VARCHAR(100) NOT NULL,
                         pricing_id INT NOT NULL REFERENCES ticket_pricings(id),
                         visit_date DATE NOT NULL,
                         excursion_id INT REFERENCES excursions(id), -- nullable, only for excursion tickets
                         purchase_method VARCHAR(20) NOT NULL CHECK (purchase_method IN ('ONLINE', 'OFFLINE')),
                         purchase_time TIMESTAMP NOT NULL DEFAULT NOW()
);
INSERT INTO tickets (uuid, full_name, pricing_id, visit_date, excursion_id, purchase_method) VALUES
                                                                                                 (gen_random_uuid(), 'Alice Johnson', 1, '2025-06-01', NULL, 'ONLINE'),
                                                                                                 (gen_random_uuid(), 'Bob Smith', 2, '2025-06-03', 1, 'OFFLINE'),
                                                                                                 (gen_random_uuid(), 'Charlie Davis', 3, '2025-06-05', NULL, 'ONLINE'),
                                                                                                 (gen_random_uuid(), 'Diana Lopez', 1, '2025-06-02', NULL, 'OFFLINE'),
                                                                                                 (gen_random_uuid(), 'Ethan Clark', 2, '2025-06-04', 1, 'ONLINE');