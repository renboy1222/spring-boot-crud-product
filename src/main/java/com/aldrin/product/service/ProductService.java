/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.product.service;

import com.aldrin.product.model.Category;
import com.aldrin.product.model.Product;
import com.aldrin.product.repository.ProductRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Java Programming with Aldrin
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(String name, Double price, MultipartFile image, Category category) throws IOException {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);

        if (!image.isEmpty()) {
            product.setPhoto(image.getBytes());
        }

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, String name, Double price, MultipartFile image, Category category) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(name);
            product.setPrice(price);
            product.setCategory(category);

            if (!image.isEmpty()) {
                product.setPhoto(image.getBytes());
            }

            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public Page<Product> findPaginatedAndSearched(int pageNo, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);  // Page number starts from 0
        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByNameContaining(keyword, pageable);
        }
        return productRepository.findAll(pageable);
    }

    public long countSearchResults(String keyword) {
        // Count the total number of results for a given search keyword
        return productRepository.searchByName(keyword, Pageable.unpaged()).getTotalElements();
    }

}
