package es.unex.cum.sinf.practica1.databaseConnection;

public interface Connection {
    void open(String node);
    void close();
}
