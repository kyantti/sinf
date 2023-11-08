package es.unex.cum.sinf.practica1.model.daos.cassandra;

import es.unex.cum.sinf.practica1.model.daos.ClientDao;
import es.unex.cum.sinf.practica1.model.entities.Client;

import java.util.List;
import java.util.UUID;

public class CassandraPackageDao implements ClientDao {
    @Override
    public Client get(UUID uuid) {
        return null;
    }

    @Override
    public List<Client> getAll() {
        return null;
    }

    @Override
    public void insert(Client client) {

    }

    @Override
    public void update(Client client) {

    }

    @Override
    public void delete(UUID uuid) {

    }
}
