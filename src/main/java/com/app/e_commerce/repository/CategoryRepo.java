package com.app.e_commerce.repository;

import com.app.e_commerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category,Long> {

    Category findByName(String name);
}
