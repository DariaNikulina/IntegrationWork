INSERT INTO routes (from_city, to_city, duration_minutes)
VALUES ('Пермь', 'Екатеринбург', 380) ON CONFLICT DO NOTHING;

INSERT INTO buses (plate_num, seat_count)
VALUES ('B123BB', 30) ON CONFLICT DO NOTHING;

DO $$
DECLARE
  existed_route_id BIGINT;
  existing_bus_id BIGINT;
  new_trip_id BIGINT;
  seat_counter INT;
  total_seats INT;
BEGIN
    SELECT id INTO existed_route_id FROM routes WHERE from_city='Пермь' AND to_city='Екатеринбург';
    SELECT id INTO existing_bus_id FROM buses WHERE plate_num='B123BB';
    
    IF existed_route_id IS NULL THEN
        RAISE NOTICE 'Маршрут не найден';
        RETURN;
    END IF;
    
    SELECT id INTO new_trip_id FROM trips WHERE route_id = existed_route_id AND departure_time::date = (CURRENT_DATE + INTERVAL '1 day')::date LIMIT 1;
    IF new_trip_id IS NULL THEN
        INSERT INTO trips (route_id, bus_id, departure_time, price_rub)
        VALUES (existed_route_id, existing_bus_id, (CURRENT_DATE + INTERVAL '1 day') + TIME '10:00', 500)
        RETURNING id INTO new_trip_id;
    END IF;

    SELECT seat_count INTO total_seats FROM buses WHERE id = existing_bus_id;

    IF (SELECT COUNT(1) FROM seats WHERE trip_id = new_trip_id) = 0 THEN
            FOR seat_counter IN 1..total_seats LOOP
                INSERT INTO seats (trip_id, seat_number, status)
                VALUES (new_trip_id, seat_counter, 'FREE');
            END LOOP;
    END IF;
END$$;

INSERT INTO routes (from_city, to_city, duration_minutes)
VALUES ('Пермь', 'Казань', 300) ON CONFLICT DO NOTHING;

INSERT INTO buses (plate_num, seat_count)
VALUES ('C123CC', 50) ON CONFLICT DO NOTHING;

DO $$
DECLARE
existed_route_id BIGINT;
  existing_bus_id BIGINT;
  new_trip_id BIGINT;
  seat_counter INT;
  total_seats INT;
BEGIN
SELECT id INTO existed_route_id FROM routes WHERE from_city='Пермь' AND to_city='Казань';
SELECT id INTO existing_bus_id FROM buses WHERE plate_num='C123CC';

IF existed_route_id IS NULL THEN
        RAISE NOTICE 'Маршрут не найден';
        RETURN;
END IF;

SELECT id INTO new_trip_id FROM trips WHERE route_id = existed_route_id AND departure_time::date = (CURRENT_DATE + INTERVAL '1 day')::date LIMIT 1;
IF new_trip_id IS NULL THEN
        INSERT INTO trips (route_id, bus_id, departure_time, price_rub)
        VALUES (existed_route_id, existing_bus_id, (CURRENT_DATE + INTERVAL '1 day') + TIME '12:00', 500)
        RETURNING id INTO new_trip_id;
END IF;

SELECT seat_count INTO total_seats FROM buses WHERE id = existing_bus_id;

IF (SELECT COUNT(1) FROM seats WHERE trip_id = new_trip_id) = 0 THEN
            FOR seat_counter IN 1..total_seats LOOP
    INSERT INTO seats (trip_id, seat_number, status)
    VALUES (new_trip_id, seat_counter, 'FREE');
END LOOP;
END IF;
END$$;

