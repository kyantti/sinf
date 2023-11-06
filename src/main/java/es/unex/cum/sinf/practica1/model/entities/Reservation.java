package es.unex.cum.sinf.practica1.model.entities;

import java.time.LocalDate;
import java.util.UUID;

public class Reservation {
    private UUID reservationId;
    private UUID packageId;
    private UUID clientId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean payed;

    public Reservation(UUID reservationId, UUID packageId, UUID clientId, LocalDate startDate, LocalDate endDate,
            boolean payed) {
        this.reservationId = reservationId;
        this.packageId = packageId;
        this.clientId = clientId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payed = payed;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public void setReservationId(UUID reservationId) {
        this.reservationId = reservationId;
    }

    public UUID getPackageId() {
        return packageId;
    }

    public void setPackageId(UUID packageId) {
        this.packageId = packageId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    @Override
    public String toString() {
        return "Reservation [reservationId=" + reservationId + ", packageId=" + packageId + ", clientId=" + clientId
                + ", startDate=" + startDate + ", endDate=" + endDate + ", payed=" + payed + "]";
    }

    
}
