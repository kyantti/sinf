package es.unex.cum.sinf.practica1.model.daos;

import es.unex.cum.sinf.practica1.model.entities.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservationDao extends Dao<Reservation, UUID>{
   List<Reservation> getReservationsByClientId(UUID clientId);
   List<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate);
}
