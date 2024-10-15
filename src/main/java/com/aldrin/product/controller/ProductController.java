/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.product.controller;

/**
 *
 * @author Java Programming with Aldrin
 */

import com.aldrin.product.model.Product;
import com.aldrin.product.service.CategoryService;
import com.aldrin.product.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;

@Controller
@RequestMapping("/products")
public class ProductController {


    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService,CategoryService categoryService) {
        this.productService = productService;
        this.categoryService =categoryService;
    }

   @GetMapping
    public String viewProductsPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String selectedItem) {
        Page<Product> productPage = productService.findPaginatedAndSearched(page, size, keyword);
        long totalResults = productService.countSearchResults(keyword);
        String selectedColor = "#FFFFFF"; // Default color

        // Add selected color logic based on item
        if (selectedItem != null) {
            selectedColor = "#FF5733"; // Set a custom color if an item is selected
        }
        int totalPages = productPage.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedItem", selectedItem);
        model.addAttribute("color", selectedColor);
        model.addAttribute("totalResults", totalResults);

        return "product-list";
    }
    
    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "product-form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("image") MultipartFile image) {
        try {
            productService.saveProduct(product.getName(), product.getPrice(), image,product.getCategory());
            return "redirect:/products";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
    
 @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }


    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        return "product-form";
    }
    

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product, @RequestParam("image") MultipartFile image) {
        try {
            productService.updateProduct(id, product.getName(), product.getPrice(), image,product.getCategory());
            return "redirect:/products";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/list")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product-list";
    }

    // Display image on the list
    @GetMapping("/image/{id}")
    @ResponseBody
    public byte[] showProductImage(@PathVariable Long id) {
        return productService.getProductById(id).map(Product::getPhoto).orElse(null);
    }
    
    @GetMapping("/{id}/photo")
    @ResponseBody
    public byte[] getStudentPhoto(@PathVariable Long id) {
        Product product = productService.findById(id);
        return product != null ? product.getPhoto() : null;
    }
}

