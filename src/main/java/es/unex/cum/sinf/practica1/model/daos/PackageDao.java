package es.unex.cum.sinf.practica1.model.daos;

import es.unex.cum.sinf.practica1.model.entities.TravelPackage;

import java.util.List;
import java.util.UUID;

public interface PackageDao extends Dao<TravelPackage, UUID> {
    List<TravelPackage> getPackagesByName(String name);

    List<TravelPackage> getPackagesByDestinationId(UUID destinationId);
}
