package es.unex.cum.sinf.practica1.model.databaseConnection.mongoDb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import es.unex.cum.sinf.practica1.model.databaseConnection.Connection;

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

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public void open(String host) {
        client = (new MongoClient(host, 27017));
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
