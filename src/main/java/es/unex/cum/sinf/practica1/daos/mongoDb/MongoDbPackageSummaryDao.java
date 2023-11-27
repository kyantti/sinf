package es.unex.cum.sinf.practica1.daos.mongoDb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.*;

import es.unex.cum.sinf.practica1.daos.PackageSummaryDao;
import es.unex.cum.sinf.practica1.entities.PackageReservationSummary;

public class MongoDbPackageSummaryDao implements PackageSummaryDao {

    private final MongoCollection<Document> collection;

    public MongoDbPackageSummaryDao(MongoDatabase database) {
        this.collection = database.getCollection("package_summary");
    }

    @Override
    public PackageReservationSummary get(UUID packageId) {
        Document query = new Document("_id", packageId.toString());
        Document result = collection.find(query).first();

        if (result != null) {
            return documentToPackageReservationSummary(result);
        }
        return null;
    }

    @Override
    public Set<PackageReservationSummary> getAll() {
        Set<PackageReservationSummary> summaryList = new HashSet<>();

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                summaryList.add(documentToPackageReservationSummary(cursor.next()));
            }
        }

        return summaryList;
    }

    @Override
    public void insert(PackageReservationSummary packageReservationSummary) {
    }

    @Override
    public void update(PackageReservationSummary packageReservationSummary) {
        Document query = new Document("_id", packageReservationSummary.getPackageId().toString());
        Document update = new Document("$inc", new Document("total_reservations", packageReservationSummary.getTotalReservations()));
        UpdateOptions options = new UpdateOptions().upsert(true); // Set upsert option to true

        collection.updateOne(query, update, options);
    }

    @Override
    public void delete(UUID packageId) {
        Document query = new Document("_id", packageId.toString());
        collection.deleteOne(query);
    }

    private PackageReservationSummary documentToPackageReservationSummary(Document document) {

        UUID packageID = UUID.fromString(document.getString("_id"));
        long totalReservations = document.getLong("total_reservations");

        return new PackageReservationSummary(packageID, totalReservations);
    }

    // You can create a method to convert PackageReservationSummary to Document if needed.
}
