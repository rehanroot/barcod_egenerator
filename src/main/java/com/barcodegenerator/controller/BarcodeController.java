package com.barcodegenerator.controller;

import com.barcodegenerator.model.Product;
import com.barcodegenerator.repository.ProductRepository;
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

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String showHomePage(Model model) {
        // You can add any data you want to pass to the homepage template here
        return "index"; // Ensure this matches the HTML file name (index.html)
    }

    @GetMapping("/barcode")
    public String showBarcodePage(Model model) {
        model.addAttribute("message", ""); // Initialize the message
        return "barcode"; // Ensure this matches the HTML file name (barcode.html)
    }

    @PostMapping("/generate")
    public String generateBarcode(@RequestParam("productName") String productName,
                                  @RequestParam(value = "companyName", required = false) String companyName,
                                  @RequestParam(value = "size", required = false) String size,
                                  @RequestParam(value = "color", required = false) String color,
                                  @RequestParam(value = "shape", required = false) String shape,
                                  Model model) {
        // Generate a random 3-digit number
        int randomNum = (int) (Math.random() * 900) + 100; // Generates a number between 100 and 999

        // Get the first letter of size, color, company name, and shape
        String sizeInitial = (size != null && !size.isEmpty()) ? String.valueOf(size.charAt(0)).toUpperCase() : "";
        String colorInitial = (color != null && !color.isEmpty()) ? String.valueOf(color.charAt(0)).toUpperCase() : "";
        String companyInitial = (companyName != null && !companyName.isEmpty()) ? String.valueOf(companyName.charAt(0)).toUpperCase() : "";
        String shapeInitial = (shape != null && !shape.isEmpty()) ? String.valueOf(shape.charAt(0)).toUpperCase() : "";

        // Construct the barcode text including size, color, company, and shape initials
        String barcodeText = String.format("%d-%s-%s-%s-%s-%s", randomNum, sizeInitial, colorInitial, companyInitial, shapeInitial, productName);

        String fileName = barcodeService.generateUniqueFileName(productName);
        String filePath = fileName;

        try {
            Product product = new Product(productName, color, size, shape, companyName, filePath);
            productRepository.save(product);

            barcodeService.generateBarcodeAndSaveProduct(barcodeText, filePath);
            model.addAttribute("message", "Barcode generated successfully!");
            model.addAttribute("barcodeImage", "/images/barcodes/" + fileName);
        } catch (Exception e) {
            model.addAttribute("message", "Error generating barcode: " + e.getMessage());
        }

        return "barcode";
    }

}
