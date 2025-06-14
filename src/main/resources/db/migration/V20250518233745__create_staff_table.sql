CREATE TABLE staff (
                       id SERIAL PRIMARY KEY,
                       user_id INT UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                       hire_date DATE NOT NULL,
                       salary INTEGER NOT NULL,
                       working_days VARCHAR(100) NOT NULL,
                       shift_start TIME NOT NULL,
                       shift_end TIME NOT NULL
);
INSERT INTO staff (user_id, hire_date, salary, working_days, shift_start, shift_end) VALUES
                                                                                         (1, '2022-01-10', 7000, 'Mon,Tue,Wed,Thu,Fri', '09:00', '17:00'),
                                                                                         (2, '2023-02-15', 12000, 'Mon,Wed,Fri', '07:00', '15:00'),
                                                                                         (3, '2023-05-20', 15000, 'Tue,Thu,Sat', '08:00', '16:00'),
                                                                                         (4, '2024-03-01', 8000, 'Tue,Sat,Sun', '10:00', '18:00'),
                                                                                         (5, '2023-11-01', 8000, 'Mon,Tue,Wed', '08:00', '18:00'),
                                                                                         (6, '2022-06-18', 5000, 'Tue,Thu,Sat', '12:00', '18:00'),
                                                                                         (8, '2023-04-12', 7200, 'Mon,Tue,Wed,Fri', '09:00', '17:00'),
                                                                                         (9, '2024-01-20', 11000, 'Mon,Thu,Sat', '07:00', '15:00'),
                                                                                         (10, '2023-08-05', 16000, 'Mon,Tue,Fri', '08:00', '16:00'),
                                                                                         (11, '2024-02-10', 7500, 'Wed,Fri,Sun', '10:00', '18:00'),
                                                                                         (12, '2022-10-01', 8200, 'Mon,Tue,Wed', '08:00', '18:00'),
                                                                                         (13, '2023-03-25', 5500, 'Tue,Thu,Sat', '12:00', '18:00'),
                                                                                         (16, '2025-02-01', 7300, 'Mon,Wed,Thu', '09:00', '17:00'),
                                                                                         (17, '2025-03-25', 5700, 'Thu,Sat', '12:30', '18:40');