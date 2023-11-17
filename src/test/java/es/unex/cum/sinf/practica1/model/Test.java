package es.unex.cum.sinf.practica1.model;

import com.datastax.driver.core.Session;
import com.mongodb.client.MongoDatabase;
import es.unex.cum.sinf.practica1.model.daos.DaoManager;
import es.unex.cum.sinf.practica1.model.daos.cassandra.CassandraDaoManager;
import es.unex.cum.sinf.practica1.model.daos.mongoDb.MongoDbDaoManager;
import es.unex.cum.sinf.practica1.model.databaseConnection.Connection;
import es.unex.cum.sinf.practica1.model.databaseConnection.cassandra.CassandraConnection;
import es.unex.cum.sinf.practica1.model.databaseConnection.mongoDb.MongoDbConnection;
import es.unex.cum.sinf.practica1.model.datagenerator.DatabasePopulator;
import es.unex.cum.sinf.practica1.model.entities.*;
import org.junit.After;
import org.junit.Before;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class Test {
    private DatabasePopulator databasePopulator;
    private Connection cassandraConnection;
    private Connection mongoDbConnection;
    private DaoManager cassandraDaoManager;
    private DaoManager mongoDbDaoManager;
    @Before
    public void setUp() {

        databasePopulator = new DatabasePopulator(10, 2, 20, 20);

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

    }

    @org.junit.Test
    public void testGetAllDestinations() {
        Set<Destination> cassandraSet = Main.getAllDestinations(cassandraDaoManager);
        Set<Destination> mongoDbSet = Main.getAllDestinations(mongoDbDaoManager);

        System.out.println("Getting all destinations...");
        System.out.println("Cassandra Set: " + cassandraSet.toString());
        System.out.println("MongoDb Set:" + mongoDbSet.toString());

        assertTrue(cassandraSet.equals(mongoDbSet));

    }
    @org.junit.Test
    public void testGetPackageById(){
        Random random = new Random();
        int randomIndex = random.nextInt(databasePopulator.getPackages().size());
        UUID randomPackageId = databasePopulator.getPackages().get(randomIndex).getPackageId();
        TravelPackage cassandraPackage = Main.getPackageById(cassandraDaoManager, randomPackageId);
        TravelPackage mongoDbPackage = Main.getPackageById(mongoDbDaoManager, randomPackageId);

        System.out.println("Getting a package by id...");
        System.out.println("Package Id: " + randomPackageId);
        System.out.println("Cassandra Package: " + cassandraPackage.toString());
        System.out.println("MongoDb Package: " + mongoDbPackage.toString());

        assertTrue(cassandraPackage.equals(mongoDbPackage));
    }
    @org.junit.Test
    public void testGetReservationsByClientId(){
        Random random = new Random();
        int randomIndex = random.nextInt(databasePopulator.getClients().size());
        UUID randomClientId = databasePopulator.getClients().get(randomIndex).getClientId();
        Set<Reservation> cassandraSet = Main.getReservationsByClientId(cassandraDaoManager, randomClientId);
        Set<Reservation> mongoDbSet = Main.getReservationsByClientId(mongoDbDaoManager, randomClientId);

        System.out.println("Getting reservations by client id...");
        System.out.println("Client Id: " + randomClientId);
        System.out.println("Cassandra reservations: " + cassandraSet.toString());
        System.out.println("MongoDb reservations: " + mongoDbSet.toString());

        assertTrue(cassandraSet.equals(mongoDbSet));
    }
    @org.junit.Test
    public void testGetPackagesByDestinationId(){
        Random random = new Random();
        int randomIndex = random.nextInt(databasePopulator.getDestinationList().size());
        UUID randomDestinationId = databasePopulator.getDestinationList().get(randomIndex).getDestinationId();
        Set<TravelPackage> cassandraSet = Main.getPackagesByDestinationId(cassandraDaoManager, randomDestinationId);
        Set<TravelPackage> mongoDbSet = Main.getPackagesByDestinationId(mongoDbDaoManager, randomDestinationId);

        System.out.println("Getting packages by destination id...");
        System.out.println("Destination Id: " + randomDestinationId);
        System.out.println("Cassandra packages: " + cassandraSet.toString());
        System.out.println("MongoDb packages: " + mongoDbSet.toString());

        assertTrue(cassandraSet.equals(mongoDbSet));
    }
    @org.junit.Test
    public void testGetClientsByReservationDateRange(){
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.of(2023, 11, 25);
        Set<Client> cassandraSet = Main.getClientsByReservationDateRange(cassandraDaoManager, startDate, endDate);
        Set<Client> mongoDbSet = Main.getClientsByReservationDateRange(mongoDbDaoManager, startDate, endDate);

        System.out.println("Getting clients by reservation date range...");
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        System.out.println("Cassandra clients: " + cassandraSet);
        System.out.println("MongoDb clients: " + mongoDbSet);

        assertTrue(cassandraSet.equals(mongoDbSet));
    }
    @org.junit.Test
    public void testGetPackagesByName(){
        Random random = new Random();
        int randomIndex = random.nextInt(databasePopulator.getPackages().size());
        String randomName = databasePopulator.getPackages().get(randomIndex).getName();
        Set<TravelPackage> cassandraSet = cassandraDaoManager.getPackageDao().getPackagesByName(randomName);
        Set<TravelPackage> mongoDbSet = mongoDbDaoManager.getPackageDao().getPackagesByName(randomName);

        System.out.println("Getting packages by name...");
        System.out.println("Name: " + randomName);
        System.out.println("Cassandra packages: " + cassandraSet);
        System.out.println("MongoDb packages: " + mongoDbSet);

        assertTrue(cassandraSet.equals(mongoDbSet));
    }
    @org.junit.Test
    public void testGetPackageReservationSummary(){
        Set<PackageReservationSummary> cassandraSummary = cassandraDaoManager.getPackageSummaryDao().getAll();
        Set<PackageReservationSummary> mongoDbSummary = mongoDbDaoManager.getPackageSummaryDao().getAll();

        System.out.println("Getting package reservation summary...");
        System.out.println("Cassandra summary: " + cassandraSummary);
        System.out.println("MongoDb summary: " + mongoDbSummary);

        assertTrue(cassandraSummary.equals(mongoDbSummary));
    }
    @org.junit.Test
    public void testGetClientsWithReservationsInSpecificClimate(){
        Random random = new Random();
        int randomIndex = random.nextInt(databasePopulator.getDestinationList().size());
        String randomClimate = databasePopulator.getDestinationList().get(randomIndex).getWeather();
        Set<Client> cassandraSet = Main.getClientsWithReservationsInSpecificClimate(cassandraDaoManager, randomClimate);
        Set<Client> mongoDbSet = Main.getClientsWithReservationsInSpecificClimate(mongoDbDaoManager, randomClimate);

        System.out.println("Getting clients with reservations in specific climate...");
        System.out.println("Climate: " + randomClimate);
        System.out.println("Cassandra clients: " + cassandraSet);
        System.out.println("MongoDb clients: " + mongoDbSet);

        assertTrue(cassandraSet.equals(mongoDbSet));
    }
    @org.junit.Test
    public void testGetReservationsByClientAndDateRange(){
        Random random = new Random();
        int randomIndex = random.nextInt(databasePopulator.getClients().size());
        UUID randomClientId = databasePopulator.getClients().get(randomIndex).getClientId();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.of(2023, 11, 25);
        Set<Reservation> cassandraSet = Main.getReservationsByClientIdAndDateRange(cassandraDaoManager, randomClientId, startDate, endDate);
        Set<Reservation> mongoDbSet = Main.getReservationsByClientIdAndDateRange(mongoDbDaoManager, randomClientId, startDate, endDate);

        System.out.println("Getting reservations by client and date range...");
        System.out.println("Client Id: " + randomClientId);
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        System.out.println("Cassandra reservations: " + cassandraSet);
        System.out.println("MongoDb reservations: " + mongoDbSet);

        assertTrue(cassandraSet.equals(mongoDbSet));
    }
    @org.junit.Test
    public void testGetDestinationsByCountry(){
        Random random = new Random();
        int randomIndex = random.nextInt(databasePopulator.getDestinationList().size());
        String randomCountry = databasePopulator.getDestinationList().get(randomIndex).getCountry();
        Set<Destination> cassandraSet = Main.getDestinationsByCountry(cassandraDaoManager, randomCountry);
        Set<Destination> mongoDbSet = Main.getDestinationsByCountry(mongoDbDaoManager, randomCountry);

        System.out.println("Getting destinations by country...");
        System.out.println("Country: " + randomCountry);
        System.out.println("Cassandra destinations: " + cassandraSet);
        System.out.println("MongoDb destinations: " + mongoDbSet);

        assertTrue(cassandraSet.equals(mongoDbSet));
    }
    @org.junit.Test
    public void testGetMostPopularDestinations(){
        List<Destination> cassandraList = Main.getMostPopularDestinations(cassandraDaoManager);
        List<Destination> mongoDbList = Main.getMostPopularDestinations(mongoDbDaoManager);

        System.out.println("Getting 10 most popular destinations...");
        System.out.println("Cassandra destinations: " + cassandraList);
        System.out.println("MongoDb destinations: " + mongoDbList);

        assertTrue(cassandraList.equals(mongoDbList));
    }
    @org.junit.Test
    public void testGetClientByEmail(){
        Random random = new Random();
        int randomIndex = random.nextInt(databasePopulator.getClients().size());
        String randomEmail = databasePopulator.getClients().get(randomIndex).getEmail();
        Client cassandraClient = Main.getClientByEmail(cassandraDaoManager, randomEmail);
        Client mongoDbClient = Main.getClientByEmail(mongoDbDaoManager, randomEmail);

        System.out.println("Getting client by email...");
        System.out.println("Email: " + randomEmail);
        System.out.println("Cassandra client: " + cassandraClient);
        System.out.println("MongoDb client: " + mongoDbClient);

        assertTrue(cassandraClient.equals(mongoDbClient));
    }
    @org.junit.Test
    public void testGetPackagesByDestinationName(){
        Random random = new Random();
        int randomIndex = random.nextInt(databasePopulator.getDestinationList().size());
        String randomDestinationName = databasePopulator.getDestinationList().get(randomIndex).getName();
        Set<TravelPackage> cassandraSet = Main.getPackagesByDestinationName(cassandraDaoManager, randomDestinationName);
        Set<TravelPackage> mongoDbSet = Main.getPackagesByDestinationName(mongoDbDaoManager, randomDestinationName);

        System.out.println("Getting packages by destination name...");
        System.out.println("Destination Name: " + randomDestinationName);
        System.out.println("Cassandra packages: " + cassandraSet);
        System.out.println("MongoDb packages: " + mongoDbSet);

        assertTrue(cassandraSet.equals(mongoDbSet));
    }
    @org.junit.Test
    public void testGetPackagesByDestinationIdAndDuration(){
        Random random = new Random();
        int randomIndex = random.nextInt(databasePopulator.getDestinationList().size());
        UUID randomDestinationId = databasePopulator.getDestinationList().get(randomIndex).getDestinationId();
        int randomDuration = random.nextInt(10) + 1;
        Set<TravelPackage> cassandraSet = Main.getPackagesByDestinationIdAndDuration(cassandraDaoManager, randomDestinationId, randomDuration);
        Set<TravelPackage> mongoDbSet = Main.getPackagesByDestinationIdAndDuration(mongoDbDaoManager, randomDestinationId, randomDuration);

        System.out.println("Getting packages by destination id and duration...");
        System.out.println("Destination Id: " + randomDestinationId);
        System.out.println("Duration: " + randomDuration);
        System.out.println("Cassandra packages: " + cassandraSet);
        System.out.println("MongoDb packages: " + mongoDbSet);

        assertTrue(cassandraSet.equals(mongoDbSet));
    }
    @After
    public void tearDown() {
        cassandraConnection.close();
        mongoDbConnection.close();
    }
}
