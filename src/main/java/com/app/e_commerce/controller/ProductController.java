package com.app.e_commerce.controller;

import com.app.e_commerce.entity.Category;
import com.app.e_commerce.entity.Product;
import com.app.e_commerce.entity.Rate;
import com.app.e_commerce.services.CategoryService;
import com.app.e_commerce.services.ProductService;
import com.app.e_commerce.services.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

import java.util.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RateService rateService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/add-product";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file) throws IOException {
        productService.saveProduct(product, file);
        return "redirect:/products";
    }
    private Set<Category> processCategories(String categoriesString) {
        Set<Category> categories = new HashSet<>();
        String[] categoryNames = categoriesString.split(",");
        for (String categoryName : categoryNames) {
            Category category = categoryService.findOrCreateCategoryByName(categoryName.trim());
            if (category != null) {
                categories.add(category);
            } else {
                throw new RuntimeException(STR."Không tìm thấy danh mục: \{categoryName}");
            }
        }
        return categories;
    }
    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size) {
        Page<Product> productPage = productService.listProduct(page, size);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("size", size);
        return "product/products"; // Thymeleaf view name
    }
    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model) {
        List<Product> searchResults = productService.searchProducts(keyword.trim().toLowerCase());
        model.addAttribute("products", searchResults);
        model.addAttribute("keyword", keyword);

        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        model.addAttribute("totalItems", searchResults.size());
        model.addAttribute("size", searchResults.size());

        return "product/products";
    }
    @GetMapping("/edit/{id}")
    public String showUpdateProductForm(@PathVariable Long id, Model model) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("product", product);
            model.addAttribute("categories", categories);
            return "product/edit-product"; // Thymeleaf template for editing a product
        } else {
            return "redirect:/products"; // Redirect if product not found
        }
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute("product") Product product,
                                @RequestParam("file") MultipartFile file,
                                RedirectAttributes redirectAttributes) {
        try {
            product.setId(id);
            productService.saveProduct(product, file);
            redirectAttributes.addFlashAttribute("success", "Product updated successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update product!");
        }
        return "redirect:/products";
    }
    // New method for deleting a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Model model) {
        try {
            productService.deleteProduct(id);
            return "redirect:/products";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi xóa sản phẩm: " + e.getMessage());
            return "redirect:/products";
        }
    }
    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Optional<Product> productOptional = productService.getProductById(id);
        List<Rate> rates = rateService.getRatesByProduct(id);
        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            model.addAttribute("rates", rates);
            return "product/product-details"; // Replace with your actual view name for displaying product details
        } else {
            model.addAttribute("errorMessage", "Product not found");
            return "product/product-not-found"; // Replace with a suitable error view
        }
    }
//    @GetMapping("/find")
//    @ResponseBody
//    public List<Product> searchProducts(@RequestParam("query") String query) {
//        return productService.searchProducts(query);
//    }

}
