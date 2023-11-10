package es.unex.cum.sinf.practica1.model.databaseConnection;

public interface Connection {
    void open(String node);
    void close();
}
