use pablo_setrakian_bearzotti;

select *
from destinations;

CREATE INDEX ON pablo_setrakian_bearzotti.packages (destination_id);


CREATE TABLE pablo_setrakian_bearzotti.package_summary
(
    package_id         uuid PRIMARY KEY,
    total_reservations counter
);


UPDATE pablo_setrakian_bearzotti.package_summary
SET total_reservations = total_reservations + 1
WHERE package_id = 6c385758-20e5-4429-ae83-2fd16799586f;


select *
from destinations;



CREATE TABLE pablo_setrakian_bearzotti.package_summary_new
(
    package_id         uuid,
    total_reservations counter,
    PRIMARY KEY (package_id, total_reservations)
) WITH CLUSTERING ORDER BY (total_reservations DESC);

CREATE INDEX ON pablo_setrakian_bearzotti.package_summary (total_reservations);

CREATE TABLE pablo_setrakian_bearzotti.packages_by_destination_and_duration
(
    destination_id uuid,
    duration       int,
    package_id     uuid,
    name           text,
    price          decimal,
    PRIMARY KEY ((destination_id, duration), package_id)
);
CREATE INDEX packages_new_idx ON pablo_setrakian_bearzotti.packages_by_destination_and_duration (package_id);

SELECT *
FROM pablo_setrakian_bearzotti.packages_by_destination_and_duration
WHERE destination_id = 1f505075-2806-4b3e-9ab9-30db87f049c3
  AND duration = 6;

SELECT *
FROM pablo_setrakian_bearzotti.packages
WHERE destination_id = a5f0a3aa-063f-4beb-967f-f5e6ac58f712
  AND duration = 6 ALLOW FILTERING ;

SELECT *
FROM pablo_setrakian_bearzotti.packages_by_destination_and_duration
WHERE package_id = 0141f9fc-d8db-4e08-8189-a1523b3c93e1;

CREATE CUSTOM INDEX reservations_search_idx ON pablo_setrakian_bearzotti.reservations (client_id, package_id, payed) USING 'org.apache.cassandra.index.sasi.SASIIndex';



CREATE MATERIALIZED VIEW pablo_setrakian_bearzotti.package_summary_by_reservations AS
SELECT package_id, total_reservations
FROM package_summary
WHERE package_id IS NOT NULL
  AND total_reservations IS NOT NULL
PRIMARY KEY (total_reservations, package_id)
WITH CLUSTERING ORDER BY (package_id DESC);


CREATE TABLE pablo_setrakian_bearzotti.package_summary_v2
(
    package_id uuid,
    total_reservations int,
    PRIMARY KEY (package_id, total_reservations ))
    WITH CLUSTERING ORDER BY (total_reservations DESC);




SELECT * FROM pablo_setrakian_bearzotti.reservations
WHERE client_id =  271bfd06-ddd4-4d83-9b8b-acb2815c76c9 AND package_id = 20b908fe-5d71-4a69-8ece-63aed8317b1a;