package es.unex.cum.sinf.practica1.reports;

import es.unex.cum.sinf.practica1.entities.PackageReservationSummary;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.util.List;

public class PkgReservSummaryDataSource implements JRDataSource {
    private List<PackageReservationSummary> summaryList;
    private int index;

    public PkgReservSummaryDataSource(List<PackageReservationSummary> summaryList) {
        this.summaryList = summaryList;
        index = -1;
    }

    public boolean next() throws JRException {
        return ++index < summaryList.size();
    }

    public Object getFieldValue(JRField jrf) throws JRException {
        Object value = null;

        switch (jrf.getName()) {
            case "Travel Package ID":
                value = summaryList.get(index).getPackageId();
                break;
            case "Total Reservations":
                value = summaryList.get(index).getTotalReservations();
                break;
            default:
                break;
        }

        return value;
    }

}
