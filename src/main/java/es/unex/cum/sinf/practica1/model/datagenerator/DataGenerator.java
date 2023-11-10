package es.unex.cum.sinf.practica1.model.datagenerator;



import com.github.javafaker.Faker;
import es.unex.cum.sinf.practica1.model.entities.Client;
import es.unex.cum.sinf.practica1.model.entities.Destination;
import es.unex.cum.sinf.practica1.model.entities.Reservation;
import es.unex.cum.sinf.practica1.model.entities.TravelPackage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataGenerator{
    private Faker faker;
    public DataGenerator() {
        faker = new Faker();
    }

    public List<Destination> generateDestinations(int desiredNumOfDestinations) {
        List<Destination> destinations = new ArrayList<>();
        Destination destination = null;
        UUID destinationId = null;
        String countryName = null;
        String description = null;
        String destinationName = null;
        String weather = null;

        List <String> possibleDescriptions = new ArrayList<>();
        possibleDescriptions.add("Discover the hidden gems of this picturesque location.");
        possibleDescriptions.add("Immerse yourself in the local traditions and festivities.");
        possibleDescriptions.add("Indulge in the delectable cuisine and flavors of the region.");
        possibleDescriptions.add("Embark on an adventure to explore the wilderness and wildlife.");
        possibleDescriptions.add("Relax in the tranquil surroundings and unwind from the hustle and bustle.");
        possibleDescriptions.add("Experience the vibrant nightlife and entertainment options.");
        possibleDescriptions.add("Hike through the breathtaking mountains and enjoy panoramic views.");
        possibleDescriptions.add("Dive into the crystal-clear waters for exciting underwater adventures.");
        possibleDescriptions.add("Shop for unique souvenirs and handmade crafts in local markets.");
        possibleDescriptions.add("Engage in water sports and thrilling outdoor activities.");
        possibleDescriptions.add("Journey to a land of rich history and ancient ruins.");
        possibleDescriptions.add("Savor the flavors of the region's traditional dishes and street food.");
        possibleDescriptions.add("Uncover the mysteries of local folklore and legends.");
        possibleDescriptions.add("Relish in the vibrant colors of the local festivals and celebrations.");
        possibleDescriptions.add("Roam the charming streets and alleys of the old town.");
        possibleDescriptions.add("Reconnect with nature in the lush forests and botanical gardens.");
        possibleDescriptions.add("Witness the breathtaking sunsets over the horizon.");
        possibleDescriptions.add("Experience the warmth and hospitality of the local people.");
        possibleDescriptions.add("Get lost in the labyrinth of markets and bazaars.");
        possibleDescriptions.add("Partake in guided tours to historical landmarks and landmarks.");


        for (int i = 0; i < desiredNumOfDestinations; i++) {
            destinationId = UUID.randomUUID();
            countryName = faker.country().name();
            description = possibleDescriptions.get(faker.random().nextInt(possibleDescriptions.size()));
            destinationName = faker.country().capital();
            weather = faker.weather().description();
            destination = new Destination(destinationId, destinationName, countryName, description, weather);
            destinations.add(destination);
        }
        return destinations;
    }

    public List<TravelPackage> generatePackages(List<Destination> destinations, int desiredNumOfPackages) {
        List <TravelPackage> packages = new ArrayList<>();
        TravelPackage pkg = null;
        UUID packageId = null;
        String name = null;
        UUID destinationId = null;
        int duration = 0;
        BigDecimal price = null;

        List <String> possiblePackageNames = new ArrayList<>();
        possiblePackageNames.add("Hiking Adventure");
        possiblePackageNames.add("Explore Castles and History");
        possiblePackageNames.add("Wine Tasting Tour");
        possiblePackageNames.add("Culinary Delights Experience");
        possiblePackageNames.add("Beach Getaway");
        possiblePackageNames.add("City Sightseeing Tour");
        possiblePackageNames.add("Wildlife and Nature Exploration");
        possiblePackageNames.add("Romantic Getaway");
        possiblePackageNames.add("Adventure Expedition");
        possiblePackageNames.add("Art and Culture Tour");
        possiblePackageNames.add("Luxury Spa Retreat");
        possiblePackageNames.add("Historical Landmarks Exploration");
        possiblePackageNames.add("Tropical Paradise Escape");
        possiblePackageNames.add("Scenic River Cruise");
        possiblePackageNames.add("Outdoor Sporting Experience");
        possiblePackageNames.add("Photography Safari");
        possiblePackageNames.add("Wellness and Yoga Retreat");


        for (Destination destination : destinations) {
            for (int i = 0; i < desiredNumOfPackages; i++) {
                packageId = UUID.randomUUID();
                name = possiblePackageNames.get(faker.random().nextInt(possiblePackageNames.size()));
                destinationId = destination.getDestinationId();
                duration = faker.number().numberBetween(3,7);
                price = BigDecimal.valueOf(faker.number().randomDouble(2, 100, 5000));

                pkg = new TravelPackage(packageId, name, destinationId, duration, price);
                packages.add(pkg);
            }
        }
        
        return packages;
    }

    public List<Client> generateClients(int desiredNumOfClients) {
        List <Client> clients = new ArrayList<>();
        Client client = null;
        UUID clientId = null;
        String name = null;
        String email = null;
        String telephone = null;

        for (int i = 0; i < desiredNumOfClients; i++) {
            clientId = UUID.randomUUID();
            name = faker.name().fullName();
            email = faker.internet().emailAddress();
            telephone = faker.phoneNumber().cellPhone();
            client = new Client(clientId, name, email, telephone);
            clients.add(client);
        }

        return clients;
    }
    public List<Reservation> generateReservations(List<Client> clients, List<TravelPackage> packages, int desiredNumOfReservations) {
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation = null;
        int packagesSize = packages.size();
        int clientsSize = clients.size();
        Client client = null;
        TravelPackage pkg = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        boolean payed = false;

        if (packagesSize == 0 || clientsSize == 0) {
            return reservations;
        }

        for (int i = 0; i < desiredNumOfReservations; i++) {
            client = clients.get(i % clientsSize); // Select a client in a cyclical manner
            pkg = packages.get(i % packagesSize); // Select a package in a cyclical manner
            startDate = LocalDate.now(); // Set a start date (you can customize this as needed)
            endDate = startDate.plusDays(faker.number().numberBetween(3, 14)); // Set an end date (adjust as needed)
            payed = faker.random().nextBoolean(); // Generate a random payment status

            reservation = new Reservation(UUID.randomUUID(), pkg.getPackageId(), client.getClientId(), startDate, endDate, payed);
            reservations.add(reservation);
        }

        return reservations;
    }

    /*public static void main(String[] args) {

        DataGenerator dataGenerator = new DataGenerator();
        List<Destination> destinationList = dataGenerator.generateDestinations(10);
        List<TravelPackage> packages = dataGenerator.generatePackages(destinationList, 2);
        List<Client> clients = dataGenerator.generateClients(20);
        List<Reservation> reservations = dataGenerator.generateReservations(clients, packages, 20);

        for (Destination destination: destinationList) {
            System.out.println(destination.toString());
        }

        for (TravelPackage pkg: packages) {
            System.out.println(pkg.toString());
        }

        for (Client client: clients) {
            System.out.println(client.toString());
        }

        for (Reservation reservation: reservations) {
            System.out.println(reservation.toString());
        }

        Database database = new Database();

        database.connect("127.0.0.1");

        DestinationDao destinationDao = new CassandraDestinationDao(database.getSession());
        for (Destination destination : destinationList) {
            destinationDao.insert(destination);
        }

        PackageDao packageDao = new CassandraPackageDao(database.getSession());
        for (TravelPackage pkg: packages) {
            packageDao.insert(pkg);
        }

        ClientDao clientDao = new CassandraClientDao(database.getSession());
        for (Client client: clients) {
            clientDao.insert(client);
        }

        ReservationDao reservationDao = new CassandraReservationDao(database.getSession());
        for (Reservation reservation: reservations) {
            reservationDao.insert(reservation);
        }

        database.close();

    }*/

}
