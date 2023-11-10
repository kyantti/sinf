use pablo_setrakian_bearzotti;

select * from destinations;

CREATE INDEX ON pablo_setrakian_bearzotti.packages (destination_id);


CREATE CUSTOM INDEX ON pablo_setrakian_bearzotti.reservations (client_id, end_date, start_date) USING 'reservations';

CREATE TABLE pablo_setrakian_bearzotti.package_summary (package_id uuid PRIMARY KEY, total_reservations counter);


