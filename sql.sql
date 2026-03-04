use vehicle_service_db;
UPDATE users SET role = 'CUSTOMER' WHERE role = 'ROLE_CUSTOMER';
UPDATE users SET role = 'ADMIN' WHERE role = 'ROLE_ADMIN';
UPDATE users SET role = 'SERVICE_ADVISOR' WHERE role = 'ROLE_SERVICE_ADVISOR';
SET SQL_SAFE_UPDATES = 0;

SELECT user_id, email, role FROM users;

UPDATE users 
SET role = 'CUSTOMER'
WHERE role = 'ROLE_CUSTOMER'
AND user_id IS NOT NULL;

UPDATE users 
SET role = 'ADMIN'
WHERE role = 'ROLE_ADMIN'
AND user_id IS NOT NULL;

UPDATE users 
SET role = 'SERVICE_ADVISOR'
WHERE role = 'ROLE_SERVICE_ADVISOR'
AND user_id IS NOT NULL;

UPDATE users
SET role = 'ROLE_SERVICE_ADVISOR'
WHERE email = 'serviceadvisor@gmail.com';

UPDATE users
SET role = 'ROLE_SERVICE_ADVISOR'
WHERE email = 'amupsiddhi83@gmail.com';

