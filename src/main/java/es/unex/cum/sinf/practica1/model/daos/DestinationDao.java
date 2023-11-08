package es.unex.cum.sinf.practica1.model.daos;


import java.util.List;
import java.util.UUID;

import es.unex.cum.sinf.practica1.model.entities.Destination;

public interface DestinationDao extends Dao <Destination, UUID> {

    List<Destination> getPopularDestinations();

}
