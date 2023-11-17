package es.unex.cum.sinf.practica1.model.daos;

import es.unex.cum.sinf.practica1.model.entities.TravelPackage;

import java.util.Set;
import java.util.UUID;

public interface PackageDao extends Dao<TravelPackage, UUID> {
    Set<TravelPackage> getPackagesByName(String name);
    Set<TravelPackage> getPackagesByDestinationId(UUID destinationId);
    Set<TravelPackage> getPackagesByDestinationIdAndDuration(UUID destinationId, int duration);
}
