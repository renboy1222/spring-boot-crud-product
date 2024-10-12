/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.product.repository;

import com.aldrin.product.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Java Programming with Aldrin
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Page<Category> findByNameContaining(String name, Pageable pageable);
}