-- Se cambió ligeramente el script por la versión de MySQL 5.3
CREATE TABLE shedlock
(
    name VARCHAR(64) PRIMARY KEY,
    lock_until TIMESTAMP NULL,
    locked_at TIMESTAMP NULL,
    locked_by  VARCHAR(255)
);