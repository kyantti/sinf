package es.unex.cum.sinf.practica1;

import com.datastax.driver.core.Session;
import com.mongodb.client.MongoDatabase;
import es.unex.cum.sinf.practica1.daos.mongoDb.MongoDbDaoManager;
import es.unex.cum.sinf.practica1.databaseConnection.Connection;
import es.unex.cum.sinf.practica1.databaseConnection.mongoDb.MongoDbConnection;
import es.unex.cum.sinf.practica1.daos.DaoManager;
import es.unex.cum.sinf.practica1.daos.cassandra.CassandraDaoManager;
import es.unex.cum.sinf.practica1.databaseConnection.cassandra.CassandraConnection;
import es.unex.cum.sinf.practica1.datagenerator.DatabasePopulator;
import es.unex.cum.sinf.practica1.entities.*;
import org.junit.After;
import org.junit.Before;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;

public class Test {
    private DatabasePopulator databasePopulator;
    private Connection cassandraConnection;
    private Connection mongoDbConnection;
    private DaoManager cassandraDaoManager;
    private DaoManager mongoDbDaoManager;
    private Random random;

    @Before
    public void setUp() {

        databasePopulator = new DatabasePopulator(50, 2, 200, 500);

        cassandraConnection = new CassandraConnection();
        cassandraConnection.open("127.0.0.1");
        databasePopulator.emptyDatabase(cassandraConnection);
        Session session = ((CassandraConnection) cassandraConnection).getSession();
        String keyspace = "pablo_setrakian_bearzotti";
        cassandraDaoManager = new CassandraDaoManager(session, keyspace);

        databasePopulator.populateWithDestinations(cassandraDaoManager);
        databasePopulator.populateWithPackagesPerDestination(cassandraDaoManager);
        databasePopulator.populateWithClients(cassandraDaoManager);
        databasePopulator.populateWithReservationsOfEachPackagePerClient(cassandraDaoManager);

        mongoDbConnection = new MongoDbConnection();
        mongoDbConnection.open("localhost");
        MongoDbConnection mongoConnection = (MongoDbConnection) mongoDbConnection;
        mongoConnection.setDatabase("pablo_setrakian_bearzotti");
        databasePopulator.emptyDatabase(mongoConnection);
        MongoDatabase mongoDatabase = mongoConnection.getDatabase();
        mongoDbDaoManager = new MongoDbDaoManager(mongoDatabase);

        databasePopulator.populateWithDestinations(mongoDbDaoManager);
        databasePopulator.populateWithPackagesPerDestination(mongoDbDaoManager);
        databasePopulator.populateWithClients(mongoDbDaoManager);
        databasePopulator.populateWithReservationsOfEachPackagePerClient(mongoDbDaoManager);

        random = new Random();

    }

    private <T> void printSetElements(Set<T> set) {
        for (T element : set) {
            System.out.println(element);
        }
    }

    @org.junit.Test
    public void testGetAllDestinations() {
        Set<Destination> cassandraSet = Main.getAllDestinations(cassandraDaoManager);
        Set<Destination> mongoDbSet = Main.getAllDestinations(mongoDbDaoManager);

        System.out.println("Getting all destinations...");
        System.out.println("Cassandra Set: ");
        printSetElements(cassandraSet);
        System.out.println("MongoDb Set:");
        printSetElements(mongoDbSet);

        assertEquals(cassandraSet, mongoDbSet);

    }

    @org.junit.Test
    public void testGetPackageById() {

        int randomIndex = random.nextInt(databasePopulator.getPackages().size());
        UUID randomPackageId = databasePopulator.getPackages().get(randomIndex).getPackageId();
        TravelPackage cassandraPackage = Main.getPackageById(cassandraDaoManager, randomPackageId);
        TravelPackage mongoDbPackage = Main.getPackageById(mongoDbDaoManager, randomPackageId);

        System.out.println("Getting a package by id...");
        System.out.println("Package Id: " + randomPackageId);
        System.out.println("Cassandra Package:\n" + cassandraPackage.toString());
        System.out.println("MongoDb Package:\n" + mongoDbPackage.toString());

        assertEquals(cassandraPackage, mongoDbPackage);
    }

    @org.junit.Test
    public void testGetReservationsByClientId() {

        int randomIndex = random.nextInt(databasePopulator.getClients().size());
        UUID randomClientId = databasePopulator.getClients().get(randomIndex).getClientId();
        Set<Reservation> cassandraSet = Main.getReservationsByClientId(cassandraDaoManager, randomClientId);
        Set<Reservation> mongoDbSet = Main.getReservationsByClientId(mongoDbDaoManager, randomClientId);

        System.out.println("Getting reservations by client id...");
        System.out.println("Client Id: " + randomClientId);
        System.out.println("Cassandra reservations: ");
        printSetElements(cassandraSet);
        System.out.println("MongoDb reservations: ");
        printSetElements(mongoDbSet);

        assertEquals(cassandraSet, mongoDbSet);
    }

