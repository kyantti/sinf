package es.unex.cum.sinf.practica1.daos.mongoDb;

import com.mongodb.client.MongoDatabase;
import es.unex.cum.sinf.practica1.daos.*;

public class MongoDbDaoManager implements DaoManager {
    private final MongoDatabase database;
    private ClientDao mongoClientDao = null;
    private DestinationDao mongoDestinationDao = null;
    private PackageDao mongoPackageDao = null;
    private ReservationDao mongoReservationDao = null;
    private PackageSummaryDao mongoPackageSummaryDao = null;

    public MongoDbDaoManager(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public ClientDao getClientDao() {
        if (mongoClientDao == null) {
            mongoClientDao = new MongoDbClientDao(database);
        }
        return mongoClientDao;
    }

    @Override
    public DestinationDao getDestinationDao() {
        if (mongoDestinationDao == null) {
            mongoDestinationDao = new MongoDbDestinationDao(database);
        }
        return mongoDestinationDao;
    }

    @Override
    public PackageDao getPackageDao() {
        if (mongoPackageDao == null) {
            mongoPackageDao = new MongoDbPackageDao(database);
        }
        return mongoPackageDao;
    }

    @Override
    public ReservationDao getReservationDao() {
        if (mongoReservationDao == null) {
            mongoReservationDao = new MongoDbReservationDao(database);
        }
        return mongoReservationDao;
    }

    @Override
    public PackageSummaryDao getPackageSummaryDao() {
        if (mongoPackageSummaryDao == null) {
            mongoPackageSummaryDao = new MongoDbPackageSummaryDao(database);
        }
        return mongoPackageSummaryDao;
    }
}
