/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.product.controller;

import com.aldrin.product.model.Category;
import com.aldrin.product.service.CategoryService;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Java Programming with Aldrin
 */

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping
    public String viewCategoryPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String selectedItem) {
        Page<Category> productPage = categoryService.findPaginatedAndSearched(page, size, keyword);
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

        model.addAttribute("categoryPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedItem", selectedItem);
        model.addAttribute("color", selectedColor);

        return "category-list";
    }

    @GetMapping("/new")
    public String newCategory(Model model) {
        model.addAttribute("category", new Category());
        return "category-form";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoryService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        return "category-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return "redirect:/categories";
    }
}
