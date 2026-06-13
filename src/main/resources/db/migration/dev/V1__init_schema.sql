CREATE TYPE user_role AS ENUM ('USER', 'ADMIN');

CREATE TABLE rooms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(32) NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE photos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    room_id UUID NOT NULL,
    path VARCHAR(64) NOT NULL,
    FOREIGN KEY (room_id) REFERENCES rooms (id)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE photographers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(32) NOT NULL,
    surname VARCHAR(32) NOT NULL,
    description TEXT NOT NULL,
    photo_path VARCHAR(64) NOT NULL
);

CREATE TABLE equipment (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(32) NOT NULL,
    description TEXT NOT NULL,
    photo_path VARCHAR(64) NOT NULL
);

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    login VARCHAR(32) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role user_role NOT NULL
);

CREATE TABLE reservations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    room_id UUID,
    day DATE NOT NULL,
    from_time SMALLINT NOT NULL,
    to_time SMALLINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
    ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms (id)
    ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE reserved_equipment (
	reserved_equipment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reservation_id UUID NOT NULL,
    equipment_id UUID,
    FOREIGN KEY (reservation_id) REFERENCES reservations (id)
    ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES equipment (id)
    ON UPDATE CASCADE ON DELETE SET NULL
);