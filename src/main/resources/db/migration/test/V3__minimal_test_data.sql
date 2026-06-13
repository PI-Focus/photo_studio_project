CREATE EXTENSION IF NOT EXISTS pgcrypto;

TRUNCATE TABLE equipment CASCADE;
TRUNCATE TABLE rooms CASCADE;
TRUNCATE TABLE photos CASCADE;
TRUNCATE TABLE photographers CASCADE;
TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE reservations CASCADE;
TRUNCATE TABLE reserved_equipment CASCADE;


INSERT INTO rooms (id, title, description) VALUES
	('8718f425-0ebe-48aa-9127-4541ed29524c', 'Зал 1', 'Очень крутое описание зала 1. Лучше зала просто не существует'),
	('602109e2-f729-41f3-b93b-2f9a81878ed6', 'Зал 2', 'Очень крутое описание зала 2. Лучше зала просто не существует'),
	('f8dca524-e325-4a14-a049-4f0fe73b15f9', 'Зал 3', 'Очень крутое описание зала 3. Лучше зала просто не существует');


INSERT INTO photos (id, room_id, path) VALUES
    (gen_random_uuid(), '8718f425-0ebe-48aa-9127-4541ed29524c', '/images/placeholder.png'),
	(gen_random_uuid(), '8718f425-0ebe-48aa-9127-4541ed29524c', '/images/placeholder.png'),
	(gen_random_uuid(), '8718f425-0ebe-48aa-9127-4541ed29524c', '/images/placeholder.png');


INSERT INTO photographers (id, name, surname, description, photo_path) VALUES
    (gen_random_uuid(), 'Владимир', 'Кузнецов', 'Лучший фотограф', '/images/placeholder.png'),
    (gen_random_uuid(), 'Марина', 'Волкова', 'Нормальный фотограф', '/images/placeholder.png'),
    (gen_random_uuid(), 'Дмитрий', 'Соколов', 'Так себе фотограф', '/images/placeholder.png');

INSERT INTO equipment (id, title, description, photo_path) VALUES
	('9596154e-ee45-454a-adda-084fca722807', 'Фотоаппарат', 'Описание фотоаппарата', '/images/placeholder.png'),
	('1251f03a-1851-491f-8071-0fe87547d35d', 'Штатив', 'Описание штатива', '/images/placeholder.png'),
	('e46bd00d-b682-4adc-8320-aabd9ba4f045', 'Софтбокс', 'Описание софтбокса', '/images/placeholder.png');


INSERT INTO users (id, login, password, role) VALUES
	('04070c17-74da-4074-892b-d9c4dedab9cf', 'login0', crypt('password0', gen_salt('bf')), 'ADMIN'),
	('3e5f1ff2-7c6f-47ec-9aac-62d0f328b4bd', 'login1', crypt('password1', gen_salt('bf')), 'USER'),
	('e4a507da-7b8b-4f4b-9957-9d592d474621', 'login2', crypt('password2', gen_salt('bf')), 'USER');


INSERT INTO reservations (id, user_id, room_id, day, from_time, to_time) VALUES
    ('165b248a-9d78-4fa5-8b26-6151b8ee5e12', '3e5f1ff2-7c6f-47ec-9aac-62d0f328b4bd', '8718f425-0ebe-48aa-9127-4541ed29524c', '2026-05-18', 9, 13),
    ('95e913dd-9de0-4bec-bb4c-6828556a7259', 'e4a507da-7b8b-4f4b-9957-9d592d474621', '8718f425-0ebe-48aa-9127-4541ed29524c', '2026-05-18', 14, 18),
    ('f936c6d4-dec8-448c-807a-23e06522b8db', '04070c17-74da-4074-892b-d9c4dedab9cf', '8718f425-0ebe-48aa-9127-4541ed29524c', '2026-05-18', 19, 22);


INSERT INTO reserved_equipment (reserved_equipment_id, reservation_id, equipment_id) VALUES
    (gen_random_uuid(), '165b248a-9d78-4fa5-8b26-6151b8ee5e12', '9596154e-ee45-454a-adda-084fca722807'),
    (gen_random_uuid(), '165b248a-9d78-4fa5-8b26-6151b8ee5e12', '1251f03a-1851-491f-8071-0fe87547d35d'),
    (gen_random_uuid(), '95e913dd-9de0-4bec-bb4c-6828556a7259', 'e46bd00d-b682-4adc-8320-aabd9ba4f045');
