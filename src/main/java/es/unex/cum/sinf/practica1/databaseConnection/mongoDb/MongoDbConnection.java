package es.unex.cum.sinf.practica1.databaseConnection.mongoDb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import es.unex.cum.sinf.practica1.databaseConnection.Connection;

public class MongoDbConnection implements Connection {
    private MongoClient client;
    private MongoDatabase database;

    public MongoClient getClient() {
        return client;
    }

    public void setClient(MongoClient client) {
        this.client = client;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = client.getDatabase(database);
    }

    @Override
    public void open(String host) {
        client = MongoClients.create("mongodb://" + host + ":27017");
    }
    public void connectToCollection(String database) {
        this.database = client.getDatabase(database);
    }
    public void createCollection(String collection) {
        database.getCollection(collection);
    }
    public void deleteCollection(String collection) {
        database.getCollection(collection).drop();
    }
    public MongoClient getMongoClient() {
        return client;
    }
    @Override
    public void close(){
        client.close();
    }
}
