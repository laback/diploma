package com.example.application.repository;

import com.example.application.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    void deleteById(Long id);

    Category getCategoryByCategoryName(String categoryName);
}
