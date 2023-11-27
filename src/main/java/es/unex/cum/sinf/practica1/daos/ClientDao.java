package es.unex.cum.sinf.practica1.daos;

import java.util.UUID;

import es.unex.cum.sinf.practica1.entities.Client;

public interface ClientDao extends Dao <Client, UUID> {
    Client getClientByEmail(String email);
}
