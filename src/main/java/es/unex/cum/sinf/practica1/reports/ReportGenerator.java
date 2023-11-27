package es.unex.cum.sinf.practica1.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;

public class ReportGenerator {
    public static void generateReport(JRDataSource dataSource, String sourceFileName, String destFileName) {
        try {
            String printFileName = JasperFillManager.fillReportToFile(sourceFileName, null, dataSource);

            if (printFileName != null) {
                JasperExportManager.exportReportToPdfFile(printFileName, destFileName);
                JasperExportManager.exportReportToHtmlFile(printFileName, destFileName);
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