    @org.junit.Test
    public void testGetPackagesByDestinationId() {

        int randomIndex = random.nextInt(databasePopulator.getDestinationList().size());
        UUID randomDestinationId = databasePopulator.getDestinationList().get(randomIndex).getDestinationId();
        Set<TravelPackage> cassandraSet = Main.getPackagesByDestinationId(cassandraDaoManager, randomDestinationId);
        Set<TravelPackage> mongoDbSet = Main.getPackagesByDestinationId(mongoDbDaoManager, randomDestinationId);

        System.out.println("Getting packages by destination id...");
        System.out.println("Destination Id: " + randomDestinationId);
        System.out.println("Cassandra packages: ");
        printSetElements(cassandraSet);
        System.out.println("MongoDb packages: ");
        printSetElements(mongoDbSet);

        assertEquals(cassandraSet, mongoDbSet);
    }

    @org.junit.Test
    public void testGetClientsByReservationDateRange() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.of(2023, 11, 25);
        Set<Client> cassandraSet = Main.getClientsByReservationDateRange(cassandraDaoManager, startDate, endDate);
        Set<Client> mongoDbSet = Main.getClientsByReservationDateRange(mongoDbDaoManager, startDate, endDate);

        System.out.println("Getting clients by reservation date range...");
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        System.out.println("Cassandra clients: ");
        printSetElements(cassandraSet);
        System.out.println("MongoDb clients: ");
        printSetElements(mongoDbSet);

