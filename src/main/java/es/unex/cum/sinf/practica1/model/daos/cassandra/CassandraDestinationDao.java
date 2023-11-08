package es.unex.cum.sinf.practica1.model.daos.cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import es.unex.cum.sinf.practica1.model.daos.DestinationDao;
import es.unex.cum.sinf.practica1.model.entities.Destination;

public class CassandraDestinationDao implements DestinationDao {

    private Session session;

    public CassandraDestinationDao(Session session) {
        this.session = session;
    }

    @Override
    public Destination get(UUID k) {
        String query = "SELECT * FROM pablo_setrakian_bearzotti.destinations WHERE destination_id = ?";
        ResultSet resultSet = session.execute(query, k);
        Row row = resultSet.one();

        if (row != null) {
            return new Destination(
                row.getUUID("destination_id"),
                row.getString("country"),
                row.getString("description"),
                row.getString("name"),
                row.getString("weather"));
        } else {
            return null;
        }
    }

    @Override
    public List<Destination> getAll() {
        List<Destination> destinations = new ArrayList<>();

        String query = "SELECT * FROM pablo_setrakian_bearzotti.destinations";
        ResultSet resultSet = session.execute(query);

        for (Row row : resultSet) {
            Destination destination = new Destination(
                    row.getUUID("destination_id"),
                    row.getString("country"),
                    row.getString("description"),
                    row.getString("name"),
                    row.getString("weather"));
            destinations.add(destination);
        }

        return destinations;
    }

    @Override
    public void insert(Destination t) {
        String query = "INSERT INTO pablo_setrakian_bearzotti.destinations (destination_id, country, description, name, weather)" +
                       "VALUES (?, ?, ?, ?, ?)";
        session.execute(query, t.getDestinationId(), t.getCountry(), t.getDescription(), t.getName(), t.getWeather());
    }

    @Override
    public void update(Destination t) {
        String query = "UPDATE pablo_setrakian_bearzotti.destinations " +
                       "SET country = ?, description = ?, name = ?, weather = ?" +
                       "WHERE destination_id = ?";
        session.execute(query, t.getCountry(), t.getDescription(), t.getName(), t.getWeather(), t.getDestinationId());
    }

    @Override
    public void delete(UUID k) {
        String query = "DELETE FROM pablo_setrakian_bearzotti.destinations" +
                       "WHERE destination_id = ?";
        session.execute(query, k);
    }

    @Override
    public List<Destination> getPopularDestinations() {
        List<Destination> popularDestinations = new ArrayList<>();
        UUID destinationId = null;
        Destination destination = null;

        String query = "SELECT destination_id, COUNT(*) as reservation_count " +
                       "FROM pablo_setrakian_bearzotti.reservations " +
                       "GROUP BY destination_id " +
                       "ORDER BY reservation_count DESC";

        ResultSet resultSet = session.execute(query);

        for (Row row : resultSet) {
            destinationId = row.getUUID("destination_id");
            destination = get(destinationId);
            popularDestinations.add(destination);
        }

        return popularDestinations;
    }

}
