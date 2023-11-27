package es.unex.cum.sinf.practica1.entities;

import java.util.UUID;

public class PackageReservationSummary implements Comparable<PackageReservationSummary>  {
    private UUID packageId;
    private long totalReservations;

    public PackageReservationSummary(UUID packageId, long totalReservations) {
        this.packageId = packageId;
        this.totalReservations = totalReservations;
    }

    public UUID getPackageId() {
        return packageId;
    }

    public void setPackageId(UUID packageId) {
        this.packageId = packageId;
    }

    public long getTotalReservations() {
        return totalReservations;
    }

    public void setTotalReservations(int totalReservations) {
        this.totalReservations = totalReservations;
    }

    @Override
    public String toString() {
        return "PackageReservationSummary{" + "packageId=" + packageId + ", totalReservations=" + totalReservations + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PackageReservationSummary summary = (PackageReservationSummary) o;

        if (totalReservations != summary.totalReservations) return false;
        return packageId.equals(summary.packageId);
    }

    @Override
    public int hashCode() {
        int result = packageId.hashCode();
        result = 31 * result + (int) (totalReservations ^ (totalReservations >>> 32));
        return result;
    }

    @Override
    public int compareTo(PackageReservationSummary other) {
        return Long.compare(other.getTotalReservations(), this.getTotalReservations());
    }
}
