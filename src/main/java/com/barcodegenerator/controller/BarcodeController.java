package com.barcodegenerator.controller;

import com.barcodegenerator.service.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BarcodeController {

    @Autowired
    private BarcodeService barcodeService;

    @GetMapping("/")
    public String home() {
        return "index"; // Returns the homepage
    }

    // Correct mapping for barcode generator form
    @GetMapping("/generate")
    public String getBarcodeGeneratorForm() {
        return "barcode"; // Returns the barcode generator form
    }

    @PostMapping("/generate")
    public String generateBarcode(
            @RequestParam String productName,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String shape,
            Model model) {

        // Generate a random 3-digit number
        int randomNum = (int) (Math.random() * 900) + 100; // Generates a number between 100 and 999

        // Get the first letter of size, color, company name, and shape
        String sizeInitial = (size != null && !size.isEmpty()) ? String.valueOf(size.charAt(0)).toUpperCase() : "";
        String colorInitial = (color != null && !color.isEmpty()) ? String.valueOf(color.charAt(0)).toUpperCase() : "";
        String companyInitial = (companyName != null && !companyName.isEmpty()) ? String.valueOf(companyName.charAt(0)).toUpperCase() : "";
        String shapeInitial = (shape != null && !shape.isEmpty()) ? String.valueOf(shape.charAt(0)).toUpperCase() : "";

        // Construct the barcode text including size, color, company, and shape initials
        String barcodeText = String.format("%d-%s-%s-%s-%s-%s",
                randomNum, sizeInitial, colorInitial, companyInitial, shapeInitial, productName);

        String fileName = barcodeService.generateUniqueFileName(productName);
        String filePath = fileName;

        try {
            barcodeService.generateBarcodeAndSaveProduct(barcodeText, filePath, productName, companyName, size, color, shape); // Use the barcodeRequest directly
            model.addAttribute("message", "Barcode generated successfully!");
            model.addAttribute("barcodeImage", "/images/barcodes/" + fileName);
        } catch (Exception e) {
            model.addAttribute("message", "Error generating barcode: " + e.getMessage());
        }

        return "barcode"; // Return the view name
    }
}
