package es.unex.cum.sinf.practica1.daos.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import es.unex.cum.sinf.practica1.daos.PackageDao;
import es.unex.cum.sinf.practica1.entities.TravelPackage;

import java.math.BigDecimal;
import java.util.*;

public class CassandraPackageDao implements PackageDao {
    private final Session session;
    private final String keyspace;

    public CassandraPackageDao(Session session, String keyspace) {
        this.session = session;
        this.keyspace = keyspace;
    }

    @Override
    public TravelPackage get(UUID packageId) {
        String query = "SELECT * FROM " + keyspace + ".packages WHERE package_id = ?";
        ResultSet resultSet = session.execute(query, packageId);
        Row row = resultSet.one();

        if (row != null) {
            return mapRowToTravelPackage(row);
        } else {
            return null;
        }
    }

    @Override
    public Set<TravelPackage> getAll() {
        Set<TravelPackage> packages = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".packages";
        ResultSet resultSet = session.execute(query);

        for (Row row : resultSet) {
            TravelPackage aPackage = mapRowToTravelPackage(row);
            packages.add(aPackage);
        }

        return packages;
    }

    public void insert(TravelPackage aPackage) {
        String query = "INSERT INTO " + keyspace + ".packages (package_id, destination_id, duration, name, price) VALUES (?, ?, ?, ?, ?)";
        session.execute(query, aPackage.getPackageId(), aPackage.getDestinationId(), aPackage.getDuration(), aPackage.getName(), aPackage.getPrice());
        String query2 = "INSERT INTO " + keyspace + ".packages_by_destination_and_duration (package_id, destination_id, duration, name, price) VALUES (?, ?, ?, ?, ?)";
        session.execute(query2, aPackage.getPackageId(), aPackage.getDestinationId(), aPackage.getDuration(), aPackage.getName(), aPackage.getPrice());
    }


    public void update(TravelPackage aPackage) {
        String query = "UPDATE " + keyspace + ".packages SET destination_id = ?, duration = ?, name = ?, price = ? WHERE package_id = ?";
        session.execute(query, aPackage.getDestinationId(), aPackage.getDuration(), aPackage.getName(), aPackage.getPrice(), aPackage.getPackageId());
    }

    @Override
    public void delete(UUID packageId) {
        String query = "DELETE FROM " + keyspace + ".packages WHERE package_id = ?";
        session.execute(query, packageId);
    }

    @Override
    public Set<TravelPackage> getPackagesByName(String name) {
        Set<TravelPackage> packageList = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".packages WHERE name = ?";
        ResultSet resultSet = session.execute(query, name);

        for (Row row : resultSet) {
            TravelPackage aPackage = mapRowToTravelPackage(row);
            packageList.add(aPackage);
        }

        return packageList;
    }

    @Override
    public Set<TravelPackage> getPackagesByDestinationId(UUID destinationId) {
        Set <TravelPackage> packageList = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".packages WHERE destination_id = ?";
        ResultSet resultSet = session.execute(query, destinationId);

        for (Row row : resultSet) {
            TravelPackage aPackage = mapRowToTravelPackage(row);
            packageList.add(aPackage);
        }

        return packageList;
    }

    @Override
    public Set<TravelPackage> getPackagesByDestinationIdAndDuration(UUID destinationId, int duration) {
        Set <TravelPackage> packageList = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".packages_by_destination_and_duration WHERE destination_id = ? AND duration = ?";
        ResultSet resultSet = session.execute(query, destinationId, duration);

        for (Row row : resultSet) {
            TravelPackage aPackage = mapRowToTravelPackage(row);
            packageList.add(aPackage);
        }

        return packageList;
    }

    private TravelPackage mapRowToTravelPackage(Row row) {
        UUID packageId = row.getUUID("package_id");
        String name = row.getString("name");
        UUID destinationId = row.getUUID("destination_id");
        int duration = row.getInt("duration");
        BigDecimal price = row.getDecimal("price");

        return new TravelPackage(packageId, name, destinationId, duration, price);
    }
}
