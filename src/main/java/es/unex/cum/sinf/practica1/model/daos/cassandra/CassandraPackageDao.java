package es.unex.cum.sinf.practica1.model.daos.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import es.unex.cum.sinf.practica1.model.daos.PackageDao;
import es.unex.cum.sinf.practica1.model.entities.TravelPackage;
import org.apache.cassandra.exceptions.CassandraException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CassandraPackageDao implements PackageDao {
    private Session session;

    public CassandraPackageDao(Session session) {
        this.session = session;
    }

    @Override
    public TravelPackage get(UUID packageId) {
        String query = "SELECT * FROM pablo_setrakian_bearzotti.packages " +
                       "WHERE package_id = ?";
        ResultSet resultSet = session.execute(query, packageId);
        Row row = resultSet.one();
        TravelPackage pkg = null;

        if (row != null) {
            pkg = new TravelPackage(row.getUUID("package_id"), row.getString("name"),
                    row.getUUID("destination_id"),
                    row.getInt("duration"),
                    row.getDecimal("price"));
            return pkg;
        }
        else {
            return null;
        }
    }

    @Override
    public List<TravelPackage> getAll() {
        List<TravelPackage> packages = new ArrayList<>();

        String query = "SELECT * FROM pablo_setrakian_bearzotti.packages";
        ResultSet resultSet = session.execute(query);

        for (Row row : resultSet) {
            TravelPackage aPackage = new TravelPackage(
                                row.getUUID("package_id"),
                    row.getString("name"),
                    row.getUUID("destination_id"),
                    row.getInt("duration"),
                    row.getDecimal("price")
                        );
            packages.add(aPackage);
        }

        return packages;
    }

    public void insert(TravelPackage aPackage) {
        String query = "INSERT INTO pablo_setrakian_bearzotti.packages (package_id, destination_id, duration, name, price) VALUES (?, ?, ?, ?, ?)";
        session.execute(query, aPackage.getPackageId(), aPackage.getDestinationId(), aPackage.getDuration(), aPackage.getName(), aPackage.getPrice());
    }


    public void update(TravelPackage aPackage) {
        String query = "UPDATE pablo_setrakian_bearzotti.packages SET destination_id = ?, duration = ?, name = ?, price = ? WHERE package_id = ?";
        session.execute(query, aPackage.getDestinationId(), aPackage.getDuration(), aPackage.getName(), aPackage.getPrice(), aPackage.getPackageId());
    }

    @Override
    public void delete(UUID packageId) {
        String query = "DELETE FROM pablo_setrakian_bearzotti.packages WHERE package_id = ?";
        session.execute(query, packageId);
    }

    @Override
    public List<TravelPackage> getPackagesByName(String name){
        List<TravelPackage> packageList = new ArrayList<>();

        String query = "SELECT * FROM pablo_setrakian_bearzotti.packages " +
                       "WHERE name = ?";
        ResultSet resultSet = session.execute(query, name);

        for (Row row : resultSet) {
            TravelPackage aPackage = new TravelPackage(
                    row.getUUID("package_id"),
                    row.getString("name"),
                    row.getUUID("destination_id"),
                    row.getInt("duration"),
                    row.getDecimal("price")
            );
            packageList.add(aPackage);
        }

        return packageList;
    }

    @Override
    public List<TravelPackage> getPackagesByDestinationId(UUID destinationId) {
        List<TravelPackage> packageList = new ArrayList<>();

        String query = "SELECT * FROM pablo_setrakian_bearzotti.packages " +
                       "WHERE destination_id = ?";
        ResultSet resultSet = session.execute(query, destinationId);

        for (Row row : resultSet) {
            TravelPackage aPackage = new TravelPackage(
                    row.getUUID("package_id"),
                    row.getString("name"),
                    row.getUUID("destination_id"),
                    row.getInt("duration"),
                    row.getDecimal("price")
            );
            packageList.add(aPackage);
        }

        return packageList;
    }
}
