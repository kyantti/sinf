package es.unex.cum.sinf.practica1.daos.mongoDb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import es.unex.cum.sinf.practica1.daos.PackageDao;
import es.unex.cum.sinf.practica1.entities.TravelPackage;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.*;

import org.bson.types.Decimal128;

public class MongoDbPackageDao implements PackageDao {

    private final MongoCollection<Document> collection;

    public MongoDbPackageDao(MongoDatabase database) {
        this.collection = database.getCollection("packages");
    }

    @Override
    public TravelPackage get(UUID packageId) {
        Document query = new Document("_id", packageId.toString());
        Document result = collection.find(query).first();

        if (result != null) {
            return documentToTravelPackage(result);
        } else {
            return null;
        }
    }

    @Override
    public Set<TravelPackage> getAll() {
        Set<TravelPackage> packages = new HashSet<>();

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                packages.add(documentToTravelPackage(cursor.next()));
            }
        }

        return packages;
    }

    @Override
    public void insert(TravelPackage aPackage) {
        Document document = travelPackageToDocument(aPackage);
        collection.insertOne(document);
    }

    @Override
    public void update(TravelPackage aPackage) {
        Document filter = new Document("_id", aPackage.getPackageId().toString());
        Document update = new Document("$set", aPackage.getDestinationId().toString())
                .append("duration", aPackage.getDuration())
                .append("name", aPackage.getName())
                .append("price", new Decimal128(aPackage.getPrice()));
        collection.updateOne(filter, update);
    }

    @Override
    public void delete(UUID packageId) {
        Document query = new Document("_id", packageId.toString());
        collection.deleteOne(query);
    }

    @Override
    public Set<TravelPackage> getPackagesByName(String name) {
        Set<TravelPackage> packages = new HashSet<>();

        Document query = new Document("name", name);
        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
            while (cursor.hasNext()) {
                packages.add(documentToTravelPackage(cursor.next()));
            }
        }

        return packages;
    }

    @Override
    public Set<TravelPackage> getPackagesByDestinationId(UUID destinationId) {
        Set<TravelPackage> packages = new HashSet<>();

        Document query = new Document("destination_id", destinationId.toString());

        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
            while (cursor.hasNext()) {
                packages.add(documentToTravelPackage(cursor.next()));
            }
        }

        return packages;
    }

    @Override
    public Set<TravelPackage> getPackagesByDestinationIdAndDuration(UUID destinationId, int duration) {
        Set<TravelPackage> packages = new HashSet<>();
        Document query = new Document("destination_id", destinationId.toString()).append("duration", duration);

        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
            while (cursor.hasNext()) {
                packages.add(documentToTravelPackage(cursor.next()));
            }
        }

        return packages;
    }

    private TravelPackage documentToTravelPackage(Document document) {
        UUID packageId = UUID.fromString(document.getString("_id"));
        String name = document.getString("name");
        UUID destinationId = UUID.fromString(document.getString("destination_id"));
        int duration = document.getInteger("duration");
        BigDecimal price = document.get("price", Decimal128.class).bigDecimalValue();

        return new TravelPackage(packageId, name, destinationId, duration, price);
    }

    private Document travelPackageToDocument(TravelPackage aPackage) {
        String packageId = aPackage.getPackageId().toString();
        String name = aPackage.getName();
        String destinationId = aPackage.getDestinationId().toString();
        int duration = aPackage.getDuration();
        Decimal128 price = new Decimal128(aPackage.getPrice());

        return new Document("_id", packageId).append("name", name).append("destination_id", destinationId).append("duration", duration).append("price", price);
    }
}
