CREATE TYPE examination_status_enum AS ENUM ('PLANNED', 'COMPLETED');

CREATE TABLE vet_examination_schedules (
                                  id SERIAL PRIMARY KEY,
                                  animal_id INTEGER NOT NULL REFERENCES animals(id) ON DELETE CASCADE,
                                  vet_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                  planned_datetime TIMESTAMP NOT NULL,
                                  reason TEXT NOT NULL,
                                  status examination_status_enum NOT NULL DEFAULT 'PLANNED',
                                  completed_at TIMESTAMP,
                                  UNIQUE(animal_id, planned_datetime),
                                  UNIQUE (vet_id, planned_datetime)
);

CREATE OR REPLACE PROCEDURE mark_animals_needing_checkup()
AS $$
DECLARE
    animal_rec RECORD;
    vet_rec RECORD;
    next_exam_time TIMESTAMP := NOW() + INTERVAL '1 day';
    attempt INT := 0;
BEGIN
    -- Update overdue animals
    UPDATE animals
    SET health_status = 'NEEDS_CHECK_UP'
    WHERE (last_checked_up_at < NOW() - INTERVAL '30 days')
      AND health_status = 'HEALTHY';

    -- Loop over animals needing checkup
    FOR animal_rec IN
        SELECT a.id
        FROM animals a
        WHERE a.health_status = 'NEEDS_CHECK_UP'
          AND NOT EXISTS (
            SELECT 1 FROM vet_examination_schedules v
            WHERE v.animal_id = a.id AND v.status = 'PLANNED'
        )
        LOOP
            -- Pick random vet
            SELECT * INTO vet_rec
            FROM users
            WHERE role = 'VETERINARIAN'
            ORDER BY RANDOM()
            LIMIT 1;

            -- Try inserting with retries
            IF vet_rec.id IS NOT NULL THEN
                LOOP
                    BEGIN
                        INSERT INTO vet_examination_schedules (
                            animal_id,
                            vet_id,
                            planned_datetime,
                            reason,
                            status
                        ) VALUES (
                                     animal_rec.id,
                                     vet_rec.id,
                                     next_exam_time,
                                     'Routine checkup (auto)',
                                     'PLANNED'
                                 );
                        -- Exit loop on success
                        EXIT;
                    EXCEPTION WHEN unique_violation THEN
                        -- Increment time slot
                        next_exam_time := next_exam_time + INTERVAL '15 minutes';
                        attempt := attempt + 1;

                        -- Prevent infinite loop
                        IF attempt >= 96 THEN
                            RAISE NOTICE 'Could not schedule exam for animal % within 24 hours.', animal_rec.id;
                            EXIT;
                        END IF;
                    END;
                END LOOP;
            END IF;
        END LOOP;
END;
$$ LANGUAGE plpgsql;
