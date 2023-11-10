package es.unex.cum.sinf.practica1.model.daos.mongoDb;

import es.unex.cum.sinf.practica1.model.daos.ReservationDao;
import es.unex.cum.sinf.practica1.model.entities.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class MongoDbReservationDao implements ReservationDao {
    @Override
    public Reservation get(UUID uuid) {
        return null;
    }

    @Override
    public List<Reservation> getAll() {
        return null;
    }

    @Override
    public void insert(Reservation reservation) {

    }

    @Override
    public void update(Reservation reservation) {

    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public List<Reservation> getReservationsByClientId(UUID clientId) {
        return null;
    }

    @Override
    public List<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) {
        return null;
    }
}
