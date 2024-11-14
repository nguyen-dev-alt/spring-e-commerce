package com.app.e_commerce.services;

import com.app.e_commerce.entity.Product;
import com.app.e_commerce.entity.Rate;
import com.app.e_commerce.repository.ProductRepo;
import com.app.e_commerce.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateService {
    @Autowired
    private RateRepository rateRepository;
    @Autowired
    private ProductService productService;


    public List<Rate> getRatesByProduct(Long productId) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return rateRepository.findByProduct(product);
    }
    public Rate saveRate(Rate rate) {
        return rateRepository.save(rate);
    }
}
