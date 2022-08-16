package com.hmm.dms.util;

import com.hmm.dms.service.dto.RptParamsDTO;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

public class ReportPrint {

    public static String print(List<?> rptData, RptParamsDTO rptPara, Map<String, Object> parameters) {
        Logger log = LoggerFactory.getLogger(ReportPrint.class);
        log.debug("ReportPrint:print() Step1");
        log.debug("Report Output Path:" + rptPara.getRptOutputPath());

        String rptFilePath = "";
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rptData);
            File file = ResourceUtils.getFile("classpath:jrxml/" + rptPara.getRptJrxml());
            JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters, dataSource);

            log.debug("ReportPrint:print() Step2");
            /*
             * File
             * jasperFile=ResourceUtils.getFile("classpath:jasper/"+rptPara.getRptJasper());
             * String sourceFileName=jasperFile.getAbsolutePath(); String
             * printFileName=JasperFillManager.fillReportToFile(sourceFileName, parameters,
             * dataSource);
             */

            File outputDir = new File(rptPara.getRptOutputPath());
            if (outputDir.exists() && outputDir.isDirectory()) {
                log.debug("ReportPrint:print() Step3");
                rptFilePath = export(rptPara, jasperPrint);
            } else {
                boolean isSuccess = outputDir.mkdir();
                if (isSuccess) {
                    log.debug("ReportPrint:print() Step4");
                    rptFilePath = export(rptPara, jasperPrint);
                }
            }
        } catch (FileNotFoundException fe) {
            System.out.print(fe.getMessage());
        } catch (JRException jre) {
            System.out.println(jre.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return rptFilePath;
    }

    private static String export(RptParamsDTO rptPara, JasperPrint jasperPrint) throws JRException {
        String rptFilePath;

        // Export HTML file
        rptFilePath = rptPara.getRptOutputPath() + rptPara.getRptFileName() + ".html";
        JasperExportManager.exportReportToHtmlFile(jasperPrint, rptFilePath);

        // Export PDF file
        rptFilePath = rptPara.getRptOutputPath() + rptPara.getRptFileName() + ".pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint, rptFilePath);

        // Export Excel File
        rptFilePath = rptPara.getRptOutputPath() + rptPara.getRptFileName() + ".xls";
        JRXlsExporter xlsExporter = new JRXlsExporter();
        xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(rptFilePath));
        xlsExporter.exportReport();

        return rptFilePath;
    }
}
