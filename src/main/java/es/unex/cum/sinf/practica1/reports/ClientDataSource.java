package es.unex.cum.sinf.practica1.reports;

import es.unex.cum.sinf.practica1.entities.Client;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.util.List;

public class ClientDataSource implements JRDataSource {
    private List<Client> clientList;
    private int index;

    public ClientDataSource(List<Client> clientList) {
        this.clientList = clientList;
        index = -1;
    }

    public boolean next() throws JRException {
        return ++index < clientList.size();
    }

    public Object getFieldValue(JRField jrf) throws JRException {
        Object value = null;

        switch (jrf.getName()) {
            case "Client ID":
                value = clientList.get(index).getClientId();
                break;
            case "Name":
                value = clientList.get(index).getName();
                break;
            case "E-Mail":
                value = clientList.get(index).getEmail();
                break;
            case "Telephone":
                value = clientList.get(index).getTelephone();
                break;
            default:
                break;
        }

        return value;
    }

}
