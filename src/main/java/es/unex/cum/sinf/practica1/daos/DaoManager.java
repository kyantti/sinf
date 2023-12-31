package es.unex.cum.sinf.practica1.daos;

public interface DaoManager {
    ClientDao getClientDao();
    DestinationDao getDestinationDao();
    PackageDao getPackageDao();
    ReservationDao getReservationDao();
    PackageSummaryDao getPackageSummaryDao();
}
