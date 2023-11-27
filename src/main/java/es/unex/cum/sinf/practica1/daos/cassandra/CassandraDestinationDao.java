package es.unex.cum.sinf.practica1.daos.cassandra;

import java.util.*;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import es.unex.cum.sinf.practica1.daos.DestinationDao;
import es.unex.cum.sinf.practica1.entities.Destination;

public class CassandraDestinationDao implements DestinationDao {

    private final Session session;
    private final String keyspace;

    public CassandraDestinationDao(Session session, String keyspace) {
        this.session = session;
        this.keyspace = keyspace;
    }

    @Override
    public Destination get(UUID auxDestinationId) {
        String query = "SELECT * FROM " + keyspace + ".destinations WHERE destination_id = ?";
        ResultSet resultSet = session.execute(query, auxDestinationId);
        Row row = resultSet.one();

        return mapRowToDestination(row);
    }

    @Override
    public Set<Destination> getAll() {
        Set <Destination> destinations = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".destinations";
        ResultSet resultSet = session.execute(query);

        for (Row row : resultSet) {
            Destination destination = mapRowToDestination(row);
            destinations.add(destination);
        }

        return destinations;
    }

    @Override
    public void insert(Destination destination) {
        String query = "INSERT INTO " + keyspace + ".destinations (destination_id, country, description, name, weather) VALUES (?, ?, ?, ?, ?)";
        session.execute(query, destination.getDestinationId(), destination.getCountry(), destination.getDescription(), destination.getName(), destination.getWeather());
    }

    @Override
    public void update(Destination destination) {
        String query = "UPDATE " + keyspace + ".destinations SET country = ?, description = ?, name = ?, weather = ? WHERE destination_id = ?";
        session.execute(query, destination.getCountry(), destination.getDescription(), destination.getName(), destination.getWeather(), destination.getDestinationId());
    }

    @Override
    public void delete(UUID destinationId) {
        String query = "DELETE FROM " + keyspace + ".destinations WHERE destination_id = ?";
        session.execute(query, destinationId);
    }

    @Override
    public Set<Destination> getDestinationsByWeather(String weather) {
        Set<Destination> destinations = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".destinations WHERE weather = ?";
        ResultSet resultSet = session.execute(query, weather);

        for (Row row : resultSet) {
            Destination destination = mapRowToDestination(row);
            destinations.add(destination);
        }

        return destinations;
    }

    @Override
    public Set<Destination> getDestinationsByCountry(String country) {
        Set<Destination> destinations = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".destinations WHERE country = ?";
        ResultSet resultSet = session.execute(query, country);

        for (Row row : resultSet) {
            Destination destination = mapRowToDestination(row);
            destinations.add(destination);
        }

        return destinations;
    }

    @Override
    public Set<Destination> getDestinationsByName(String name) {
        Set<Destination> destinations = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".destinations WHERE name = ?";
        ResultSet resultSet = session.execute(query, name);

        for (Row row : resultSet) {
            Destination destination = mapRowToDestination(row);
            destinations.add(destination);
        }

        return destinations;
    }

    private Destination mapRowToDestination(Row row) {
        UUID destinationId = row.getUUID("destination_id");
        String name = row.getString("name");
        String country = row.getString("country");
        String description = row.getString("description");
        String weather = row.getString("weather");

        return new Destination(destinationId, name, country, description, weather);
    }

}
