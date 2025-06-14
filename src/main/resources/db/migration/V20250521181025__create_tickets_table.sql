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
                                                                                                 (gen_random_uuid(), 'Alice Johnson', 1, '2025-06-14', NULL, 'ONLINE'),
                                                                                                 (gen_random_uuid(), 'Bob Smith', 2, CURRENT_DATE + INTERVAL '10 days', 1, 'OFFLINE'),
                                                                                                 (gen_random_uuid(), 'Charlie Davis', 3, '2025-06-05', NULL, 'ONLINE'),
                                                                                                 (gen_random_uuid(), 'Diana Lopez', 1, '2025-06-14', NULL, 'OFFLINE'),
                                                                                                 (gen_random_uuid(), 'Ethan Clark', 2, CURRENT_DATE + INTERVAL '10 days', 1, 'ONLINE'),
                                                                                                 (gen_random_uuid(), 'Bobby Clark', 1, '2025-06-20', NULL, 'ONLINE'),
                                                                                                 (gen_random_uuid(), 'Bob Doe', 2, CURRENT_DATE + INTERVAL '8 days', 2, 'OFFLINE'),
                                                                                                 (gen_random_uuid(), 'Arianna Froz', 3, '2025-06-05', NULL, 'ONLINE'),
                                                                                                 (gen_random_uuid(), 'Kendrick Brown', 1, '2025-06-14', NULL, 'OFFLINE'),
                                                                                                 (gen_random_uuid(), 'Ethan Gray', 2, CURRENT_DATE + INTERVAL '15 days', 3, 'ONLINE'),
                                                                                                 (gen_random_uuid(), 'Frederica Cam', 1, '2025-06-20', NULL, 'ONLINE'),
                                                                                                 (gen_random_uuid(), 'Paul Smith', 2, CURRENT_DATE + INTERVAL '12 days', 4, 'OFFLINE'),
                                                                                                 (gen_random_uuid(), 'Victor Fernandez', 3, '2025-06-05', NULL, 'ONLINE'),
                                                                                                 (gen_random_uuid(), 'Gia Fox', 1, '2025-07-02', NULL, 'OFFLINE'),
                                                                                                 (gen_random_uuid(), 'Dabby Smith', 2, CURRENT_DATE + INTERVAL '17 days', 5, 'ONLINE');