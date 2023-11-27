package es.unex.cum.sinf.practica1.reports;

import es.unex.cum.sinf.practica1.entities.Destination;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.util.List;

public class DestinationDataSource implements  JRDataSource{
    private List<Destination> destinationList;
    private int index;

    public DestinationDataSource(List<Destination> destinationList) {
        this.destinationList = destinationList;
        index = -1;
    }

    public boolean next() throws JRException {
        return ++index < destinationList.size();
    }

    public Object getFieldValue(JRField jrf) throws JRException {
        Object value = null;

        switch (jrf.getName()) {
            case "Destination ID":
                value = destinationList.get(index).getDestinationId();
                break;
            case "Country":
                value = destinationList.get(index).getCountry();
                break;
            case "Description":
                value = destinationList.get(index).getDescription();
                break;
            case "Name":
                value = destinationList.get(index).getName();
                break;
            case "Climate":
                value = destinationList.get(index).getWeather();
                break;
            default:
                break;
        }

        return value;
    }

}
