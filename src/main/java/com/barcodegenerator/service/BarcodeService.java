package com.barcodegenerator.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.barcodegenerator.model.Product;
import com.barcodegenerator.model.dto.BarcodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BarcodeService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${product.api.url:http://localhost:1004/api/products}")
    private String productApiUrl; // Injecting URL from application.properties

    public void generateBarcodeImage(String barcodeText, String filePath) throws WriterException, IOException {
        File directory = createDirectory("src/main/resources/static/images/barcodes");

        Path path = new File(directory, filePath).toPath();
        BitMatrix bitMatrix = new QRCodeWriter().encode(barcodeText, BarcodeFormat.QR_CODE, 200, 100);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    private File createDirectory(String directoryPath) throws IOException {
        File directory = new File(directoryPath);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory: " + directory.getAbsolutePath());
        }
        return directory;
    }

    public String generateUniqueFileName(String productName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String sanitizedProductName = productName.replaceAll("\\s+", "_");
        return sanitizedProductName + "_" + timestamp + ".png";
    }

    public void generateBarcodeAndSaveProduct(String barcodeText, String filePath, String productName, String companyName, String size, String color, String shape) throws WriterException, IOException {
        generateBarcodeImage(barcodeText, filePath);

        // Create Product object from parameters
        Product product = new Product();
        product.setName(productName);
        product.setColor(color);
        product.setSize(size);
        product.setShape(shape);
        product.setCompanyName(companyName);
        product.setBarcodePath(filePath);

        try {
            restTemplate.postForObject(productApiUrl, product, Product.class);
        } catch (Exception e) {
            // Handle HTTP request error, possibly log the exception
            throw new RuntimeException("Error while saving product: " + e.getMessage(), e);
        }
    }
}
