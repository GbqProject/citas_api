-- Synthetic local/demo availability for the next calendar month.
-- The professional is selected by the stable demo code so this does not
-- depend on a credential or a user email.

INSERT INTO specialties(code, name, appointment_duration_minutes, is_general, requires_admin_approval, active)
VALUES ('CARDIOLOGIA_DEMO', 'Cardiología Demo', 60, FALSE, TRUE, TRUE)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    appointment_duration_minutes = VALUES(appointment_duration_minutes),
    is_general = VALUES(is_general),
    requires_admin_approval = VALUES(requires_admin_approval),
    active = VALUES(active);

INSERT INTO professional_specialties(professional_id, specialty_id, is_primary, active)
SELECT p.id, s.id, FALSE, TRUE
FROM professionals p
JOIN specialties s ON s.code = 'CARDIOLOGIA_DEMO'
WHERE p.professional_code LIKE 'FQ-MED-%'
ON DUPLICATE KEY UPDATE active = TRUE;

INSERT INTO professional_locations(professional_id, location_id, active)
SELECT p.id, l.id, TRUE
FROM professionals p
JOIN locations l ON l.code IN ('HIC', 'ICV')
WHERE p.professional_code LIKE 'FQ-MED-%'
ON DUPLICATE KEY UPDATE active = TRUE;

INSERT INTO availability_blocks(professional_id, location_id, available_date, start_time, end_time, active)
WITH RECURSIVE calendar_days AS (
    SELECT CAST(DATE_FORMAT(DATE_ADD(CURRENT_DATE, INTERVAL 1 MONTH), '%Y-%m-01') AS DATE) AS available_date
    UNION ALL
    SELECT DATE_ADD(available_date, INTERVAL 1 DAY)
    FROM calendar_days
    WHERE available_date < LAST_DAY(DATE_ADD(CURRENT_DATE, INTERVAL 1 MONTH))
), demo_sessions AS (
    SELECT 'HIC' AS location_code, CAST('08:00:00' AS TIME) AS start_time, CAST('12:00:00' AS TIME) AS end_time
    UNION ALL
    SELECT 'ICV', CAST('14:00:00' AS TIME), CAST('18:00:00' AS TIME)
)
SELECT p.id, l.id, d.available_date, s.start_time, s.end_time, TRUE
FROM calendar_days d
JOIN demo_sessions s ON WEEKDAY(d.available_date) < 5
JOIN locations l ON l.code = s.location_code AND l.active = TRUE
JOIN professionals p ON p.professional_code LIKE 'FQ-MED-%' AND p.active = TRUE
WHERE NOT EXISTS (
    SELECT 1
    FROM availability_blocks existing
    WHERE existing.professional_id = p.id
      AND existing.location_id = l.id
      AND existing.available_date = d.available_date
      AND existing.start_time = s.start_time
      AND existing.end_time = s.end_time
);

INSERT INTO professional_slots(availability_block_id, start_at, end_at)
WITH RECURSIVE half_hours AS (
    SELECT CAST('00:00:00' AS TIME) AS slot_time
    UNION ALL
    SELECT ADDTIME(slot_time, '00:30:00')
    FROM half_hours
    WHERE slot_time < CAST('23:30:00' AS TIME)
)
SELECT b.id,
       TIMESTAMP(b.available_date, h.slot_time),
       TIMESTAMP(b.available_date, ADDTIME(h.slot_time, '00:30:00'))
FROM availability_blocks b
JOIN half_hours h ON h.slot_time >= b.start_time AND h.slot_time < b.end_time
WHERE b.available_date >= DATE_FORMAT(DATE_ADD(CURRENT_DATE, INTERVAL 1 MONTH), '%Y-%m-01')
  AND b.available_date <= LAST_DAY(DATE_ADD(CURRENT_DATE, INTERVAL 1 MONTH))
  AND b.professional_id IN (SELECT id FROM professionals WHERE professional_code LIKE 'FQ-MED-%')
  AND NOT EXISTS (
      SELECT 1
      FROM professional_slots existing
      WHERE existing.availability_block_id = b.id
        AND existing.start_at = TIMESTAMP(b.available_date, h.slot_time)
  );
