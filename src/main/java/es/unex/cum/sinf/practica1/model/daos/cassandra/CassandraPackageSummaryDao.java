package es.unex.cum.sinf.practica1.model.daos.cassandra;

import com.datastax.driver.core.Session;
import es.unex.cum.sinf.practica1.model.daos.PackageSummaryDao;
import es.unex.cum.sinf.practica1.model.entities.TravelPackage;

import java.util.List;
import java.util.UUID;

public class CassandraPackageSummaryDao implements PackageSummaryDao {
    private Session session;

    public CassandraPackageSummaryDao(Session session){
        this.session = session;
    }

    @Override
    public TravelPackage get(Integer integer) {
        return null;
    }

    @Override
    public List<TravelPackage> getAll() {
        return null;
    }

    @Override
    public void insert(TravelPackage travelPackage) {
        String query = "INSERT INTO pablo_setrakian_bearzotti.packages (package_id, total_reservations VALUES (?, ?)";
        session.execute(query,travelPackage.getPackageId(), 1);
    }

    @Override
    public void update(TravelPackage travelPackage) {

    }

    @Override
    public void delete(Integer integer) {

    }
}
