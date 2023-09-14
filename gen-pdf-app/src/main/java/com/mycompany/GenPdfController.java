package com.mycompany;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GenPdfController {


    @GetMapping
    public void generatePdf() {

        Map<String, String> subReports = new HashMap<>();

        try {
            // get data for reports
            // save and return filePath
            String path = "c:/practice/java/gen-pdf-app";
            String filePath = "data.json";
            InputStream inputStream = new FileInputStream(new File(path + "/" + filePath));

            String outputFilePath = System.getProperty("java.io.tmpdir") + filePath;
            OutputStream out = Files.newOutputStream(Paths.get(outputFilePath));
            inputStream.transferTo(out);

            // get main report
            InputStream mainJrxmlStream = GenPdfController.class.getClassLoader()
                    .getResourceAsStream("jaspers/mycompany.jasper");

            Map<String, Object> jasperDataMap = new HashMap<>();
            jasperDataMap.put("net.sf.jasperreports.json.source", filePath);
            // if has image, logo
            //jasperDataMap.put("imagePath", imagePath);
            if (subReports.size() > 0) {
                jasperDataMap.putAll(subReports);
            }

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(mainJrxmlStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, jasperDataMap);

            mainJrxmlStream.close();
            String pdfFile = "c:/practice/java/gen-pdf-app/report.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
