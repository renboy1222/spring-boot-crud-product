/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.product.service;

import com.aldrin.product.model.Product;
import com.aldrin.product.repository.ProductRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.List;

/**
 *
 * @author Java Programming with Aldrin
 */

@Service
public class PdfService {

    private final ProductRepository productRepository;

    public PdfService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    
    public ByteArrayInputStream generateProductPDF(List<Product> products) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add a title
            document.add(new Paragraph("Product List").setBold().setFontSize(18));

            // Create a table with two columns (Name and Price)
            Table table = new Table(4);
            table.addHeaderCell("#");
            table.addHeaderCell("Product ");
            table.addHeaderCell("Price");
            table.addHeaderCell("Category");

            // Populate the table with products
            int i =1;
            DecimalFormat df = new DecimalFormat("#,###.00");
            for (Product product : products) {
                table.addCell(String.valueOf(i)+".");
                table.addCell(product.getName());
                table.addCell(String.valueOf(df.format(product.getPrice())));
                table.addCell(product.getCategory().getName());
                i++;
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
