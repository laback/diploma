package com.example.application.service;


import com.example.application.model.Category;
import com.example.application.repository.CategoryRepository;
import com.example.application.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //Возвращает все категории
    public List<Category> getAllCategoriesByPage(int page){
        return PageUtils.getAllEntitiesByPage(categoryRepository, page);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public long getMaxPages(){
        return PageUtils.getMaxPages(categoryRepository);
    }

    //Добавляет категорию
    public void addCategory(Category category){
        category.setCreated(LocalDateTime.now());
        category.setUpdated(LocalDateTime.now());

        categoryRepository.save(category);
    }

    public Category getCategoryByName(String name){
        return categoryRepository.getCategoryByCategoryName(name);
    }

    //Добавляет список категорий
    public void addCategories(List<Category> categories){
        categoryRepository.saveAll(categories);
    }

    //Удаляет категорию
    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }
}
