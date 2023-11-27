package es.unex.cum.sinf.practica1.daos.mongoDb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import es.unex.cum.sinf.practica1.daos.DestinationDao;
import es.unex.cum.sinf.practica1.entities.Destination;
import org.bson.Document;

import java.util.*;

public class MongoDbDestinationDao implements DestinationDao {

    private final MongoCollection<Document> collection;

    public MongoDbDestinationDao(MongoDatabase database) {
        this.collection = database.getCollection("destinations");
    }

    @Override
    public Destination get(UUID destinationId) {
        Document query = new Document("_id", destinationId.toString());
        Document result = collection.find(query).first();

        assert result != null;
        return documentToDestination(result);
    }

    @Override
    public Set<Destination> getAll() {
        Set<Destination> destinations = new HashSet<>();

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                destinations.add(documentToDestination(cursor.next()));
            }
        }

        return destinations;
    }

    @Override
    public void insert(Destination destination) {
        Document document = destinationToDocument(destination);
        collection.insertOne(document);
    }

    @Override
    public void update(Destination destination) {
        Document filter = new Document("_id", destination.getDestinationId().toString());
        Document update = new Document("$set", new Document("country", destination.getCountry())
                .append("description", destination.getDescription())
                .append("name", destination.getName())
                .append("weather", destination.getWeather()));
        collection.updateOne(filter, update);
    }

    @Override
    public void delete(UUID destinationId) {
        Document query = new Document("_id", destinationId.toString());
        collection.deleteOne(query);
    }

    @Override
    public Set<Destination> getDestinationsByWeather(String weather) {
        Set<Destination> destinations = new HashSet<>();

        Document query = new Document("weather", weather);
        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
            while (cursor.hasNext()) {
                destinations.add(documentToDestination(cursor.next()));
            }
        }

        return destinations;
    }

    @Override
    public Set<Destination> getDestinationsByCountry(String country) {
        Set<Destination> destinations = new HashSet<>();

        Document query = new Document("country", country);
        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
            while (cursor.hasNext()) {
                destinations.add(documentToDestination(cursor.next()));
            }
        }

        return destinations;
    }

    @Override
    public Set<Destination> getDestinationsByName(String name) {
        Set<Destination> destinations = new HashSet<>();

        Document query = new Document("name", name);
        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
            while (cursor.hasNext()) {
                destinations.add(documentToDestination(cursor.next()));
            }
        }

        return destinations;
    }


    private Destination documentToDestination(Document document) {
        UUID destinationId = UUID.fromString(document.getString("_id"));
        String name = document.getString("name");
        String country = document.getString("country");
        String description = document.getString("description");
        String weather = document.getString("weather");

        return new Destination(destinationId, name, country, description, weather);
    }

    private Document destinationToDocument(Destination destination) {
        String destinationId = destination.getDestinationId().toString();
        String name = destination.getName();
        String country = destination.getCountry();
        String description = destination.getDescription();
        String weather = destination.getWeather();

        return new Document("_id", destinationId).append("name", name).append("country", country).append("description", description).append("weather", weather);
    }
}
