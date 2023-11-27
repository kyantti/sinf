package es.unex.cum.sinf.practica1.daos;

import es.unex.cum.sinf.practica1.entities.Reservation;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public interface ReservationDao extends Dao<Reservation, UUID>{
   Set<Reservation> getReservationsByClientId(UUID clientId);
   Set<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate);
   Set<Reservation> getReservationsByPackageId(UUID packageId);
   Set<Reservation> getReservationsByClientIdAndDateRange(UUID clientId, LocalDate startDate, LocalDate endDate);
   Set<Reservation> getReservationsByClientIdAndPackageIdAndPaymentStatus(UUID clientId, UUID packageId, boolean payed);
}
