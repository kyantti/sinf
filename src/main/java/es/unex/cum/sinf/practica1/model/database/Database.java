package es.unex.cum.sinf.practica1.model.database;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import java.util.logging.Logger;

public class Database {
    private Cluster cluster;
	private Session session;
	private Logger logger = Logger.getLogger(getClass().getName());

	public Cluster getCluster() {
		return cluster;
	}

	public Session getSession() {
		return session;
	}
	
    public void connect(String node) {
		cluster = Cluster.builder().addContactPoint(node).build();
		Metadata metadata = cluster.getMetadata();
		logger.info("Connected to cluster: " + metadata.getClusterName());
		for (Host host : metadata.getAllHosts()) {
			logger.info("Datatacenter: " + host.getDatacenter() + ", Host: " + host.getAddress() + ", Rack: " + host.getRack());
		}
		session = cluster.connect();
	}

    public void close() {
		session.close();
		cluster.close();
		logger.info("Connection closed");
	}


}
