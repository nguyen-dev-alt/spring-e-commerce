package com.app.e_commerce.repository;

import com.app.e_commerce.entity.Product;
import com.app.e_commerce.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    List<Rate> findByProduct(Product product);
}
