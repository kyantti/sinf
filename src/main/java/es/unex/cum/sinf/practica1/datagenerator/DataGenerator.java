package es.unex.cum.sinf.practica1.datagenerator;



import com.github.javafaker.Faker;
import es.unex.cum.sinf.practica1.entities.Client;
import es.unex.cum.sinf.practica1.entities.Destination;
import es.unex.cum.sinf.practica1.entities.Reservation;
import es.unex.cum.sinf.practica1.entities.TravelPackage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class DataGenerator{
    private final Faker faker;
    public DataGenerator() {
        faker = new Faker();
    }
    public List<Destination> generateDestinations(int desiredNumOfDestinations) {
        List<Destination> destinations = new ArrayList<>();
        Destination destination;
        UUID destinationId;
        String countryName;
        String description;
        String destinationName;
        String weather;

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

    public List<TravelPackage> generatePackagesPerDestination(List<Destination> destinations, int desiredNumOfPackages) {
        List <TravelPackage> packages = new ArrayList<>();
        TravelPackage pkg;
        UUID packageId;
        String name;
        UUID destinationId;
        int duration;
        BigDecimal price;

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
        List<Client> clients = new ArrayList<>();
        Set<String> generatedEmails = new HashSet<>();
        Client client;
        UUID clientId;
        String name;
        String email;
        String telephone;

        for (int i = 0; i < desiredNumOfClients; i++) {
            clientId = UUID.randomUUID();
            name = faker.name().fullName();
            email = generateUniqueEmail(generatedEmails);
            telephone = faker.phoneNumber().cellPhone();
            client = new Client(clientId, name, email, telephone);
            clients.add(client);
        }

        return clients;
    }

    private String generateUniqueEmail(Set<String> generatedEmails) {
        String email = faker.internet().emailAddress();
        while (generatedEmails.contains(email)) {
            email = faker.internet().emailAddress();
        }
        generatedEmails.add(email);
        return email;
    }

    public List<Reservation> generateReservations(List<Client> clients, List<TravelPackage> packages, int desiredNumOfReservations) {
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation;
        int packagesSize = packages.size();
        int clientsSize = clients.size();
        Client client;
        TravelPackage pkg;
        LocalDate startDate;
        LocalDate endDate;
        boolean payed;

        if (packagesSize == 0 || clientsSize == 0) {
            return reservations;
        }

        for (int i = 0; i < desiredNumOfReservations; i++) {
            client = clients.get(i % clientsSize); // Select a client in a cyclical manner
            pkg = packages.get(faker.random().nextInt(packagesSize));
            extracted(reservations, client, pkg);
        }

        return reservations;
    }

    private void extracted(List<Reservation> reservations, Client client, TravelPackage pkg) {
        LocalDate startDate;
        LocalDate endDate;
        boolean payed;
        Reservation reservation;
        startDate = LocalDate.now(); // Set a start date (you can customize this as needed)
        endDate = startDate.plusDays(faker.number().numberBetween(3, 14)); // Set an end date (adjust as needed)
        payed = faker.random().nextBoolean(); // Generate a random payment status

        reservation = new Reservation(UUID.randomUUID(), pkg.getPackageId(), client.getClientId(), startDate, endDate, payed);
        reservations.add(reservation);
    }

    public List<Reservation> generateReservationsPerPackage(List<Client> clients, TravelPackage travelPackage, int desiredNumOfReservations){
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation;
        int clientsSize = clients.size();
        Client client;
        LocalDate startDate;
        LocalDate endDate;
        boolean payed;

        if (clientsSize == 0) {
            return reservations;
        }

        for (int i = 0; i < desiredNumOfReservations; i++) {
            client = clients.get(i % clientsSize); // Select a client in a cyclical manner
            extracted(reservations, client, travelPackage);
        }

        return reservations;
    }

}
