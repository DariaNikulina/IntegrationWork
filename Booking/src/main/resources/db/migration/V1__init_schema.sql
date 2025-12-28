CREATE TABLE IF NOT EXISTS routes (
    id BIGSERIAL PRIMARY KEY,
    from_city VARCHAR(128) NOT NULL,
    to_city VARCHAR(128) NOT NULL,
    duration_minutes INT
);

CREATE TABLE IF NOT EXISTS buses (
    id BIGSERIAL PRIMARY KEY,
    plate_num VARCHAR(64) NOT NULL,
    seat_count INT NOT NULL
);

CREATE TABLE IF NOT EXISTS trips (
    id BIGSERIAL PRIMARY KEY,
    route_id BIGINT NOT NULL REFERENCES routes(id) ON DELETE CASCADE,
    bus_id BIGINT REFERENCES buses(id) ON DELETE SET NULL,
    departure_time TIMESTAMP WITHOUT TIME ZONE,
    price_rub INT
);

CREATE TABLE IF NOT EXISTS seats (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL REFERENCES trips(id) ON DELETE CASCADE,
    seat_number INT NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'FREE',
    CONSTRAINT seats_trip_seat_unique UNIQUE (trip_id, seat_number),
    CONSTRAINT seats_status_check CHECK (status IN ('FREE','BOOKED'))
);

CREATE TABLE IF NOT EXISTS tickets (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL REFERENCES trips(id) ON DELETE CASCADE,
    seat_id BIGINT NOT NULL REFERENCES seats(id) ON DELETE CASCADE,
    passenger_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'RESERVED',
    CONSTRAINT ticket_status_check CHECK (status IN ('RESERVED','CANCELLED'))
    );


CREATE INDEX IF NOT EXISTS idx_trips_departure ON trips(departure_time);
CREATE INDEX IF NOT EXISTS idx_tickets_email ON tickets(email);
