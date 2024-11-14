package com.app.e_commerce.controller;

import com.app.e_commerce.data.RevenueDataPoint;
import com.app.e_commerce.services.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/revenue")
@CrossOrigin("*")

public class RevenueController {
    @Autowired
    private RevenueService revenueService;

    @GetMapping("/day")
    public ResponseEntity<BigDecimal> getRevenueByDay(
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        BigDecimal revenue = revenueService.calculateRevenueByDay(date);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/month")
    public ResponseEntity<BigDecimal> getRevenueByMonth(
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year) {
        if (month == null || year == null) {
            LocalDate now = LocalDate.now();
            month = now.getMonthValue();
            year = now.getYear();
        }
        BigDecimal revenue = revenueService.calculateRevenueByMonth(month, year);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/year")
    public ResponseEntity<BigDecimal> getRevenueByYear(
            @RequestParam(value = "year", required = false) Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        BigDecimal revenue = revenueService.calculateRevenueByYear(year);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<RevenueDataPoint>> getMonthlyRevenue(
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate) {
        List<RevenueDataPoint> revenueData;
        if (startDate != null && endDate != null) {
            revenueData = revenueService.getDailyRevenueBetweenDates(startDate, endDate);
        } else {
            if (month == null || year == null) {
                LocalDate now = LocalDate.now();
                month = now.getMonthValue();
                year = now.getYear();
            }
            revenueData = revenueService.getDailyRevenueForMonth(month, year);
        }
        return ResponseEntity.ok(revenueData);
    }

    @GetMapping("/yearly-revenue")
    public ResponseEntity<List<RevenueDataPoint>> getYearlyRevenue(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "startYear", required = false) Integer startYear,
            @RequestParam(value = "endYear", required = false) Integer endYear) {
        List<RevenueDataPoint> revenueData;
        if (startYear != null && endYear != null) {
            revenueData = revenueService.getMonthlyRevenueBetweenYears(startYear, endYear);
        } else {
            if (year == null) {
                year = LocalDate.now().getYear();
            }
            revenueData = revenueService.getMonthlyRevenueForYear(year);
        }
        return ResponseEntity.ok(revenueData);
    }
}