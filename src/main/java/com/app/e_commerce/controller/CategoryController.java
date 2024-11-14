package com.app.e_commerce.controller;

import com.app.e_commerce.entity.Category;
import com.app.e_commerce.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/new-category")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/category-form";
    }
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") @Valid Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "category-form";
        }
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            model.addAttribute("category", category);
            return "category/category-form";
        } else {
            return "redirect:/categories";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "category/categories";
    }
}

