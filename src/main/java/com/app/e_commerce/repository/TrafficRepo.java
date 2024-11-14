package com.app.e_commerce.repository;

import com.app.e_commerce.entity.Traffic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
@Repository
public interface TrafficRepo extends JpaRepository<Traffic, Long> {
    Optional<Traffic> findByDate(LocalDate date);
}
