CREATE TABLE IF NOT EXISTS rescheduling_statuses (
    id SMALLINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(80) NOT NULL,
    is_terminal BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB;

INSERT INTO rescheduling_statuses(id,code,name,is_terminal)
VALUES (1,'PENDING','Pendiente',FALSE),(2,'APPROVED','Aprobada',TRUE),(3,'REJECTED','Rechazada',TRUE)
ON DUPLICATE KEY UPDATE name=VALUES(name),is_terminal=VALUES(is_terminal);

CREATE TABLE IF NOT EXISTS appointment_reschedule_requests (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT UNSIGNED NOT NULL,
    requested_by_user_id BIGINT UNSIGNED NOT NULL,
    status_id SMALLINT UNSIGNED NOT NULL,
    new_scheduled_start_at DATETIME NOT NULL,
    new_scheduled_end_at DATETIME NOT NULL,
    reason VARCHAR(500) NULL,
    decision_reason VARCHAR(500) NULL,
    decided_by_user_id BIGINT UNSIGNED NULL,
    requested_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    decided_at TIMESTAMP NULL,
    FOREIGN KEY(appointment_id) REFERENCES appointments(id) ON DELETE CASCADE,
    FOREIGN KEY(requested_by_user_id) REFERENCES users(id),
    FOREIGN KEY(status_id) REFERENCES rescheduling_statuses(id),
    FOREIGN KEY(decided_by_user_id) REFERENCES users(id),
    INDEX ix_reschedule_appointment_status(appointment_id,status_id),
    INDEX ix_reschedule_status_requested(status_id,requested_at)
) ENGINE=InnoDB;

ALTER TABLE professional_slots
    ADD COLUMN reschedule_request_id BIGINT UNSIGNED NULL,
    ADD CONSTRAINT fk_slots_reschedule_request FOREIGN KEY(reschedule_request_id)
        REFERENCES appointment_reschedule_requests(id) ON DELETE SET NULL,
    ADD INDEX ix_slots_reschedule(reschedule_request_id);
