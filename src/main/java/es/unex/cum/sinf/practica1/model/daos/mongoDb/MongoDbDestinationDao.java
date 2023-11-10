package es.unex.cum.sinf.practica1.model.daos.mongoDb;

import es.unex.cum.sinf.practica1.model.daos.DestinationDao;
import es.unex.cum.sinf.practica1.model.entities.Destination;

import java.util.List;
import java.util.UUID;

public class MongoDbDestinationDao implements DestinationDao {
    @Override
    public Destination get(UUID uuid) {
        return null;
    }

    @Override
    public List<Destination> getAll() {
        return null;
    }

    @Override
    public void insert(Destination destination) {

    }

    @Override
    public void update(Destination destination) {

    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public List<Destination> getPopularDestinations() {
        return null;
    }
}
