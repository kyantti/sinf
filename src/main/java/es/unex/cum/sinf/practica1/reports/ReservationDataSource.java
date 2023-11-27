package es.unex.cum.sinf.practica1.reports;

import es.unex.cum.sinf.practica1.entities.Reservation;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.util.List;

public class ReservationDataSource implements JRDataSource {
    private List<Reservation> reservationList;
    private int index;

    public ReservationDataSource(List<Reservation> reservationList) {
        this.reservationList = reservationList;
        index = -1;
    }

    public boolean next() throws JRException {
        return ++index < reservationList.size();
    }

    public Object getFieldValue(JRField jrf) throws JRException {
        Object value = null;

        switch (jrf.getName()) {
            case "Reservation ID":
                value = reservationList.get(index).getReservationId();
                break;
            case "Travel Package ID":
                value = reservationList.get(index).getPackageId();
                break;
            case "Client ID":
                value = reservationList.get(index).getClientId();
                break;
            case "Start Date":
                value = reservationList.get(index).getStartDate();
                break;
            case "End Date":
                value = reservationList.get(index).getEndDate();
                break;
            default:
                break;
        }

        return value;
    }

}
