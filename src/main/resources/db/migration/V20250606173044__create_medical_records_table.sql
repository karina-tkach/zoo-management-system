CREATE TABLE medical_records (
                                        id SERIAL PRIMARY KEY,
                                        examination_id INTEGER NOT NULL UNIQUE REFERENCES vet_examination_schedules(id) ON DELETE CASCADE,
                                        diagnosis TEXT,
                                        treatment TEXT,
                                        notes TEXT,
                                        created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE OR REPLACE FUNCTION update_animal_and_exam_after_medical_record()
RETURNS TRIGGER AS $$
DECLARE
    a_id INT;
    v_id INT;
    next_exam_time TIMESTAMP := NOW() + INTERVAL '7 days';
    attempt INT := 0;
BEGIN
    -- Get the animal_id and vet_id from the schedule
SELECT animal_id, vet_id INTO a_id, v_id
FROM vet_examination_schedules
WHERE id = NEW.examination_id;

-- Update animal health and exam status
UPDATE animals
SET health_status = CASE
                        WHEN NEW.diagnosis IS NULL OR TRIM(NEW.diagnosis) = '' THEN 'HEALTHY'::health_status_enum
                        ELSE 'SICK'::health_status_enum
    END,
    last_checked_up_at = NOW()
WHERE id = a_id;

UPDATE vet_examination_schedules
SET status = 'COMPLETED'::examination_status_enum,
    completed_at = NOW()
WHERE id = NEW.examination_id;

-- Try to insert follow-up exam if diagnosis exists
IF NEW.diagnosis IS NOT NULL AND TRIM(NEW.diagnosis) <> '' THEN
        LOOP
            -- Try inserting, exit loop if successful
BEGIN
INSERT INTO vet_examination_schedules (
    animal_id, vet_id, planned_datetime, reason, status
) VALUES (
             a_id, v_id, next_exam_time, 'Follow-up after diagnosis', 'PLANNED'::examination_status_enum
         );
EXIT;
EXCEPTION WHEN unique_violation THEN
                -- Move exam forward by 15 minutes
                next_exam_time := next_exam_time + INTERVAL '15 minutes';
                attempt := attempt + 1;

                -- Safety exit to prevent infinite loop
                IF attempt >= 96 THEN
                    RAISE NOTICE 'Could not schedule follow-up exam within 24 hours';
                    EXIT;
END IF;
END;
END LOOP;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_update_after_exam
    AFTER INSERT ON medical_records
    FOR EACH ROW
    EXECUTE FUNCTION update_animal_and_exam_after_medical_record();