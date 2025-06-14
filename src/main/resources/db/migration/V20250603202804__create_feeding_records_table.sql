CREATE TABLE feeding_records (
                          id SERIAL PRIMARY KEY,
                          feeding_schedule_id INTEGER NOT NULL REFERENCES feeding_schedules(id) ON DELETE CASCADE,
                          date DATE NOT NULL,
                          UNIQUE(feeding_schedule_id, date)
);

INSERT INTO feeding_records (feeding_schedule_id, date) VALUES
                                                            (1, CURRENT_DATE - INTERVAL '1 day'),
                                                            (2, CURRENT_DATE - INTERVAL '1 day'),
                                                            (3, CURRENT_DATE - INTERVAL '1 day'),
                                                            (4, CURRENT_DATE - INTERVAL '1 day'),
                                                            (5, CURRENT_DATE - INTERVAL '1 day'),
                                                            (6, CURRENT_DATE - INTERVAL '1 day'),
                                                            (7, CURRENT_DATE - INTERVAL '1 day'),
                                                            (8, CURRENT_DATE - INTERVAL '1 day'),
                                                            (9, CURRENT_DATE - INTERVAL '1 day'),
                                                            (10, CURRENT_DATE - INTERVAL '1 day'),
                                                            (11, CURRENT_DATE - INTERVAL '1 day'),
                                                            (12, CURRENT_DATE - INTERVAL '1 day'),
                                                            (13, CURRENT_DATE - INTERVAL '1 day'),
                                                            (14, CURRENT_DATE - INTERVAL '1 day'),
                                                            (15, CURRENT_DATE - INTERVAL '1 day');



CREATE OR REPLACE FUNCTION feeding_schedule_update_trigger()
RETURNS TRIGGER AS $$
BEGIN
  IF NOT OLD.is_done_today AND NEW.is_done_today THEN
    -- Запобігання повторному вставленню
    IF NOT EXISTS (
      SELECT 1 FROM feeding_records
      WHERE feeding_schedule_id = OLD.id AND date = CURRENT_DATE
    ) THEN
      INSERT INTO feeding_records (feeding_schedule_id, date)
      VALUES (OLD.id, CURRENT_DATE);

      UPDATE animals
      SET last_fed_up_at = NOW()
      WHERE id = OLD.animal_id;
    END IF;

  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;




CREATE TRIGGER trg_feeding_schedule_update
    AFTER UPDATE ON feeding_schedules
    FOR EACH ROW
    EXECUTE FUNCTION feeding_schedule_update_trigger();
