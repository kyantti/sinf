package es.unex.cum.sinf.practica1.model.daos.cassandra;

import com.datastax.driver.core.Session;
import es.unex.cum.sinf.practica1.model.daos.*;

public class CassandraDaoManager implements DaoManager {
    private Session session;
    private ClientDao cassandraClientDao = null;
    private DestinationDao cassandraDestinationDao = null;
    private PackageDao cassandraPackageDao = null;
    private ReservationDao cassandraReservationDao = null;

    public CassandraDaoManager(Session session) {
        this.session = session;
    }
    @Override
    public ClientDao getClientDao() {
        if (cassandraClientDao == null){
            return new CassandraClientDao(session);
        }
        return cassandraClientDao;
    }
    @Override
    public DestinationDao getDestinationDao() {
        if (cassandraDestinationDao == null) {
            cassandraDestinationDao = new CassandraDestinationDao(session);
        }
        return cassandraDestinationDao;
    }
    @Override
    public PackageDao getPackageDao() {
        if (cassandraPackageDao == null) {
            cassandraPackageDao = new CassandraPackageDao(session);
        }
        return cassandraPackageDao;
    }
    @Override
    public ReservationDao getReservationDao() {
        if (cassandraReservationDao == null) {
            cassandraReservationDao = new CassandraReservationDao(session);
        }
        return cassandraReservationDao;
    }
}
