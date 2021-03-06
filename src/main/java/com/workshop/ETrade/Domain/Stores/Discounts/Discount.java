package com.workshop.ETrade.Domain.Stores.Discounts;

import com.workshop.ETrade.Domain.Stores.Calculators.Calculator;
import com.workshop.ETrade.Domain.Stores.Calculators.CategoryCalculator;
import com.workshop.ETrade.Domain.Stores.Calculators.ProductCalculator;
import com.workshop.ETrade.Domain.Stores.Calculators.StoreCalculator;
import com.workshop.ETrade.Domain.Stores.Product;
import com.workshop.ETrade.Persistance.Stores.DiscountDTO;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Map;

public class Discount {

    @DBRef(lazy = true)
    private Calculator calculator;
    private String description;
    private int id;

    private DiscountType type;

    public Discount(int id) {
        this.calculator = null;
        description = null;
        this.id = id;
    }

    public Discount(int id, String discountOn , int discountPercentage, String description, DiscountType type) {
        this.description = description;
        this.id = id;
        switch (type) {
            case CATEGORY -> calculator = new CategoryCalculator(discountOn, discountPercentage);
            case PRODUCT -> calculator = new ProductCalculator(discountOn, discountPercentage);
            case STORE -> calculator = new StoreCalculator(discountPercentage);
        }
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public Calculator getCalculator() {
        return calculator;
    }

    public Map<Product, Double> calculatePrices(Map<Product, Double> prices , Map<Product, Integer> amounts) {
        return calculator.calcPrices(prices);
    }

    public boolean shouldApply(Map<Product, Integer> amounts) {
        return true;
    }

    public String getDiscountOn() {
        return calculator.getDiscountOn();
    }

    public int getDiscountPercentage() {
        return calculator.getDiscountPercentage();
    }

    public String getDescription() {
        return description;
    }

    public DiscountType getType() {
        return type;
    }

    public DiscountDTO init() {
        return new DiscountDTO(this);
    }
}
