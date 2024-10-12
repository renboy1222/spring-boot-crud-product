/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.product.controller;

import com.aldrin.product.model.Product;
import com.aldrin.product.repository.ProductRepository;
import com.aldrin.product.service.PdfService;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Java Programming with Aldrin
 */

@Controller
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private ProductRepository productRepository;


    @GetMapping("/products/download-pdf")
    public ResponseEntity<InputStreamResource> downloadPdf() {
        List<Product> products= productRepository.findAll();
        ByteArrayInputStream pdfStream = pdfService.generateProductPDF(products);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=products.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }
}

