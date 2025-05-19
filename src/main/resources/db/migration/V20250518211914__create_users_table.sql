CREATE TABLE users
(
    id        SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    password  VARCHAR NOT NULL,
    email     VARCHAR UNIQUE NOT NULL,
    role      VARCHAR NOT NULL
);
INSERT INTO Users (name, password, email, role) VALUES
                                                    ('Julia Fox', '$2a$12$9aePrcOq.8iy.YIrVyoRReDprO5k./34i0UBeuZUs.qJOMN2Pd2X6', 'admin@gmail.com', 'ADMIN'),
                                                    ('John Smith', '$2a$12$bqjLK9k5aLw4RawmQlsvMuUL4bhwRI8wh6/EGfXksRdwynRAGyvM.', 'caretaker@gmail.com', 'CARETAKER'),
                                                    ('Mark Bryan', '$2a$12$WhVvVnKwZVxYI7Dn7LPrNecM1pOiyIalLDlE.tPHt3aZp8/Aawdry', 'veterinarian@gmail.com', 'VETERINARIAN'),
                                                    ('Jessica Stone', '$2a$12$HMrk/0a5aH1LD5bzGv34v.gE1WbyGoAfewdiD05eQ.dOOYLqJuOTK', 'guide@gmail.com', 'GUIDE'),
                                                    ('John Paul', '$2a$12$bsr2gZq9sAVCQcy0RVgXzujEsODXiprBlZdorQFHMgQipY4F/dwr.', 'ticket.agent@gmail.com', 'TICKET_AGENT'),
                                                    ('Rebecca Fox', '$2a$12$/Xd1VKnVMgHEHn9tfxXzfulMy3FND/nrfHCbH2PFfKIAceIYlZYh6', 'event.manager@gmail.com', 'EVENT_MANAGER'),
                                                    ('Karina Tkach', '$2a$12$Vt5u.kWPJP3FD7/BDiBaCOsBbgC36cEMdbKfqVmJjgVweLHE48hmu', 'visitor@gmail.com', 'VISITOR');
