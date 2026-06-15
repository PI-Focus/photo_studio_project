CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO rooms (id, title, description, price) VALUES
	('8718f425-0ebe-48aa-9127-4541ed29524c', 'Зал 1', 'Очень крутое описание зала 1. Лучше зала просто не существует', 150000),
	('602109e2-f729-41f3-b93b-2f9a81878ed6', 'Зал 2', 'Очень крутое описание зала 2. Лучше зала просто не существует', 100000),
	('f8dca524-e325-4a14-a049-4f0fe73b15f9', 'Зал 3', 'Очень крутое описание зала 3. Лучше зала просто не существует', 90000);


INSERT INTO photos (id, room_id, path) VALUES
    (gen_random_uuid(), '8718f425-0ebe-48aa-9127-4541ed29524c', '/images/placeholder.png'),
	(gen_random_uuid(), '8718f425-0ebe-48aa-9127-4541ed29524c', '/images/placeholder.png'),
	(gen_random_uuid(), '8718f425-0ebe-48aa-9127-4541ed29524c', '/images/placeholder.png');


INSERT INTO photographers (id, name, surname, description, price, photo_path) VALUES
    ('692d3820-d762-436a-93ae-aaa1c7d2c1f5', 'Владимир', 'Кузнецов', 'Лучший фотограф', 50000, '/images/placeholder.png'),
    ('c0db4f10-b720-4e2e-9a72-e3a4f30d3028', 'Марина', 'Волкова', 'Нормальный фотограф', 70000, '/images/placeholder.png'),
    ('19b7720d-793a-4add-8207-a8baead65e2a', 'Дмитрий', 'Соколов', 'Так себе фотограф', 90000, '/images/placeholder.png');

INSERT INTO equipment (id, title, description, price, photo_path) VALUES
	('9596154e-ee45-454a-adda-084fca722807', 'Фотоаппарат', 'Описание фотоаппарата', 10000, '/images/placeholder.png'),
	('1251f03a-1851-491f-8071-0fe87547d35d', 'Штатив', 'Описание штатива', 20000, '/images/placeholder.png'),
	('e46bd00d-b682-4adc-8320-aabd9ba4f045', 'Софтбокс', 'Описание софтбокса', 15000, '/images/placeholder.png');


INSERT INTO users (id, login, phone_number, email, password, role) VALUES
	('04070c17-74da-4074-892b-d9c4dedab9cf', 'login0', '81111111111', 'email1@e', crypt('password0', gen_salt('bf')), 'ADMIN'),
	('3e5f1ff2-7c6f-47ec-9aac-62d0f328b4bd', 'login1', '82222222222', 'email2@e', crypt('password1', gen_salt('bf')), 'USER'),
	('e4a507da-7b8b-4f4b-9957-9d592d474621', 'login2', '83333333333', 'email3@e', crypt('password2', gen_salt('bf')), 'USER');


INSERT INTO reservations (id, user_id, room_id, photographer_id, time) VALUES
    ('56190bf3-92e4-450f-8fed-f2298069c82a', 'e4a507da-7b8b-4f4b-9957-9d592d474621', '8718f425-0ebe-48aa-9127-4541ed29524c', '692d3820-d762-436a-93ae-aaa1c7d2c1f5', '[2026-06-15 08:00:00, 2026-06-15 12:00:00]'),
    ('95e913dd-9de0-4bec-bb4c-6828556a7259', 'e4a507da-7b8b-4f4b-9957-9d592d474621', '8718f425-0ebe-48aa-9127-4541ed29524c', 'c0db4f10-b720-4e2e-9a72-e3a4f30d3028', '[2026-06-15 13:00:00, 2026-06-15 14:00:00]'),
    ('f54cdf57-a4a9-471e-b70a-c170b5d9b88e', '3e5f1ff2-7c6f-47ec-9aac-62d0f328b4bd', '602109e2-f729-41f3-b93b-2f9a81878ed6', '19b7720d-793a-4add-8207-a8baead65e2a', '[2026-06-15 09:00:00, 2026-06-15 19:00:00]');


INSERT INTO reserved_equipment (reserved_equipment_id, reservation_id, equipment_id, count) VALUES
   (gen_random_uuid(), '95e913dd-9de0-4bec-bb4c-6828556a7259', 'e46bd00d-b682-4adc-8320-aabd9ba4f045', 1),
   (gen_random_uuid(), '56190bf3-92e4-450f-8fed-f2298069c82a', '9596154e-ee45-454a-adda-084fca722807', 1),
   (gen_random_uuid(), '56190bf3-92e4-450f-8fed-f2298069c82a', '1251f03a-1851-491f-8071-0fe87547d35d', 1);
