package es.unex.cum.sinf.practica1.daos.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import es.unex.cum.sinf.practica1.daos.ReservationDao;
import es.unex.cum.sinf.practica1.entities.Reservation;

import java.time.LocalDate;
import java.util.*;

public class CassandraReservationDao implements ReservationDao {
    private final Session session;
    private final String keyspace;

    public CassandraReservationDao(Session session, String keyspace) {
        this.session = session;
        this.keyspace = keyspace;
    }

    @Override
    public Reservation get(UUID reservationId) {
        String query = "SELECT * FROM " + keyspace + ".reservations WHERE reservation_id = ?";
        ResultSet resultSet = session.execute(query, reservationId);
        Row row = resultSet.one();

        if (row != null) {
            return mapRowToReservation(row);
        } else {
            return null;
        }
    }

    @Override
    public Set<Reservation> getAll() {
        Set <Reservation> reservations = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".reservations";
        ResultSet resultSet = session.execute(query);

        for (Row row : resultSet) {
            Reservation reservation = mapRowToReservation(row);
            reservations.add(reservation);
        }

        return reservations;
    }

    private java.time.LocalDate cassandraLocalDateToJavaLocalDate(com.datastax.driver.core.LocalDate cassandraLocalDate) {
        return java.time.LocalDate.of(cassandraLocalDate.getYear(), cassandraLocalDate.getMonth(), cassandraLocalDate.getDay());
    }

    @Override
    public void insert(Reservation reservation) {
        String query = "INSERT INTO " + keyspace + ".reservations (reservation_id, client_id, end_date, package_id, payed, start_date) VALUES (?, ?, ?, ?, ?, ?)";
        session.execute(query, reservation.getReservationId(), reservation.getClientId(), javaLocalDateToCassandraLocalDate(reservation.getEndDate()), reservation.getPackageId(), reservation.isPayed(), javaLocalDateToCassandraLocalDate(reservation.getStartDate()));
        String query2 = "INSERT INTO " + keyspace + ".reservations_by_client_package_payed (reservation_id, client_id, end_date, package_id, payed, start_date) VALUES (?, ?, ?, ?, ?, ?)";
        session.execute(query2, reservation.getReservationId(), reservation.getClientId(), javaLocalDateToCassandraLocalDate(reservation.getEndDate()), reservation.getPackageId(), reservation.isPayed(), javaLocalDateToCassandraLocalDate(reservation.getStartDate()));
    }

    private com.datastax.driver.core.LocalDate javaLocalDateToCassandraLocalDate(java.time.LocalDate javaLocalDate) {
        return com.datastax.driver.core.LocalDate.fromYearMonthDay(javaLocalDate.getYear(), javaLocalDate.getMonthValue(), javaLocalDate.getDayOfMonth());
    }

    @Override
    public void update(Reservation reservation) {
        String query = "UPDATE " + keyspace + ".reservations SET client_id = ?, end_date = ?, package_id = ?, payed = ?, start_date = ? WHERE reservation_id = ?";
        session.execute(query, reservation.getClientId(), reservation.getEndDate(), reservation.getPackageId(), reservation.isPayed(), reservation.getStartDate(), reservation.getReservationId());
    }

    @Override
    public void delete(UUID reservationId) {
        String query = "DELETE FROM " + keyspace + ".reservations WHERE reservation_id = ?";
        session.execute(query, reservationId);
    }

    @Override
    public Set<Reservation> getReservationsByClientId(UUID clientId) {
        Set<Reservation> reservations = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".reservations WHERE client_id = ?";
        ResultSet resultSet = session.execute(query, clientId);

        for (Row row : resultSet) {
            Reservation reservation = mapRowToReservation(row);
            reservations.add(reservation);
        }

        return reservations;
    }

    @Override
    public Set<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) {
        Set<Reservation> reservations = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".reservations WHERE start_date >= ? and end_date <= ? ALLOW FILTERING";
        ResultSet resultSet = session.execute(query, javaLocalDateToCassandraLocalDate(startDate), javaLocalDateToCassandraLocalDate(endDate));

        for (Row row : resultSet) {
            Reservation reservation = mapRowToReservation(row);
            reservations.add(reservation);
        }

        return reservations;
    }

    @Override
    public Set<Reservation> getReservationsByPackageId(UUID packageId) {
        Set<Reservation> reservations = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".reservations WHERE package_id = ?";
        ResultSet resultSet = session.execute(query, packageId);

        for (Row row : resultSet) {
            Reservation reservation = mapRowToReservation(row);
            reservations.add(reservation);
        }

        return reservations;
    }

    @Override
    public Set<Reservation> getReservationsByClientIdAndDateRange(UUID clientId, LocalDate startDate, LocalDate endDate) {
        Set<Reservation> reservations = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".reservations WHERE client_id = ? and start_date >= ? and end_date <= ? ALLOW FILTERING";
        ResultSet resultSet = session.execute(query, clientId, javaLocalDateToCassandraLocalDate(startDate), javaLocalDateToCassandraLocalDate(endDate));

        for (Row row : resultSet) {
            Reservation reservation = mapRowToReservation(row);
            reservations.add(reservation);
        }

        return reservations;
    }

    @Override
    public Set<Reservation> getReservationsByClientIdAndPackageIdAndPaymentStatus(UUID clientId, UUID packageId, boolean payed) {
        Set<Reservation> reservations = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".reservations_by_client_package_payed WHERE client_id = ? AND package_id = ? AND payed = ?";
        ResultSet resultSet = session.execute(query, clientId, packageId, payed );

        for (Row row : resultSet) {
            Reservation reservation = mapRowToReservation(row);
            reservations.add(reservation);
        }

        return reservations;
    }

    private Reservation mapRowToReservation(Row row) {
        UUID reservationId = row.getUUID("reservation_id");
        UUID packageId = row.getUUID("package_id");
        UUID clientId = row.getUUID("client_id");
        LocalDate startDate = cassandraLocalDateToJavaLocalDate(row.getDate("start_date"));
        LocalDate endDate = cassandraLocalDateToJavaLocalDate(row.getDate("end_date"));
        boolean payed = row.getBool("payed");

        return new Reservation(reservationId, packageId, clientId, startDate, endDate, payed);
    }

}
