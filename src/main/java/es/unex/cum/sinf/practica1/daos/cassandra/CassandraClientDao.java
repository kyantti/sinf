package es.unex.cum.sinf.practica1.daos.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import es.unex.cum.sinf.practica1.daos.ClientDao;
import es.unex.cum.sinf.practica1.entities.Client;

import java.util.*;

public class CassandraClientDao implements ClientDao {
    private final Session session;
    private final String keyspace;

    public CassandraClientDao(Session session, String keyspace){
        this.session = session;
        this.keyspace = keyspace;
    }

    @Override
    public Client get(UUID clientId) {
        String query = "SELECT * FROM " + keyspace + ".clients WHERE client_id = ?";
        ResultSet resultSet = session.execute(query, clientId);
        Row row = resultSet.one();

        if (row != null) {
            return mapRowToClient(row);
        } else {
            return null;
        }
    }

    @Override
    public Set<Client> getAll() {
        Set <Client> clients = new HashSet<>();

        String query = "SELECT * FROM " + keyspace + ".clients";
        ResultSet resultSet = session.execute(query);

        for (Row row : resultSet) {
            Client client = mapRowToClient(row);
            clients.add(client);
        }

        return clients;
    }

    @Override
    public void insert(Client client) {
        String query = "INSERT INTO " + keyspace + ".clients (client_id, name, email, phone) VALUES (?, ?, ?, ?)";
        session.execute(query, client.getClientId(), client.getName(), client.getEmail(), client.getTelephone());
    }

    @Override
    public void update(Client client) {
        String query = "UPDATE " + keyspace + ".clients SET name = ?, email = ?, phone = ? WHERE client_id = ?";
        session.execute(query, client.getName(), client.getEmail(), client.getTelephone(), client.getClientId());
    }

    @Override
    public void delete(UUID clientId) {
        String query = "DELETE FROM " + keyspace + ".clients WHERE client_id = ?";
        session.execute(query, clientId);
    }

    @Override
    public Client getClientByEmail(String email) {
        String query = "SELECT * FROM " + keyspace + ".clients WHERE email = ?";
        ResultSet resultSet = session.execute(query, email);
        Row row = resultSet.one();

        if (row != null) {
            return mapRowToClient(row);
        } else {
            return null;
        }
    }

    private Client mapRowToClient(Row row) {
        UUID clientId = row.getUUID("client_id");
        String name = row.getString("name");
        String email = row.getString("email");
        String phone = row.getString("phone");

        return new Client(clientId, name, email, phone);
    }
}
