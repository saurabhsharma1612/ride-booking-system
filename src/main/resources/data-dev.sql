-- =========================================================
-- Development/Test Data
-- =========================================================

-- ---------------------------------------------------------
-- Test Rider
-- Email: rider.test@example.com
-- Password: Rider@123
-- ---------------------------------------------------------

INSERT INTO app_user (
    name,
    email,
    password
)
VALUES (
           'Test Rider',
           'rider.test@example.com',
           '$2a$10$vT9pxXeTWw2CFFvSLpFCie0XxXSyELYF9CqFo3t4JlSrBgsiRbT2i'
       )
    ON CONFLICT (email) DO NOTHING;


-- ---------------------------------------------------------
-- Test Driver
-- Email: driver.test@example.com
-- Password: Driver@123
-- ---------------------------------------------------------

INSERT INTO app_user (
    name,
    email,
    password
)
VALUES (
           'Test Driver',
           'driver.test@example.com',
           '$2a$10$u1fIZsfHaqAg/4HP9YQRCe2DPxdhYUx/fIi0L4yEcJlSzJMt1Vh7K'
       )
    ON CONFLICT (email) DO NOTHING;

-- ---------------------------------------------------------
-- Ensure deterministic test passwords
-- ---------------------------------------------------------

UPDATE app_user
SET password = '$2a$10$vT9pxXeTWw2CFFvSLpFCie0XxXSyELYF9CqFo3t4JlSrBgsiRbT2i'
WHERE email = 'rider.test@example.com';

UPDATE app_user
SET password = '$2a$10$u1fIZsfHaqAg/4HP9YQRCe2DPxdhYUx/fIi0L4yEcJlSzJMt1Vh7K'
WHERE email = 'driver.test@example.com';

-- ---------------------------------------------------------
-- Roles
-- ---------------------------------------------------------

INSERT INTO user_roles (user_id, roles)
SELECT id, 'USER'
FROM app_user
WHERE email = 'rider.test@example.com'
  AND NOT EXISTS (
    SELECT 1
    FROM user_roles
    WHERE user_id = (
        SELECT id
        FROM app_user
        WHERE email = 'rider.test@example.com'
    )
      AND roles = 'USER'
);

INSERT INTO user_roles (user_id, roles)
SELECT id, 'DRIVER'
FROM app_user
WHERE email = 'driver.test@example.com'
  AND NOT EXISTS (
    SELECT 1
    FROM user_roles
    WHERE user_id = (
        SELECT id
        FROM app_user
        WHERE email = 'driver.test@example.com'
    )
      AND roles = 'DRIVER'
);

-- ---------------------------------------------------------
-- Rider profile
-- ---------------------------------------------------------

INSERT INTO rider (
    user_id,
    rating
)
SELECT
    id,
    0.0
FROM app_user
WHERE email = 'rider.test@example.com'
  AND NOT EXISTS (
    SELECT 1
    FROM rider r
    WHERE r.user_id = (
        SELECT id
        FROM app_user
        WHERE email = 'rider.test@example.com'
    )
);


-- ---------------------------------------------------------
-- Rider wallet
-- ---------------------------------------------------------

INSERT INTO wallet (
    user_id,
    balance
)
SELECT
    id,
    1000.0
FROM app_user
WHERE email = 'rider.test@example.com'
  AND NOT EXISTS (
    SELECT 1
    FROM wallet w
    WHERE w.user_id = (
        SELECT id
        FROM app_user
        WHERE email = 'rider.test@example.com'
    )
);

UPDATE rider
SET rating = 0.0
WHERE user_id = (
    SELECT id
    FROM app_user
    WHERE email = 'rider.test@example.com'
);

UPDATE wallet
SET balance = 1000.0
WHERE user_id = (
    SELECT id
    FROM app_user
    WHERE email = 'rider.test@example.com'
);

-- ---------------------------------------------------------
-- Driver profile
-- ---------------------------------------------------------

INSERT INTO driver (
    user_id,
    rating,
    available,
    current_location
)
SELECT
    id,
    4.8,
    true,
    ST_SetSRID(
            ST_MakePoint(77.5950, 12.9720),
            4326
    )
FROM app_user
WHERE email = 'driver.test@example.com'
  AND NOT EXISTS (
    SELECT 1
    FROM driver d
    WHERE d.user_id = (
        SELECT id
        FROM app_user
        WHERE email = 'driver.test@example.com'
    )
);
-- ---------------------------------------------------------
-- Ensure deterministic driver test state
-- ---------------------------------------------------------

UPDATE driver
SET
    rating = 4.8,
    available = true,
    current_location = ST_SetSRID(
            ST_MakePoint(77.5950, 12.9720),
            4326
                       )
WHERE user_id = (
    SELECT id
    FROM app_user
    WHERE email = 'driver.test@example.com'
);