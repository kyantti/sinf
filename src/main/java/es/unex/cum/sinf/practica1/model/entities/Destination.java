package es.unex.cum.sinf.practica1.model.entities;

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

    
}
