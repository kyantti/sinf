package es.unex.cum.sinf.practica1.reports;

import es.unex.cum.sinf.practica1.entities.TravelPackage;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.util.List;

public class TravelPackageDataSource implements JRDataSource {
    private List<TravelPackage> travelPackageList;
    private int index;

    public TravelPackageDataSource(List<TravelPackage> travelPackageList) {
        this.travelPackageList = travelPackageList;
        index = -1;
    }

    public boolean next() throws JRException {
        return ++index < travelPackageList.size();
    }

    public Object getFieldValue(JRField jrf) throws JRException {
        Object value = null;

        switch (jrf.getName()) {
            case "Travel Package ID":
                value = travelPackageList.get(index).getPackageId();
                break;
            case "Name":
                value = travelPackageList.get(index).getName();
                break;
            case "Destination ID":
                value = travelPackageList.get(index).getDestinationId();
                break;
            case "Duration (Days)":
                value = travelPackageList.get(index).getDuration();
                break;
            case "Price (€)":
                value = travelPackageList.get(index).getPrice();
                break;
            default:
                break;
        }

        return value;
    }
}
