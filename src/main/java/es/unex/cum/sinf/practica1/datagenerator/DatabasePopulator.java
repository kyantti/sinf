package es.unex.cum.sinf.practica1.datagenerator;

import com.datastax.driver.core.Session;
import com.mongodb.client.MongoDatabase;
import es.unex.cum.sinf.practica1.daos.DaoManager;
import es.unex.cum.sinf.practica1.daos.cassandra.CassandraDaoManager;
import es.unex.cum.sinf.practica1.daos.mongoDb.MongoDbDaoManager;
import es.unex.cum.sinf.practica1.databaseConnection.Connection;
import es.unex.cum.sinf.practica1.databaseConnection.cassandra.CassandraConnection;
import es.unex.cum.sinf.practica1.databaseConnection.mongoDb.MongoDbConnection;
import es.unex.cum.sinf.practica1.entities.*;

import java.util.*;

public class DatabasePopulator {
    private final List<Destination> destinationList;
    private final List<TravelPackage> packages;
    private final List<Client> clients;
    private final List<Reservation> reservations;

    public DatabasePopulator(int numOfDestinations, int numOfPackagesPerDestination, int numOfClients, int numOfReservations) {
        DataGenerator dataGenerator = new DataGenerator();
        destinationList = dataGenerator.generateDestinations(numOfDestinations);
        packages = dataGenerator.generatePackagesPerDestination(destinationList, numOfPackagesPerDestination);
        clients = dataGenerator.generateClients(numOfClients);
        reservations = dataGenerator.generateReservations(clients, packages, numOfReservations);
    }

    public List<Destination> getDestinationList() {
        return destinationList;
    }

    public List<TravelPackage> getPackages() {
        return packages;
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void populateWithDestinations(DaoManager daoManager) {
        for (Destination destination : destinationList) {
            daoManager.getDestinationDao().insert(destination);
        }

    }

    public void populateWithPackagesPerDestination(DaoManager daoManager) {
        for (TravelPackage travelPackage : packages) {
            daoManager.getPackageDao().insert(travelPackage);
        }
    }

    public void populateWithClients(DaoManager daoManager) {
        for (Client client : clients) {
            daoManager.getClientDao().insert(client);
        }
    }

    public void populateWithReservationsOfEachPackagePerClient(DaoManager daoManager) {
        Map<UUID, Long> packageIdCounts = new HashMap<>();
        PackageReservationSummary summary;
        UUID packageId;
        long count;

        for (Reservation reservation : reservations) {
            daoManager.getReservationDao().insert(reservation);
            packageId = reservation.getPackageId();
            packageIdCounts.put(packageId, packageIdCounts.getOrDefault(packageId, 0L) + 1);
        }

        for (Map.Entry<UUID, Long> entry : packageIdCounts.entrySet()) {
            packageId = entry.getKey();
            count = entry.getValue();
            summary = new PackageReservationSummary(packageId, count);
            daoManager.getPackageSummaryDao().update(summary);
        }

    }

    private static void truncateTables(Session session) {
        session.execute("TRUNCATE pablo_setrakian_bearzotti.destinations;");
        session.execute("TRUNCATE pablo_setrakian_bearzotti.packages;");
        session.execute("TRUNCATE pablo_setrakian_bearzotti.clients;");
        session.execute("TRUNCATE pablo_setrakian_bearzotti.reservations;");
        session.execute("TRUNCATE pablo_setrakian_bearzotti.package_summary;");
        session.execute("TRUNCATE pablo_setrakian_bearzotti.packages_by_destination_and_duration;");
        session.execute("TRUNCATE pablo_setrakian_bearzotti.reservations_by_client_package_payed;");
    }

    public void emptyDatabase(Connection connection) {
        if (connection instanceof CassandraConnection) {
            CassandraConnection cassandraConnection = (CassandraConnection) connection;
            Session session = cassandraConnection.getSession();
            truncateTables(session);
        } else if (connection instanceof MongoDbConnection) {
            MongoDbConnection mongoConnection = (MongoDbConnection) connection;
            MongoDatabase mongoDatabase = mongoConnection.getDatabase();
            mongoDatabase.drop();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;
        DaoManager daoManager = null;
        DatabasePopulator databasePopulator = new DatabasePopulator(10, 2, 20, 20);

        while (true) {
            System.out.println("Seleccione la base de datos a llenar:");
            System.out.println("1. Cassandra");
            System.out.println("2. MongoDB");
            System.out.println("3. Salir");

            int dataBaseChoice = scanner.nextInt();

            switch (dataBaseChoice) {
                case 1:
                    connection = new CassandraConnection();
                    connection.open("127.0.0.1");
                    databasePopulator.emptyDatabase(connection);
                    Session session = ((CassandraConnection) connection).getSession();
                    String keyspace = "pablo_setrakian_bearzotti";
                    daoManager = new CassandraDaoManager(session, keyspace);
                    break;
                case 2:
                    connection = new MongoDbConnection();
                    connection.open("localhost");
                    MongoDbConnection mongoConnection = (MongoDbConnection) connection;
                    mongoConnection.setDatabase("pablo_setrakian_bearzotti");
                    databasePopulator.emptyDatabase(connection);
                    MongoDatabase mongoDatabase = mongoConnection.getDatabase();
                    daoManager = new MongoDbDaoManager(mongoDatabase);
                    break;
                case 3:
                    scanner.close();
                    if (connection != null) {
                        connection.close();
                    }
                    System.exit(0);
                    break;
                default:
                    System.out.println("Categoría no válida. Por favor, seleccione una categoría válida.");
                    continue; // Go back to the start of the loop
            }

            databasePopulator.populateWithDestinations(daoManager);
            databasePopulator.populateWithPackagesPerDestination(daoManager);
            databasePopulator.populateWithClients(daoManager);
            databasePopulator.populateWithReservationsOfEachPackagePerClient(daoManager);

            System.out.println("Base de datos poblada exitosamente. ¿Desea seleccionar otra base de datos? (s/n)");
            String anotherDatabase = scanner.next().toLowerCase();
            if (!anotherDatabase.equals("s")) {
                scanner.close();
                connection.close();
                System.exit(0);
            }
        }
    }
}