        assertEquals(cassandraSet, mongoDbSet);
    }

    @org.junit.Test
    public void testGetPackagesByName() {

        int randomIndex = random.nextInt(databasePopulator.getPackages().size());
        String randomName = databasePopulator.getPackages().get(randomIndex).getName();
        Set<TravelPackage> cassandraSet = cassandraDaoManager.getPackageDao().getPackagesByName(randomName);
        Set<TravelPackage> mongoDbSet = mongoDbDaoManager.getPackageDao().getPackagesByName(randomName);

        System.out.println("Getting packages by name...");
        System.out.println("Name: " + randomName);
        System.out.println("Cassandra packages: ");
        printSetElements(cassandraSet);
        System.out.println("MongoDb packages: ");
        printSetElements(mongoDbSet);

        assertEquals(cassandraSet, mongoDbSet);
    }

    @org.junit.Test
    public void testGetPackageReservationSummary() {
        Set<PackageReservationSummary> cassandraSummary = cassandraDaoManager.getPackageSummaryDao().getAll();
        Set<PackageReservationSummary> mongoDbSummary = mongoDbDaoManager.getPackageSummaryDao().getAll();

        System.out.println("Getting package reservation summary...");
        System.out.println("Cassandra summary:\n" + cassandraSummary);
        System.out.println("MongoDb summary:\n" + mongoDbSummary);

        assertEquals(cassandraSummary, mongoDbSummary);
    }

    @org.junit.Test
    public void testGetClientsWithReservationsInSpecificClimate() {

        int randomIndex = random.nextInt(databasePopulator.getDestinationList().size());
        String randomClimate = databasePopulator.getDestinationList().get(randomIndex).getWeather();
        Set<Client> cassandraSet = Main.getClientsWithReservationsInSpecificClimate(cassandraDaoManager, randomClimate);
        Set<Client> mongoDbSet = Main.getClientsWithReservationsInSpecificClimate(mongoDbDaoManager, randomClimate);

        System.out.println("Getting clients with reservations in specific climate...");
        System.out.println("Climate: " + randomClimate);
        System.out.println("Cassandra clients: ");
        printSetElements(cassandraSet);
        System.out.println("MongoDb clients: ");
        printSetElements(mongoDbSet);

        assertEquals(cassandraSet, mongoDbSet);
    }

    @org.junit.Test
    public void testGetReservationsByClientAndDateRange() {

        int randomIndex = random.nextInt(databasePopulator.getClients().size());
        Reservation randomReservation = databasePopulator.getReservations().get(randomIndex);
        UUID clientId = randomReservation.getClientId();
        LocalDate startDate = randomReservation.getStartDate();
        LocalDate endDate = randomReservation.getEndDate();
        Set<Reservation> cassandraSet = Main.getReservationsByClientIdAndDateRange(cassandraDaoManager, clientId, startDate, endDate);
        Set<Reservation> mongoDbSet = Main.getReservationsByClientIdAndDateRange(mongoDbDaoManager, clientId, startDate, endDate);

        System.out.println("Getting reservations by client and date range...");
        System.out.println("Client Id: " + clientId);
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        System.out.println("Cassandra reservations: ");
        printSetElements(cassandraSet);
        System.out.println("MongoDb reservations: ");
        printSetElements(mongoDbSet);

        assertEquals(cassandraSet, mongoDbSet);
    }

    @org.junit.Test
    public void testGetDestinationsByCountry() {

        int randomIndex = random.nextInt(databasePopulator.getDestinationList().size());
        String randomCountry = databasePopulator.getDestinationList().get(randomIndex).getCountry();
        Set<Destination> cassandraSet = Main.getDestinationsByCountry(cassandraDaoManager, randomCountry);
        Set<Destination> mongoDbSet = Main.getDestinationsByCountry(mongoDbDaoManager, randomCountry);

        System.out.println("Getting destinations by country...");
        System.out.println("Country: " + randomCountry);
        System.out.println("Cassandra destinations: ");
        printSetElements(cassandraSet);
        System.out.println("MongoDb destinations: ");
        printSetElements(mongoDbSet);

        assertEquals(cassandraSet, mongoDbSet);
    }

    @org.junit.Test
    public void testGetMostPopularDestinations() {
        List<Destination> cassandraList = Main.getMostPopularDestinations(cassandraDaoManager);
        List<Destination> mongoDbList = Main.getMostPopularDestinations(mongoDbDaoManager);

        System.out.println("Getting 10 most popular destinations...");
        System.out.println("Cassandra destinations: ");
        for (Destination destination: cassandraList) {
            System.out.println(destination);
        }
        System.out.println("MongoDb destinations: ");
        for (Destination destination: mongoDbList) {
            System.out.println(destination);
        }

        assertEquals(cassandraList, mongoDbList);
    }

    @org.junit.Test
    public void testGetClientByEmail() {

        int randomIndex = random.nextInt(databasePopulator.getClients().size());
        String randomEmail = databasePopulator.getClients().get(randomIndex).getEmail();
        Client cassandraClient = Main.getClientByEmail(cassandraDaoManager, randomEmail);
        Client mongoDbClient = Main.getClientByEmail(mongoDbDaoManager, randomEmail);

        System.out.println("Getting client by email...");
        System.out.println("Email: " + randomEmail);
        System.out.println("Cassandra client:\n" + cassandraClient);
        System.out.println("MongoDb client:\n" + mongoDbClient);

        assertEquals(cassandraClient, mongoDbClient);
    }

    @org.junit.Test
    public void testGetPackagesByDestinationName() {

        int randomIndex = random.nextInt(databasePopulator.getDestinationList().size());
        String randomDestinationName = databasePopulator.getDestinationList().get(randomIndex).getName();
        Set<TravelPackage> cassandraSet = Main.getPackagesByDestinationName(cassandraDaoManager, randomDestinationName);
        Set<TravelPackage> mongoDbSet = Main.getPackagesByDestinationName(mongoDbDaoManager, randomDestinationName);

        System.out.println("Getting packages by destination name...");
        System.out.println("Destination Name: " + randomDestinationName);
        System.out.println("Cassandra packages: ");
        printSetElements(cassandraSet);
        System.out.println("MongoDb packages: ");
        printSetElements(mongoDbSet);

        assertEquals(cassandraSet, mongoDbSet);
    }

    @org.junit.Test
    public void testGetPackagesByDestinationIdAndDuration() {

        int randomIndex = random.nextInt(databasePopulator.getDestinationList().size());
        TravelPackage randomPackage = databasePopulator.getPackages().get(randomIndex);
        UUID destinationId = randomPackage.getDestinationId();
        int duration = randomPackage.getDuration();
        Set<TravelPackage> cassandraSet = Main.getPackagesByDestinationIdAndDuration(cassandraDaoManager, destinationId, duration);
        Set<TravelPackage> mongoDbSet = Main.getPackagesByDestinationIdAndDuration(mongoDbDaoManager, destinationId, duration);

        System.out.println("Getting packages by destination id and duration...");
        System.out.println("Destination Id: " + destinationId);
        System.out.println("Duration: " + duration);
        System.out.println("Cassandra packages: ");
        printSetElements(cassandraSet);
        System.out.println("MongoDb packages: ");
        printSetElements(mongoDbSet);

        assertEquals(cassandraSet, mongoDbSet);
    }

    @org.junit.Test
    public void getReservationsByClientIdAndDestinationIdAndPaymentStatus() {

        int randomIndex = random.nextInt(databasePopulator.getClients().size());
        Reservation randomReservation = databasePopulator.getReservations().get(randomIndex);
        UUID clientId = randomReservation.getClientId();
        UUID packageId = randomReservation.getPackageId();
        TravelPackage travelPackage = Main.getPackageById(cassandraDaoManager, packageId);
        UUID destinationId = travelPackage.getDestinationId();
        boolean paymentStatus = randomReservation.isPayed();
        Set<Reservation> cassandraSet = Main.getReservationsByClientIdAndDestinationIdAndPaymentStatus(cassandraDaoManager, clientId, destinationId, paymentStatus);
        Set<Reservation> mongoDbSet = Main.getReservationsByClientIdAndDestinationIdAndPaymentStatus(mongoDbDaoManager, clientId, destinationId, paymentStatus);

        System.out.println("Getting reservations by client id, destination id and payment status...");
        System.out.println("Client Id: " + clientId);
        System.out.println("Destination Id: " + destinationId);
        System.out.println("Payed: " + paymentStatus);
        System.out.println("Cassandra reservations: ");
        printSetElements(cassandraSet);
        System.out.println("MongoDb reservations: ");
        printSetElements(mongoDbSet);

        assertEquals(cassandraSet, mongoDbSet);
    }

    @After
    public void tearDown() {
        cassandraConnection.close();
        mongoDbConnection.close();
    }
}
