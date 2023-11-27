package es.unex.cum.sinf.practica1.entities;

import java.util.UUID;

public class Destination {
    private UUID destinationId;
    private String name;
    private String country;
    private String description;
    private String weather;

    public Destination(UUID destinationId, String name, String country, String description, String weather) {
        this.destinationId = destinationId;
        this.name = name;
        this.country = country;
        this.description = description;
        this.weather = weather;
    }

    public UUID getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(UUID destinationId) {
        this.destinationId = destinationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "Destination [destinationId=" + destinationId + ", name=" + name + ", country=" + country
                + ", description=" + description + ", weather=" + weather + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Destination that = (Destination) o;

        if (!destinationId.equals(that.destinationId)) return false;
        if (!name.equals(that.name)) return false;
        if (!country.equals(that.country)) return false;
        if (!description.equals(that.description)) return false;
        return weather.equals(that.weather);
    }

    @Override
    public int hashCode() {
        int result = destinationId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + country.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + weather.hashCode();
        return result;
    }
}
