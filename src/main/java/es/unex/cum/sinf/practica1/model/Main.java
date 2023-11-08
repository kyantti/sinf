package es.unex.cum.sinf.practica1.model;

import java.util.List;
import java.util.UUID;

import com.github.javafaker.Address;
import com.github.javafaker.Country;
import es.unex.cum.sinf.practica1.model.daos.cassandra.CassandraDestinationDao;
import es.unex.cum.sinf.practica1.model.database.Database;
import es.unex.cum.sinf.practica1.model.datagenerator.DataGenerator;
import es.unex.cum.sinf.practica1.model.entities.Client;
import es.unex.cum.sinf.practica1.model.entities.Destination;
import com.github.javafaker.Faker;
import es.unex.cum.sinf.practica1.model.entities.Package;
import es.unex.cum.sinf.practica1.model.entities.Reservation;

public class Main {

    public static void main(String[] args) {

        //Database database = new Database();

        //Ejemplo 1
        //database.connect("127.0.0.1");

        //CassandraDestinationDao cassandraDestinationDao = new CassandraDestinationDao(database.getSession());
        /*for (Destination destination : cassandraDestinationDao.getAll()) {
            System.out.println(destination.toString());
        }*/

        //Destination destination = cassandraDestinationDao.get(UUID.fromString("e6d67bcb-2327-43f2-a2a3-4368a995fdca"));
        //System.out.println(destination.toString());

        //database.close();

        DataGenerator dataGenerator = new DataGenerator();
        List<Destination> destinationList = dataGenerator.generateDestinations(50);
        List<Package> packages = dataGenerator.generatePackages(destinationList, 100);
        List<Client> clients = dataGenerator.generateClients(200);
        List<Reservation> reservations = dataGenerator.generateReservations(clients, packages, 500);

        for (Destination destination: destinationList) {
            System.out.println(destination.toString());
        }

        for (Package pkg: packages) {
            System.out.println(pkg.toString());
        }

        for (Client client: clients) {
            System.out.println(client.toString());
        }

        for (Reservation reservation: reservations) {
            System.out.println(reservation.toString());
        }





    }
}
