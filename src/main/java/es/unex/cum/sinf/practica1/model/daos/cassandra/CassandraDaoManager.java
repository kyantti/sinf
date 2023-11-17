package es.unex.cum.sinf.practica1.model.daos.cassandra;

import com.datastax.driver.core.Session;
import es.unex.cum.sinf.practica1.model.daos.*;

public class CassandraDaoManager implements DaoManager {
    private Session session;
    private String keyspace;
    private ClientDao cassandraClientDao = null;
    private DestinationDao cassandraDestinationDao = null;
    private PackageDao cassandraPackageDao = null;
    private ReservationDao cassandraReservationDao = null;
    private PackageSummaryDao cassandraPackageSummaryDao = null;

    public CassandraDaoManager(Session session, String keyspace) {
        this.session = session;
        this.keyspace = keyspace;
    }
    @Override
    public ClientDao getClientDao() {
        if (cassandraClientDao == null){
            return new CassandraClientDao(session, keyspace);
        }
        return cassandraClientDao;
    }
    @Override
    public DestinationDao getDestinationDao() {
        if (cassandraDestinationDao == null) {
            cassandraDestinationDao = new CassandraDestinationDao(session, keyspace);
        }
        return cassandraDestinationDao;
    }
    @Override
    public PackageDao getPackageDao() {
        if (cassandraPackageDao == null) {
            cassandraPackageDao = new CassandraPackageDao(session, keyspace);
        }
        return cassandraPackageDao;
    }
    @Override
    public ReservationDao getReservationDao() {
        if (cassandraReservationDao == null) {
            cassandraReservationDao = new CassandraReservationDao(session, keyspace);
        }
        return cassandraReservationDao;
    }

    @Override
    public PackageSummaryDao getPackageSummaryDao() {
        if (cassandraPackageSummaryDao == null) {
            cassandraPackageSummaryDao = new CassandraPackageSummaryDao(session, keyspace);
        }
        return cassandraPackageSummaryDao;
    }
}
