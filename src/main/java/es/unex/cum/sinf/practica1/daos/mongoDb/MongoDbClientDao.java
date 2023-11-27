package es.unex.cum.sinf.practica1.daos.mongoDb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import es.unex.cum.sinf.practica1.daos.ClientDao;
import es.unex.cum.sinf.practica1.entities.Client;
import org.bson.Document;

import java.util.*;

public class MongoDbClientDao implements ClientDao {
    private final MongoCollection<Document> clientsCollection;

    public MongoDbClientDao(MongoDatabase database) {
        this.clientsCollection = database.getCollection("clients");
    }

    @Override
    public Client get(UUID clientId) {
        Document query = new Document("_id", clientId.toString());
        Document result = clientsCollection.find(query).first();

        assert result != null;
        return mapDocumentToClient(result);

    }

    @Override
    public Set<Client> getAll() {
        Set<Client> clients = new HashSet<>();
        try (MongoCursor<Document> cursor = clientsCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                clients.add(mapDocumentToClient(document));
            }
        }
        return clients;
    }

    @Override
    public void insert(Client client) {
        Document document = new Document()
                .append("_id", client.getClientId().toString())
                .append("name", client.getName())
                .append("email", client.getEmail())
                .append("phone", client.getTelephone());

        clientsCollection.insertOne(document);
    }

    @Override
    public void update(Client client) {
        Document filter = new Document("_id", client.getClientId().toString());
        Document update = new Document("$set", new Document()
                .append("name", client.getName())
                .append("email", client.getEmail())
                .append("phone", client.getTelephone()));

        clientsCollection.updateOne(filter, update);
    }

    @Override
    public void delete(UUID clientId) {
        Document query = new Document("_id", clientId.toString());
        clientsCollection.deleteOne(query);
    }

    @Override
    public Client getClientByEmail(String email) {
        Document query = new Document("email", email);
        Document result = clientsCollection.find(query).first();

        assert result != null;
        return mapDocumentToClient(result);

    }

    private Client mapDocumentToClient(Document document) {
        UUID clientId = UUID.fromString(document.getString("_id"));
        String name = document.getString("name");
        String email = document.getString("email");
        String phone = document.getString("phone");

        return new Client(clientId, name, email, phone);
    }
}
