package es.unex.cum.sinf.practica1.model.databaseConnection.cassandra;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import es.unex.cum.sinf.practica1.model.databaseConnection.Connection;

import java.util.logging.Logger;

public class CassandraConnection implements Connection {
    private Cluster cluster;
	private Session session;
	private Logger logger = Logger.getLogger(getClass().getName());

	public Cluster getCluster() {
		return cluster;
	}

	public Session getSession() {
		return session;
	}
	@Override
    public void open(String node) {
		cluster = Cluster.builder().addContactPoint(node).build();
		Metadata metadata = cluster.getMetadata();
		logger.info("Connected to cluster: " + metadata.getClusterName());
		for (Host host : metadata.getAllHosts()) {
			logger.info("Datatacenter: " + host.getDatacenter() + ", Host: " + host.getAddress() + ", Rack: " + host.getRack());
		}
		session = cluster.connect();
	}
	@Override
    public void close() {
		session.close();
		cluster.close();
		logger.info("Connection closed");
	}


}
