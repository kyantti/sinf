package es.unex.cum.sinf.practica1.model.entities;

import java.util.UUID;

/**
 * Client
 */
public class Client {
    private UUID clientId;
    private String name;
    private String email;
    private String telephone;

    public Client(UUID clientId, String name, String email, String telephone) {
        this.clientId = clientId;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "Client [clientId=" + clientId + ", name=" + name + ", email=" + email + ", telephone=" + telephone
                + "]";
    }

    

}