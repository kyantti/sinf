package es.unex.cum.sinf.practica1.model.daos.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import es.unex.cum.sinf.practica1.model.daos.PackageSummaryDao;
import es.unex.cum.sinf.practica1.model.entities.PackageReservationSummary;

import java.util.*;

public class CassandraPackageSummaryDao implements PackageSummaryDao {
    private Session session;
    private String keyspace;

    public CassandraPackageSummaryDao(Session session, String keyspace){
        this.session = session;
        this.keyspace = keyspace;
    }

    @Override
    public PackageReservationSummary get(UUID uuid) {
        String query = "SELECT * FROM " + keyspace + ".package_summary WHERE package_id = ?";
        ResultSet resultSet = session.execute(query, uuid);
        Row row = resultSet.one();
        if (row != null) {
            return mapRowToPackageReservationSummary(row);
        }
        return null;
    }

    @Override
    public Set<PackageReservationSummary> getAll() {
        String query = "SELECT * FROM " + keyspace + ".package_summary";
        ResultSet resultSet = session.execute(query);
        Set<PackageReservationSummary> summaryList = new HashSet<>();
        for (Row row : resultSet) {
            PackageReservationSummary summary = mapRowToPackageReservationSummary(row);
            summaryList.add(summary);
        }
        return summaryList;
    }

    @Override
    public void insert(PackageReservationSummary packageReservationSummary) {
        //INSERT statements are not allowed on counter tables, use UPDATE instead
        update(packageReservationSummary);
    }

    @Override
    public void update(PackageReservationSummary packageReservationSummary) {
        String query = "UPDATE " + keyspace + ".package_summary SET total_reservations = total_reservations + ? WHERE package_id = ?";
        session.execute(query, packageReservationSummary.getTotalReservations(), packageReservationSummary.getPackageId());
    }

    @Override
    public void delete(UUID uuid) {
        String query = "DELETE FROM " + keyspace + ".package_summary WHERE package_id = ?";
        session.execute(query, uuid);
    }

    private PackageReservationSummary mapRowToPackageReservationSummary(Row row) {
        UUID packageId = row.getUUID("package_id");
        long totalReservations = row.getLong("total_reservations");

        return new PackageReservationSummary(packageId, totalReservations);
    }

}
