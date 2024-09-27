INSERT INTO customers (id, created_at, created_by, is_deleted, updated_at, updated_by, account_non_expired, active, changed_default_password, cover_img, email, enabled, first_name, last_name, locked, password, phone_number, profile_img, user_id, username)
VALUES (0, now(),0, false, now(), 0, true, true, false, 'https://cdn-icons-png.flaticon.com/512/149/149071.png', 'system@system.com', true, 'System', 'System', false, '', '0123456789', 'https://cdn-icons-png.flaticon.com/512/149/149071.png','system123456', 'systemSystem12345678' );

INSERT INTO roles (id, created_at, created_by, is_deleted, updated_at, updated_by, name, privileges)
VALUES (0, now(), 0, false, now(), 0, 'SYSTEM', '');

INSERT INTO user_roles (id, created_at, created_by, is_deleted, updated_at, updated_by, role_id, user_id)
VALUES (0, now(), 0, false, now(), 0, 0, 0)
