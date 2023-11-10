package es.unex.cum.sinf.practica1.model.daos.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import es.unex.cum.sinf.practica1.model.daos.ClientDao;
import es.unex.cum.sinf.practica1.model.daos.ReservationDao;
import es.unex.cum.sinf.practica1.model.entities.Client;
import es.unex.cum.sinf.practica1.model.entities.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CassandraReservationDao implements ReservationDao {
    private Session session;

    public CassandraReservationDao(Session session) {
        this.session = session;
    }

    @Override
    public Reservation get(UUID reservationId) {
        String query = "SELECT * FROM pablo_setrakian_bearzotti.reservations" +
                       "WHERE reservation_id = ?";
        ResultSet resultSet = session.execute(query, reservationId);
        Row row = resultSet.one();

        if (row != null) {
            return new Reservation(
                    row.getUUID("reservation_id"),
                    row.getUUID("package_id"),
                    row.getUUID("client_id"),
                    cassandraLocalDateToJavaLocalDate(row.getDate("start_date")),
                    cassandraLocalDateToJavaLocalDate(row.getDate("end_date")),
                    row.getBool("payed")
            );
        } else {
            return null;
        }
    }

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();

        String query = "SELECT * FROM pablo_setrakian_bearzotti.reservations";
        ResultSet resultSet = session.execute(query);

        for (Row row : resultSet) {
            Reservation reservation = new Reservation(
                    row.getUUID("reservation_id"),
                    row.getUUID("package_id"),
                    row.getUUID("client_id"),
                    cassandraLocalDateToJavaLocalDate(row.getDate("start_date")),
                    cassandraLocalDateToJavaLocalDate(row.getDate("end_date")),
                    row.getBool("payed")
            );
            reservations.add(reservation);
        }

        return reservations;
    }

    private java.time.LocalDate cassandraLocalDateToJavaLocalDate(com.datastax.driver.core.LocalDate cassandraLocalDate) {
        return java.time.LocalDate.of(
                cassandraLocalDate.getYear(),
                cassandraLocalDate.getMonth(),
                cassandraLocalDate.getDay()
        );
    }

    @Override
    public void insert(Reservation reservation) {
        String query = "INSERT INTO pablo_setrakian_bearzotti.reservations (reservation_id, client_id, end_date, package_id, payed, start_date)" +
                       "VALUES (?, ?, ?, ?, ?, ?)";
        session.execute(query, reservation.getReservationId(), reservation.getClientId(), javaLocalDateToCassandraLocalDate(reservation.getEndDate()), reservation.getPackageId(), reservation.isPayed(), javaLocalDateToCassandraLocalDate(reservation.getStartDate()));
    }

    private com.datastax.driver.core.LocalDate javaLocalDateToCassandraLocalDate(java.time.LocalDate javaLocalDate) {
        return com.datastax.driver.core.LocalDate.fromYearMonthDay(javaLocalDate.getYear(), javaLocalDate.getMonthValue(), javaLocalDate.getDayOfMonth());
    }

    @Override
    public void update(Reservation reservation) {
        String query = "UPDATE pablo_setrakian_bearzotti.reservations" +
                       "SET client_id = ?, end_date = ?, package_id = ?, payed = ?, start_date = ?" +
                       "WHERE reservation_id = ?";
        session.execute(query, reservation.getClientId(), reservation.getEndDate(), reservation.getPackageId(), reservation.isPayed(), reservation.getStartDate(), reservation.getReservationId());
    }

    @Override
    public void delete(UUID reservationId) {
        String query = "DELETE FROM pablo_setrakian_bearzotti.reservations" +
                       "WHERE reservation_id = ?";
        session.execute(query, reservationId);
    }

    @Override
    public List<Reservation> getReservationsByClientId(UUID clientId) {
        List<Reservation> reservations = new ArrayList<>();

        String query = "SELECT * FROM pablo_setrakian_bearzotti.reservations " +
                       "WHERE client_id = ?";
        ResultSet resultSet = session.execute(query, clientId);

        for (Row row : resultSet) {
            Reservation reservation = new Reservation(
                    row.getUUID("reservation_id"),
                    row.getUUID("package_id"),
                    row.getUUID("client_id"),
                    cassandraLocalDateToJavaLocalDate(row.getDate("start_date")),
                    cassandraLocalDateToJavaLocalDate(row.getDate("end_date")),
                    row.getBool("payed")
            );
            reservations.add(reservation);
        }

        return reservations;
    }

    @Override
    public List<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Reservation> reservations = new ArrayList<>();

        String query = "SELECT * FROM pablo_setrakian_bearzotti.reservations " +
                       "WHERE start_date >= ? and end_date <= ? "+
                       "ALLOW FILTERING";
        ResultSet resultSet = session.execute(query,javaLocalDateToCassandraLocalDate(startDate), javaLocalDateToCassandraLocalDate(endDate));

        for (Row row : resultSet) {
            Reservation reservation = new Reservation(
                    row.getUUID("reservation_id"),
                    row.getUUID("package_id"),
                    row.getUUID("client_id"),
                    cassandraLocalDateToJavaLocalDate(row.getDate("start_date")),
                    cassandraLocalDateToJavaLocalDate(row.getDate("end_date")),
                    row.getBool("payed")
            );
            reservations.add(reservation);
        }

        return reservations;
    }

}
