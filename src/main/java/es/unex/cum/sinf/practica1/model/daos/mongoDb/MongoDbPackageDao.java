package es.unex.cum.sinf.practica1.model.daos.mongoDb;

import es.unex.cum.sinf.practica1.model.daos.PackageDao;
import es.unex.cum.sinf.practica1.model.entities.TravelPackage;

import java.util.List;
import java.util.UUID;

public class MongoDbPackageDao implements PackageDao {
    @Override
    public TravelPackage get(UUID uuid) {
        return null;
    }

    @Override
    public List<TravelPackage> getAll() {
        return null;
    }

    @Override
    public void insert(TravelPackage travelPackage) {

    }

    @Override
    public void update(TravelPackage travelPackage) {

    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public List<TravelPackage> getPackagesByName(String name) {
        return null;
    }

    @Override
    public List<TravelPackage> getPackagesByDestinationId(UUID destinationId) {
        return null;
    }
}
