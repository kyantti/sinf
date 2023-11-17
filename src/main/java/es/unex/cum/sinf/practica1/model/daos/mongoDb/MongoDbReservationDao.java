package es.unex.cum.sinf.practica1.model.daos.mongoDb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import es.unex.cum.sinf.practica1.model.daos.ReservationDao;
import es.unex.cum.sinf.practica1.model.entities.Reservation;

public class MongoDbReservationDao implements ReservationDao {

    private final MongoCollection<Document> collection;

    public MongoDbReservationDao(MongoDatabase database) {
        this.collection = database.getCollection("reservations");
    }

    @Override
    public Reservation get(UUID reservationId) {
        Document query = new Document("reservation_id", reservationId.toString());
        Document result = collection.find(query).first();

        if (result != null) {
            return documentToReservation(result);
        } else {
            return null;
        }
    }

    @Override
    public Set<Reservation> getAll() {
        Set<Reservation> reservations = new HashSet<>();

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                reservations.add(documentToReservation(cursor.next()));
            }
        }

        return reservations;
    }

    @Override
    public void insert(Reservation reservation) {
        Document document = reservationToDocument(reservation);
        collection.insertOne(document);
    }

    @Override
    public void update(Reservation reservation) {
        Document filter = new Document("reservation_id", reservation.getReservationId().toString());
        Document update = new Document("$set",
                new Document("client_id", reservation.getClientId().toString())
                        .append("package_id", reservation.getPackageId().toString())
                        .append("start_date", reservation.getStartDate())
                        .append("end_date", reservation.getEndDate())
                        .append("payed", reservation.isPayed()));

        collection.updateOne(filter, update);
    }

    @Override
    public void delete(UUID reservationId) {
        Document query = new Document("reservation_id", reservationId.toString());
        collection.deleteOne(query);
    }

    @Override
    public Set<Reservation> getReservationsByClientId(UUID clientId) {
        Set<Reservation> reservations = new HashSet<>();

        Document query = new Document("client_id", clientId.toString());
        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
            while (cursor.hasNext()) {
                reservations.add(documentToReservation(cursor.next()));
            }
        }

        return reservations;
    }

    @Override
    public Set<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) {
        Set<Reservation> reservations = new HashSet<>();

        Document query = new Document("start_date", new Document("$gte", startDate))
                .append("end_date", new Document("$lte", endDate));

        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
            while (cursor.hasNext()) {
                reservations.add(documentToReservation(cursor.next()));
            }
        }

        return reservations;
    }

    @Override
    public Set<Reservation> getReservationsByPackageId(UUID packageId) {
        Set<Reservation> reservations = new HashSet<>();

        Document query = new Document("package_id", packageId.toString());
        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
            while (cursor.hasNext()) {
                reservations.add(documentToReservation(cursor.next()));
            }
        }

        return reservations;
    }

    @Override
    public Set<Reservation> getReservationsByClientIdAndDateRange(UUID clientId, LocalDate startDate, LocalDate endDate) {
        Set <Reservation> reservations = new HashSet<>();

        Document query = new Document("client_id", clientId.toString())
                .append("start_date", new Document("$gte", startDate))
                .append("end_date", new Document("$lte", endDate));

        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
            while (cursor.hasNext()) {
                reservations.add(documentToReservation(cursor.next()));
            }
        }

        return reservations;
    }

    private Reservation documentToReservation(Document document) {
        UUID reservationId = UUID.fromString(document.getString("_id"));
        UUID packageId = UUID.fromString(document.getString("package_id"));
        UUID client_id = UUID.fromString(document.getString("client_id"));
        LocalDate startDate = document.getDate("start_date").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = document.getDate("end_date").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        boolean payed = document.getBoolean("payed");

        return new Reservation(reservationId, packageId, client_id, startDate, endDate, payed);
    }

    private Document reservationToDocument(Reservation reservation) {
        String reservationId = reservation.getReservationId().toString();
        String clientId = reservation.getClientId().toString();
        String packageId = reservation.getPackageId().toString();
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();
        boolean payed = reservation.isPayed();

        return new Document("_id", reservationId)
                .append("client_id", clientId)
                .append("package_id", packageId)
                .append("start_date", startDate)
                .append("end_date", endDate)
                .append("payed", payed);

    }
}
