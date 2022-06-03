package com.by.gomel.gstu.controller;

import com.by.gomel.gstu.model.Category;
import com.by.gomel.gstu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //Получает все категори
    @GetMapping("user/categories/{page}")
    public String getAllCategories(Model model, @PathVariable int page, @Param("parentCategoryName") String parentCategoryName){

        var categories = categoryService.getAllCategories(parentCategoryName);

        model.addAttribute("categories", categoryService.getCategoriesByPage(categories, page));

        model.addAttribute("parentCategoryName", parentCategoryName);
        model.addAttribute("page", page);
        model.addAttribute("maxPages", categoryService.getMaxPages(categories));
        return "categories/index";
    }

    //Возвращает форму для добавления категории
    @GetMapping("user/categories/add")
    public String getCategoryAddForm(Model model){
        model.addAttribute("category", new Category());
        model.addAttribute("categories", categoryService.getAllCategories());

        return "categories/add";
    }

    //Получает данные о новой категории с формы и добавляет в базу
    @PostMapping("user/categories")
    public String addCategory(@ModelAttribute(name = "category") Category category){

        categoryService.addCategory(category);

        return "redirect:categories";
    }

    //Получает идентификатор удаляемой категории и удаляет из базы
    @DeleteMapping("user/categories/{id}")
    public String deleteCategory(@PathVariable(name = "id") Long id){
        categoryService.deleteCategory(id);

        return "redirect:";
    }
}
