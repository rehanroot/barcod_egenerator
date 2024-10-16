package com.barcodegenerator.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BarcodeService {

    public void generateBarcodeImage(String barcodeText, String filePath) throws WriterException, IOException {
        // Create the directory if it does not exist
        File directory = new File("src/main/resources/static/images/barcodes"); // Correct path to your static folder
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created: " + directory.getAbsolutePath());
            } else {
                throw new IOException("Failed to create directory: " + directory.getAbsolutePath());
            }
        }

        // Generate barcode image
        Path path = new File(directory, filePath).toPath(); // Save the barcode image inside the created directory
        BitMatrix bitMatrix = new QRCodeWriter().encode(barcodeText, BarcodeFormat.QR_CODE, 200, 100); // Using QR_CODE, change if needed
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public String generateUniqueFileName(String productName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String sanitizedProductName = productName.replaceAll("\\s+", "_"); // Replace spaces with underscores
        return sanitizedProductName + "_" + timestamp + ".png"; // Create a unique filename
    }

    public void generateBarcodeAndSaveProduct(String barcodeText, String filePath) throws WriterException, IOException {
        generateBarcodeImage(barcodeText, filePath);
        // Additional logic for saving the product details in the database can be added here
    }
}
