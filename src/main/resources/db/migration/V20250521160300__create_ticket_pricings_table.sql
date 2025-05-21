CREATE TYPE ticket_type_enum AS ENUM ('ADULT', 'CHILD', 'PREFERENTIAL');
CREATE TYPE visit_type_enum AS ENUM ('GENERAL', 'EXCURSION');

CREATE TABLE ticket_pricings (
                                id SERIAL PRIMARY KEY,
                                ticket_type ticket_type_enum NOT NULL,
                                visit_type visit_type_enum NOT NULL,
                                price INT NOT NULL CHECK (price >= 0),

                                UNIQUE(ticket_type, visit_type)
);

INSERT INTO ticket_pricings (ticket_type, visit_type, price) VALUES
                                                                 ('ADULT', 'GENERAL', 200),
                                                                 ('ADULT', 'EXCURSION', 250),
                                                                 ('CHILD', 'GENERAL', 150),
                                                                 ('CHILD', 'EXCURSION', 150),
                                                                 ('PREFERENTIAL', 'GENERAL', 150),
                                                                 ('PREFERENTIAL', 'EXCURSION', 200);