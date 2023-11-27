package es.unex.cum.sinf.practica1.entities;

import java.math.BigDecimal;
import java.util.UUID;

public class TravelPackage {
    private UUID packageId;
    private String name;
    private UUID destinationId;
    private int duration;
    private BigDecimal price;

    public TravelPackage(UUID packageId, String name, UUID destinationId, int duration, BigDecimal price) {
        this.packageId = packageId;
        this.name = name;
        this.destinationId = destinationId;
        this.duration = duration;
        this.price = price;
    }

    public UUID getPackageId() {
        return packageId;
    }

    public void setPackageId(UUID packageId) {
        this.packageId = packageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(UUID destinationId) {
        this.destinationId = destinationId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Package [packageId=" + packageId + ", name=" + name + ", destinationId=" + destinationId + ", duration="
                + duration + ", price=" + price + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TravelPackage that = (TravelPackage) o;

        if (duration != that.duration) return false;
        if (!packageId.equals(that.packageId)) return false;
        if (!name.equals(that.name)) return false;
        if (!destinationId.equals(that.destinationId)) return false;
        return price.equals(that.price);
    }

    @Override
    public int hashCode() {
        int result = packageId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + destinationId.hashCode();
        result = 31 * result + duration;
        result = 31 * result + price.hashCode();
        return result;
    }
}
