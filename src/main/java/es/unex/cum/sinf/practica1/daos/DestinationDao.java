package es.unex.cum.sinf.practica1.daos;

import java.util.Set;
import java.util.UUID;

import es.unex.cum.sinf.practica1.entities.Destination;

public interface DestinationDao extends Dao <Destination, UUID> {
    Set<Destination> getDestinationsByWeather(String weather);
    Set<Destination> getDestinationsByCountry(String country);
    Set<Destination> getDestinationsByName(String name);
}
