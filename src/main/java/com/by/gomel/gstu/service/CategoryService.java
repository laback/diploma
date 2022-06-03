package com.by.gomel.gstu.service;


import com.by.gomel.gstu.model.Category;
import com.by.gomel.gstu.util.PageUtils;
import com.by.gomel.gstu.repository.CategoryRepository;
import com.by.gomel.gstu.repository.DetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final DetailRepository detailRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, DetailRepository detailRepository) {
        this.categoryRepository = categoryRepository;
        this.detailRepository = detailRepository;
    }

    //Возвращает все категории
    public List<Category> getCategoriesByPage(List<Category> categories, int page){

        setDetailsCount(categories);
        return PageUtils.getAllEntitiesByPage(categories, page);
    }

    public List<Category> getAllCategories(String parentCategoryName){
        var categories = categoryRepository.findAll();

        if(parentCategoryName != null && !parentCategoryName.isBlank()){
            categories = categories.stream().filter(category -> category.getParentCategory()!= null && parentCategoryName.toLowerCase().contains(category.getParentCategory().getCategoryName().toLowerCase())).collect(Collectors.toList());
        }

        return categories;
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public List<Category> getAllChildCategory(){
        return getAllCategories().stream().filter(category -> category.getParentCategory() != null).collect(Collectors.toList());
    }

    public long getMaxPages(List<Category> categories){
        return PageUtils.getMaxPages(categories);
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

    private void setDetailsCount(List<Category> categories){
        categories.forEach(category -> {
            if(category.getParentCategory() != null){
                category.setDetailsCount(detailRepository.getDetailsByCategory(category).size());
            }
        });
    }
}
