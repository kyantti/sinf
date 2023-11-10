package es.unex.cum.sinf.practica1.model.daos.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import es.unex.cum.sinf.practica1.model.daos.ClientDao;
import es.unex.cum.sinf.practica1.model.entities.Client;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CassandraClientDao implements ClientDao {
    private Session session;

    public CassandraClientDao(Session session) {
        this.session = session;
    }

    @Override
    public Client get(UUID clientId) {
        String query = "SELECT * FROM pablo_setrakian_bearzotti.clients WHERE client_id = ?";
        ResultSet resultSet = session.execute(query, clientId);
        Row row = resultSet.one();

        if (row != null) {
            return new Client(
                    row.getUUID("client_id"),
                    row.getString("name"),
                    row.getString("email"),
                    row.getString("phone")
            );
        } else {
            return null;
        }
    }

    @Override
    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();

        String query = "SELECT * FROM clients";
        ResultSet resultSet = session.execute(query);

        for (Row row : resultSet) {
            Client client = new Client(
                    row.getUUID("client_id"),
                    row.getString("name"),
                    row.getString("email"),
                    row.getString("phone")
            );
            clients.add(client);
        }

        return clients;
    }

    @Override
    public void insert(Client client) {
        String query = "INSERT INTO pablo_setrakian_bearzotti.clients (client_id, name, email, phone) VALUES (?, ?, ?, ?)";
        session.execute(query, client.getClientId(), client.getName(), client.getEmail(), client.getTelephone());
    }

    @Override
    public void update(Client client) {
        String query = "UPDATE pablo_setrakian_bearzotti.clients SET name = ?, email = ?, phone = ? WHERE client_id = ?";
        session.execute(query, client.getName(), client.getEmail(), client.getTelephone(), client.getClientId());
    }

    @Override
    public void delete(UUID clientId) {
        String query = "DELETE FROM pablo_setrakian_bearzotti.clients WHERE client_id = ?";
        session.execute(query, clientId);
    }

}
