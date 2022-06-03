package com.by.gomel.gstu.repository;

import com.by.gomel.gstu.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    void deleteById(Long id);

    Category getCategoryByCategoryName(String categoryName);
}
