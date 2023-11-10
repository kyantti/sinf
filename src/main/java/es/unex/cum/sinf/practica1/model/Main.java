package es.unex.cum.sinf.practica1.model;

import com.datastax.driver.core.Session;
import es.unex.cum.sinf.practica1.model.daos.DaoManager;
import es.unex.cum.sinf.practica1.model.daos.cassandra.CassandraDaoManager;
import es.unex.cum.sinf.practica1.model.databaseConnection.Connection;
import es.unex.cum.sinf.practica1.model.databaseConnection.cassandra.CassandraConnection;
import es.unex.cum.sinf.practica1.model.entities.Client;
import es.unex.cum.sinf.practica1.model.entities.Destination;
import es.unex.cum.sinf.practica1.model.entities.Reservation;
import es.unex.cum.sinf.practica1.model.entities.TravelPackage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);

    public static void performBasicQueries(DaoManager daoManager) {
        boolean returnToMainMenu = false;
        int basicChoice = 0;

        while (!returnToMainMenu) {
            System.out.println("Consultas Básicas:");
            System.out.println("1. Obtener todos los destinos disponibles en la agencia de viajes.");
            System.out.println("2. Obtener los detalles de un paquete turístico específico a través de su ID.");
            System.out.println("3. Obtener todas las reservas realizadas por un cliente específico.");
            System.out.println("4. Obtener todos los paquetes turísticos disponibles para un destino en particular.");
            System.out.println(
                    "5. Obtener todos los clientes que han realizado reservas en un rango de fechas específico.");
            System.out.println("6. Volver al menú principal");
            System.out.println("Seleccione una opción: ");

            basicChoice = scanner.nextInt();

            switch (basicChoice) {
                case 1:
                    getAllDestinations(daoManager);
                    break;
                case 2:
                    getPackageById(daoManager);
                    break;
                case 3:
                    getReservationsByClientId(daoManager);
                    break;
                case 4:
                    getPackagesByDestinationId(daoManager);
                    break;
                case 5:
                    getClientsByReservationDateRange(daoManager);
                    break;
                case 6:
                    returnToMainMenu = !returnToMainMenu;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
            }
        }
    }

    private static void getClientsByReservationDateRange(DaoManager daoManager) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = null;
        LocalDate endDate = null;
        String input = null;

        try {
            System.out.println("Ingrese una fecha de inicio (yyyy-MM-dd):");
            input = scanner.next();
            startDate = LocalDate.parse(input, dateFormatter);

            System.out.println("Ingrese una fecha de fin (yyyy-MM-dd):");
            input = scanner.next();
            endDate = LocalDate.parse(input, dateFormatter);

            if (startDate.isAfter(endDate)) {
                System.out.println("La fecha de inicio no puede ser posterior a la fecha de fin. Intente de nuevo.");
            } else {
                List<Reservation> reservations = daoManager.getReservationDao().getReservationsByDateRange(startDate,
                        endDate);
                List<Client> clients = new ArrayList<>();
                Client client = null;
                UUID clientId = null;

                for (Reservation reservation : reservations) {
                    clientId = reservation.getClientId();
                    client = daoManager.getClientDao().get(clientId);
                    System.out.println(client.toString());
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha incorrecto. Intente de nuevo.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void getPackagesByDestinationId(DaoManager daoManager) {
        System.out.println("Introduzca el ID del destino:");
        String input = scanner.next();
        try {
            UUID uuid = UUID.fromString(input);
            Destination destination = daoManager.getDestinationDao().get(uuid);
            if (destination != null) {
                List<TravelPackage> packages = daoManager.getPackageDao().getPackagesByDestinationId(uuid);
                for (TravelPackage travelPackage : packages) {
                    System.out.println(travelPackage.toString());
                }
            } else {
                System.out.println("No existe ningún paquete con ese ID de destino");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Formato UUID no válido. Asegúrese de ingresar un ID válido.");
        }
    }

    private static void getReservationsByClientId(DaoManager daoManager) {
        System.out.println("Introduzca el ID del cliente:");
        String input = scanner.next();
        try {
            UUID uuid = UUID.fromString(input);
            Client client = daoManager.getClientDao().get(uuid);

            if (client != null) {
                for (Reservation reservation : daoManager.getReservationDao().getReservationsByClientId(uuid)) {
                    System.out.println(reservation.toString());
                }
            } else {
                System.out.println("No existe ningún cliente con ese ID");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Formato UUID no válido. Asegúrese de ingresar un ID válido.");
        }
    }

    private static void getPackageById(DaoManager daoManager) {
        System.out.println("Introduzca el ID del paquete:");
        String input = scanner.next();
        try {
            UUID uuid = UUID.fromString(input);
            TravelPackage travelPackage = daoManager.getPackageDao().get(uuid);

            if (travelPackage != null) {
                System.out.println(travelPackage.toString());
            } else {
                System.out.println("No existe ningún paquete con ese ID");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Formato UUID no válido. Asegúrese de ingresar un ID válido.");
        }
    }

    private static void getAllDestinations(DaoManager daoManager) {
        List<Destination> destinationList = daoManager.getDestinationDao().getAll();
        for (Destination destination : destinationList) {
            System.out.println(destination.toString());
        }
    }

    public static void performAdvancedQueries(DaoManager daoManager) {
        boolean returnToMainMenu = false;
        int advancedChoice = 0;
        while (!returnToMainMenu) {
            System.out.println("Consultas Avanzadas con Indexación:");
            System.out.println("1. Generar un índice para acelerar la búsqueda de paquetes por nombre.");
            System.out.println(
                    "2. Crear una tabla de resumen para almacenar el número total de reservas realizadas por cada paquete.");
            System.out.println(
                    "3. Obtener todos los clientes que han realizado reservas en destinos con un clima específico.");
            System.out.println(
                    "4. Generar un índice compuesto para optimizar la búsqueda de reservas de un cliente en un rango de fechas determinado.");
            System.out.println("5. Crear una tabla de índice para acelerar la búsqueda de destinos por país.");
            System.out.println("6. Volver al menú principal");
            System.out.println("Seleccione una opción: ");

            advancedChoice = scanner.nextInt();

            switch (advancedChoice) {
                case 1:
                    getPackagesByName(daoManager);
                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:
                    returnToMainMenu = !returnToMainMenu;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
            }
        }
    }

    private static void getPackagesByName(DaoManager daoManager) {
        System.out.println("Introduzca el nombre del paquete:");
        scanner.nextLine();
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            List<TravelPackage> travelPackages = daoManager.getPackageDao().getPackagesByName(name);

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
    }

    public static void performComplexQueires(DaoManager daoManager) {
        boolean returnToMainMenu = false;
        int choice = 0;
        while (!returnToMainMenu) {
            System.out.println("Consultas Complejas con Indexación:");
            System.out.println(
                    "1. Obtener todos los destinos populares (los más reservados) en orden descendente según el número de reservas.");
            System.out.println("2. Generar un índice para acelerar la búsqueda de clientes por correo electrónico.");
            System.out.println(
                    "3. Crear una tabla de índice para almacenar información sobre la disponibilidad de paquetes por destino y fecha.");
            System.out.println(
                    "4. Obtener todos los paquetes turísticos disponibles para un destino específico y con una duración determinada.");
            System.out.println(
                    "5. Generar un índice compuesto para optimizar la búsqueda de reservas por cliente, destino y estado de pago.");
            System.out.println("6. Volver al menú principal");

            System.out.println("Seleccione una opción: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Obtaining popular destinations based on reservations...");
                    break;
                case 2:
                    System.out.println("Generating an index for client search by email...");
                    break;
                case 3:
                    System.out.println("Creating an index table for package availability...");
                    break;
                case 4:
                    System.out.println("Obtaining tourist packages for a specific destination and duration...");
                    break;
                case 5:
                    System.out.println("Generating a composite index for reservation search...");
                    break;
                case 6:
                    returnToMainMenu = !returnToMainMenu;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
            }
        }
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

        try {
            switch (dataBaseChoice) {
                case 1:
                    connection = new CassandraConnection();
                    connection.open("127.0.0.1");
                    Session session = ((CassandraConnection) connection).getSession();
                    daoManager = new CassandraDaoManager(session);
                    break;
                case 2:
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
                        exitApp = !exitApp;
                        break;
                    default:
                        System.out.println("Categoría no válida. Por favor, seleccione una categoría válida.");
                        break;
                }
            }

        } catch (InputMismatchException e) {
            System.out.println("Entrada no válida. Asegúrese de ingresar un número.");
        } catch (Exception e) {
            System.out.println("Error durante la inicialización de la base de datos: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
