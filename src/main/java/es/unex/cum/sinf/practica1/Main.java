package es.unex.cum.sinf.practica1;

import java.util.UUID;

import es.unex.cum.sinf.practica1.model.daos.cassandra.CassandraDestinationDao;
import es.unex.cum.sinf.practica1.model.database.Database;
import es.unex.cum.sinf.practica1.model.entities.Destination;

public class Main {

    public static void main(String[] args) {
        Database database = new Database();
    
        //Ejemplo 1
        database.connect("127.0.0.1");
        
        CassandraDestinationDao cassandraDestinationDao = new CassandraDestinationDao(database.getSession());
        /*for (Destination destination : cassandraDestinationDao.getAll()) {
            System.out.println(destination.toString());
        }*/

        Destination destination = cassandraDestinationDao.get(UUID.fromString("e6d67bcb-2327-43f2-a2a3-4368a995fdca"));
        System.out.println(destination.toString());


        database.close();
    }
}
