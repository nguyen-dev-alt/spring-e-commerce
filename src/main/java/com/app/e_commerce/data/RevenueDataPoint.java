package com.app.e_commerce.data;

import java.math.BigDecimal;

public class RevenueDataPoint {

    private String label;  // This can be the day or month (e.g., "2024-09-01" or "January")
    private BigDecimal revenue;  // Revenue for the specific day or month

    public RevenueDataPoint(String label, BigDecimal revenue) {
        this.label = label;
        this.revenue = revenue;
    }

    // Getters and setters
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
}
