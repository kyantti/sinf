package es.unex.cum.sinf.practica1.model.daos.cassandra;

import es.unex.cum.sinf.practica1.model.daos.ReservationDao;
import es.unex.cum.sinf.practica1.model.entities.Reservation;

import java.util.List;
import java.util.UUID;

public class CassandraReservationDao implements ReservationDao {
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
}
