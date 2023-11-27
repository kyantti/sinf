package es.unex.cum.sinf.practica1;

import com.datastax.driver.core.Session;
import com.mongodb.client.MongoDatabase;
import es.unex.cum.sinf.practica1.databaseConnection.Connection;
import es.unex.cum.sinf.practica1.databaseConnection.mongoDb.MongoDbConnection;
import es.unex.cum.sinf.practica1.daos.DaoManager;
import es.unex.cum.sinf.practica1.daos.cassandra.CassandraDaoManager;
import es.unex.cum.sinf.practica1.daos.mongoDb.MongoDbDaoManager;
import es.unex.cum.sinf.practica1.databaseConnection.cassandra.CassandraConnection;
import es.unex.cum.sinf.practica1.entities.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);

    public static void performBasicQueries(DaoManager daoManager) {
        boolean returnToMainMenu = false;
        int basicChoice;
        String input;

        while (!returnToMainMenu) {
            System.out.println("Consultas Básicas:");
            System.out.println("1. Obtener todos los destinos disponibles en la agencia de viajes.");
            System.out.println("2. Obtener los detalles de un paquete turístico específico a través de su ID.");
            System.out.println("3. Obtener todas las reservas realizadas por un cliente específico.");
            System.out.println("4. Obtener todos los paquetes turísticos disponibles para un destino en particular.");
            System.out.println("5. Obtener todos los clientes que han realizado reservas en un rango de fechas específico.");
            System.out.println("6. Volver al menú principal");
            System.out.println("Seleccione una opción: ");

            basicChoice = scanner.nextInt();

            switch (basicChoice) {
                case 1:
                    getAllDestinations(daoManager);
                    break;
                case 2:
                    System.out.println("Introduzca el ID del paquete:");
                    input = scanner.next();
                    try {
                        UUID packageId = UUID.fromString(input);
                        TravelPackage aPackage = getPackageById(daoManager, packageId);
                        if (aPackage != null) {
                            System.out.println(aPackage);
                        } else {
                            System.out.println("No existe ningún paquete con ese ID");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Formato UUID no válido. Asegúrese de ingresar un ID válido.");
                    }
                    break;
                case 3:
                    System.out.println("Introduzca el ID del cliente:");
                    input = scanner.next();
                    try {
                        UUID clientId = UUID.fromString(input);
                        Set<Reservation> reservations = getReservationsByClientId(daoManager, clientId);
                        if (!reservations.isEmpty()) {
                            for (Reservation reservation : reservations) {
                                System.out.println(reservation.toString());
                            }
                        } else {
                            System.out.println("No existe ningún cliente con ese ID");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Formato UUID no válido. Asegúrese de ingresar un ID válido.");
                    }
                    break;
                case 4:
                    System.out.println("Introduzca el ID del destino:");
                    input = scanner.next();
                    try {
                        UUID destinationId = UUID.fromString(input);
                        Destination destination = daoManager.getDestinationDao().get(destinationId);
                        if (destination != null) {
                            Set<TravelPackage> packages = getPackagesByDestinationId(daoManager, destinationId);
                            for (TravelPackage travelPackage : packages) {
                                System.out.println(travelPackage.toString());
                            }
                        } else {
                            System.out.println("No existe ningún paquete con ese ID de destino");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Formato UUID no válido. Asegúrese de ingresar un ID válido.");
                    }
                    break;
                case 5:
                    System.out.println("Ingrese una fecha de inicio (yyyy-MM-dd):");
                    input = scanner.next();
                    LocalDate startDate = null;
                    try {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        startDate = LocalDate.parse(input, dateFormatter);
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato de fecha incorrecto. Intente de nuevo.");
                    }
                    System.out.println("Ingrese una fecha de fin (yyyy-MM-dd):");
                    input = scanner.next();
                    LocalDate endDate = null;
                    try {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        endDate = LocalDate.parse(input, dateFormatter);
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato de fecha incorrecto. Intente de nuevo.");
                    }
                    if (startDate != null && endDate != null) {
                        if (startDate.isAfter(endDate)) {
                            System.out.println("La fecha de inicio no puede ser posterior a la fecha de fin. Intente de nuevo.");
                        } else {
                            getClientsByReservationDateRange(daoManager, startDate, endDate);
                        }
                    }

                    break;
                case 6:
                    returnToMainMenu = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
            }
        }
    }

    public static Set<Client> getClientsByReservationDateRange(DaoManager daoManager, LocalDate startDate, LocalDate endDate){
        Set<Reservation> reservations = daoManager.getReservationDao().getReservationsByDateRange(startDate, endDate);
        UUID clientId;
        Client client;
        Set<Client> clients = new HashSet<>();

        for (Reservation reservation : reservations) {
            clientId = reservation.getClientId();
            client = daoManager.getClientDao().get(clientId);
            clients.add(client);
        }

        return clients;
    }

    public static Set<TravelPackage> getPackagesByDestinationId(DaoManager daoManager, UUID destinationId) {
        return daoManager.getPackageDao().getPackagesByDestinationId(destinationId);
    }

    public static Set<Reservation> getReservationsByClientId(DaoManager daoManager, UUID clientId) {
        return daoManager.getReservationDao().getReservationsByClientId(clientId);
    }

    public static TravelPackage getPackageById(DaoManager daoManager, UUID packageId) {
        return daoManager.getPackageDao().get(packageId);
    }

    public static Set<Destination> getAllDestinations(DaoManager daoManager) {
        return daoManager.getDestinationDao().getAll();
    }

    public static void performAdvancedQueries(DaoManager daoManager) {
        boolean returnToMainMenu = false;
        int advancedChoice;
        String input;
        while (!returnToMainMenu) {
            System.out.println("Consultas Avanzadas con Indexación:");
            System.out.println("1. Generar un índice para acelerar la búsqueda de paquetes por nombre.");
            System.out.println("2. Crear una tabla de resumen para almacenar el número total de reservas realizadas por cada paquete.");
            System.out.println("3. Obtener todos los clientes que han realizado reservas en destinos con un clima específico.");
            System.out.println("4. Generar un índice compuesto para optimizar la búsqueda de reservas de un cliente en un rango de fechas determinado.");
            System.out.println("5. Crear una tabla de índice para acelerar la búsqueda de destinos por país.");
            System.out.println("6. Volver al menú principal");
            System.out.println("Seleccione una opción: ");

            advancedChoice = scanner.nextInt();

            switch (advancedChoice) {
                case 1:
                    System.out.println("Introduzca el nombre del paquete:");
                    scanner.nextLine();
                    input = scanner.nextLine();
                    if (!input.isEmpty()) {
                        Set<TravelPackage> travelPackages = getPackagesByName(daoManager, input);

                        if (!travelPackages.isEmpty()) {
                            for (TravelPackage travelPackage : travelPackages) {
                                System.out.println(travelPackage.toString());
                            }
                        } else {
                            System.out.println("No se encontraron paquetes con ese nombre.");
                        }
                    } else {
                        System.out.println("El nombre del paquete no puede estar vacío.");
                    }
                    break;
                case 2:
                    Set <PackageReservationSummary> summary = getPackageReservationsSummary(daoManager);
                    for (PackageReservationSummary packageReservationSummary : summary) {
                        System.out.println(packageReservationSummary.toString());
                    }
                    break;
                case 3:
                    System.out.println("Introduzca el tipo clima:");
                    scanner.nextLine();
                    input= scanner.nextLine();
                    if (!input.isEmpty()) {
                        Set<Client> clients = getClientsWithReservationsInSpecificClimate(daoManager, input);
                        if (!clients.isEmpty()) {
                            for (Client client : clients) {
                                System.out.println(client.toString());
                            }
                        } else {
                            System.out.println("No se encontraron clientes con reservas en destinos con ese clima.");
                        }
                    } else {
                        System.out.println("El clima no puede estar vacío.");
                    }
                    break;
                case 4:
                    System.out.println("Introduzca el ID del cliente:");
                    scanner.nextLine();
                    input = scanner.nextLine();
                    try {
                        UUID clientId = UUID.fromString(input);
                        System.out.println("Ingrese una fecha de inicio (yyyy-MM-dd):");
                        input = scanner.next();
                        LocalDate startDate = null;
                        try {
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            startDate = LocalDate.parse(input, dateFormatter);
                        } catch (DateTimeParseException e) {
                            System.out.println("Formato de fecha incorrecto. Intente de nuevo.");
                        }
                        System.out.println("Ingrese una fecha de fin (yyyy-MM-dd):");
                        input = scanner.next();
                        LocalDate endDate = null;
                        try {
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            endDate = LocalDate.parse(input, dateFormatter);
                        } catch (DateTimeParseException e) {
                            System.out.println("Formato de fecha incorrecto. Intente de nuevo.");
                        }
                        if (startDate != null && endDate != null) {
                            if (startDate.isAfter(endDate)) {
                                System.out.println("La fecha de inicio no puede ser posterior a la fecha de fin. Intente de nuevo.");
                            } else {
                                Set<Reservation> reservations = getReservationsByClientIdAndDateRange(daoManager, clientId, startDate, endDate);
                                if (!reservations.isEmpty()) {
                                    for (Reservation reservation : reservations) {
                                        System.out.println(reservation.toString());
                                    }
                                } else {
                                    System.out.println("No se encontraron reservas para ese cliente en ese rango de fechas.");
                                }
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Formato UUID no válido. Asegúrese de ingresar un ID válido.");
                    }
                    break;
                case 5:
                    System.out.println("Introduzca el nombre del país:");
                    scanner.nextLine();
                    input = scanner.nextLine();
                    if (!input.isEmpty()) {
                        Set<Destination> destinations = getDestinationsByCountry(daoManager, input);
                        if (!destinations.isEmpty()) {
                            for (Destination destination : destinations) {
                                System.out.println(destination.toString());
                            }
                        } else {
                            System.out.println("No se encontraron destinos con ese país.");
                        }
                    } else {
                        System.out.println("El país no puede estar vacío.");
                    }
                    break;
                case 6:
                    returnToMainMenu = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
            }
        }
    }

    public static Set<Destination> getDestinationsByCountry(DaoManager daoManager, String country) {
        return daoManager.getDestinationDao().getDestinationsByCountry(country);
    }

    public static Set<Client> getClientsWithReservationsInSpecificClimate(DaoManager daoManager, String weather) {
        Set<Destination> destinationsWithSpecificWeather = daoManager.getDestinationDao().getDestinationsByWeather(weather);
        Set<TravelPackage> packagesInThatDestination;
        Set<Reservation> reservationsOfThosePackages;
        Set<Client> clients = new HashSet<>();

        for (Destination destination : destinationsWithSpecificWeather) {
            UUID destinationId = destination.getDestinationId();
            packagesInThatDestination = daoManager.getPackageDao().getPackagesByDestinationId(destinationId);

            for (TravelPackage travelPackage : packagesInThatDestination) {
                UUID packageId = travelPackage.getPackageId();
                reservationsOfThosePackages = daoManager.getReservationDao().getReservationsByPackageId(packageId);

                for (Reservation reservation : reservationsOfThosePackages) {
                    UUID clientId = reservation.getClientId();
                    Client client = daoManager.getClientDao().get(clientId);
                    clients.add(client);
                }
            }
        }

       return clients;

    }

    public static Set<Reservation> getReservationsByClientIdAndDateRange(DaoManager daoManager, UUID clientId ,LocalDate startDate, LocalDate endDate){
        return daoManager.getReservationDao().getReservationsByClientIdAndDateRange(clientId, startDate, endDate);
    }

    private static Set<PackageReservationSummary> getPackageReservationsSummary(DaoManager daoManager) {
        return daoManager.getPackageSummaryDao().getAll();
    }

    private static Set<TravelPackage> getPackagesByName(DaoManager daoManager, String name) {
        return daoManager.getPackageDao().getPackagesByName(name);
    }

    public static void performComplexQueires(DaoManager daoManager) {
        boolean returnToMainMenu = false;
        int choice;
        String input;
        while (!returnToMainMenu) {
            System.out.println("Consultas Complejas con Indexación:");
            System.out.println("1. Obtener todos los destinos populares (los más reservados) en orden descendente según el número de reservas.");
            System.out.println("2. Generar un índice para acelerar la búsqueda de clientes por correo electrónico.");
            System.out.println("3. Crear una tabla de índice para almacenar información sobre la disponibilidad de paquetes por destino.");
            System.out.println("4. Obtener todos los paquetes turísticos disponibles para un destino específico y con una duración determinada.");
            System.out.println("5. Generar un índice compuesto para optimizar la búsqueda de reservas por cliente, destino y estado de pago.");
            System.out.println("6. Volver al menú principal");

            System.out.println("Seleccione una opción: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    List<Destination> destinations = getMostPopularDestinations(daoManager);
                    System.out.println("Los 10 destinos mas populares:");
                    for (Destination destination : destinations) {
                        System.out.println(destination.toString());
                    }
                    break;
                case 2:
                    System.out.println("Introduzca el E-Mail del cliente:");
                    scanner.nextLine();
                    input = scanner.nextLine();
                    if (!input.isEmpty()) {
                        Client client = getClientByEmail(daoManager, input);
                        if (client != null) {
                            System.out.println(client);
                        } else {
                            System.out.println("No se ha encontrado ningun cliente con ese E-Mail.");
                        }
                    } else {
                        System.out.println("El E-Mail no puede estar vacío.");
                    }
                    break;
                case 3:
                    System.out.println("Introduzca el ID del destino:");
                    scanner.nextLine();
                    input = scanner.nextLine();
                    try {
                        UUID destinationId = UUID.fromString(input);
                        Destination destination = daoManager.getDestinationDao().get(destinationId);
                        if (destination != null) {
                            Set<TravelPackage> packages = getPackagesByDestinationId(daoManager, destinationId);
                            for (TravelPackage travelPackage : packages) {
                                System.out.println(travelPackage.toString());
                            }
                        } else {
                            System.out.println("No existe ningún paquete con ese ID de destino");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Formato UUID no válido. Asegúrese de ingresar un ID válido.");
                    }
                    break;
                case 4:
                    System.out.println("Introduzca el ID del destino:");
                    scanner.nextLine();
                    input = scanner.nextLine();
                    try {
                        UUID destinationId = UUID.fromString(input);
                        Destination destination = daoManager.getDestinationDao().get(destinationId);
                        if (destination != null) {
                            System.out.println("Introduzca la duración (dias):");
                            scanner.nextLine();
                            input = scanner.nextLine();
                            int duration = Integer.parseInt(input);
                            Set<TravelPackage> packages = getPackagesByDestinationIdAndDuration(daoManager, destinationId, duration);
                            for (TravelPackage travelPackage : packages) {
                                System.out.println(travelPackage.toString());
                            }
                        } else {
                            System.out.println("No existe ningún paquete con ese ID de destino");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Formato UUID no válido. Asegúrese de ingresar un ID válido.");
                    }

                    break;
                case 5:
                    System.out.println("Introduzca el ID del cliente:");
                    scanner.nextLine();
                    input = scanner.nextLine();
                    try {
                        UUID clientId = UUID.fromString(input);
                        System.out.println("Introduzca el ID del destino:");
                        input = scanner.next();
                        UUID destinationId = UUID.fromString(input);
                        System.out.println("Introduzca el estado de pago:");
                        scanner.nextLine();
                        input = scanner.nextLine();
                        boolean paid = Boolean.parseBoolean(input);
                        Set<Reservation> reservations = getReservationsByClientIdAndDestinationIdAndPaymentStatus(daoManager, clientId, destinationId, paid);
                        if (!reservations.isEmpty()) {
                            for (Reservation reservation : reservations) {
                                System.out.println(reservation.toString());
                            }
                        } else {
                            System.out.println("No se encontraron reservas para ese cliente en ese destino con ese estado de pago.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Formato UUID no válido. Asegúrese de ingresar un ID válido.");
                    }
                    break;
                case 6:
                    returnToMainMenu = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
            }
        }
    }

    public static Set<Reservation> getReservationsByClientIdAndDestinationIdAndPaymentStatus(DaoManager daoManager, UUID clientId, UUID destinationId, boolean paid) {
        Set <TravelPackage> packages = getPackagesByDestinationId(daoManager, destinationId);
        UUID packageId = null;
        Set<Reservation> reservations = new HashSet<>();
        for (TravelPackage travelPackage : packages) {
            packageId = travelPackage.getPackageId();
            reservations.addAll(daoManager.getReservationDao().getReservationsByClientIdAndPackageIdAndPaymentStatus(clientId, packageId, paid));
        }
        return reservations;
    }

    public static Set<TravelPackage> getPackagesByDestinationName(DaoManager daoManager, String name) {
        Set <Destination> destination = daoManager.getDestinationDao().getDestinationsByName(name);
        Set<TravelPackage> travelPackages = new HashSet<>();
        for (Destination destination1 : destination) {
            UUID destinationId = destination1.getDestinationId();
            travelPackages.add(daoManager.getPackageDao().getPackagesByDestinationId(destinationId).iterator().next());
        }
        return travelPackages;
    }

    public static List<Destination> getMostPopularDestinations(DaoManager daoManager) {
        Set<PackageReservationSummary> summary = daoManager.getPackageSummaryDao().getAll();
        List<PackageReservationSummary> summaryList = new ArrayList<>(summary);

        TravelPackage aPackage;
        UUID destinationId;
        Destination destination;
        List<Destination> destinations = new ArrayList<>();

        Collections.sort(summaryList);

        for (PackageReservationSummary summary1: summaryList) {
            aPackage = daoManager.getPackageDao().get(summary1.getPackageId());
            destinationId = aPackage.getDestinationId();
            destination = daoManager.getDestinationDao().get(destinationId);
            destinations.add(destination);
            //System.out.println(destination.getName() + " Reservas: " + summary1.getTotalReservations());
        }

        //Only return the 10 first
        destinations = destinations.subList(0, 10);

        return destinations;

    }

    public static Set<TravelPackage> getPackagesByDestinationIdAndDuration(DaoManager daoManager, UUID destinationId, int duration) {
       return daoManager.getPackageDao().getPackagesByDestinationIdAndDuration(destinationId, duration);
    }

    public static Client getClientByEmail(DaoManager daoManager, String email) {
        return daoManager.getClientDao().getClientByEmail(email);
    }

    public static void main(String[] args) {
        boolean exitApp = false;
        Connection connection = null;
        DaoManager daoManager = null;

        System.out.println("Seleccione la base de datos a utilizar:");
        System.out.println("1. Cassandra");
        System.out.println("2. MongoDB");
        System.out.println("3. Salir");

        int dataBaseChoice = scanner.nextInt();

        switch (dataBaseChoice) {
            case 1:
                connection = new CassandraConnection();
                connection.open("127.0.0.1");
                Session session = ((CassandraConnection) connection).getSession();
                String keyspace = "pablo_setrakian_bearzotti";
                daoManager = new CassandraDaoManager(session, keyspace);
                break;
            case 2:
                connection = new MongoDbConnection();
                connection.open("localhost");
                MongoDbConnection mongoConnection = (MongoDbConnection) connection;
                mongoConnection.setDatabase("pablo_setrakian_bearzotti");
                MongoDatabase mongoDatabase = mongoConnection.getDatabase();
                daoManager = new MongoDbDaoManager(mongoDatabase);
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Categoría no válida. Por favor, seleccione una categoría válida.");
                break;
        }

        while (!exitApp) {
            System.out.println("Travel Agency App Menu:");
            System.out.println("1. Consultas Básicas");
            System.out.println("2. Consultas Avanzadas con Indexación");
            System.out.println("3. Consultas Complejas con Indexación");
            System.out.println("4. Salir");
            System.out.println("Seleccione una categoría: ");

            int categoryChoice = scanner.nextInt();

            switch (categoryChoice) {
                case 1:
                    performBasicQueries(daoManager);
                    break;
                case 2:
                    performAdvancedQueries(daoManager);
                    break;
                case 3:
                    performComplexQueires(daoManager);
                    break;
                case 4:
                    System.out.println("Saliendo de la aplicación. ¡Hasta luego!");
                    exitApp = true;
                    break;
                default:
                    System.out.println("Categoría no válida. Por favor, seleccione una categoría válida.");
                    break;
            }
        }

        scanner.close();
        assert connection != null;
        connection.close();

    }
}
