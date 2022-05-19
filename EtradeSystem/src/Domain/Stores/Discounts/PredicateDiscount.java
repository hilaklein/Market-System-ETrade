package Domain.Stores.Discounts;

import Domain.Stores.Calculators.Calculator;
import Domain.Stores.Predicates.OperatorComponent;
import Domain.Stores.Product;

import java.util.Map;

public class PredicateDiscount extends Discount {
    OperatorComponent operatorComponent;

    public PredicateDiscount(int id, String discountOn , int discountPercentage, String description, DiscountType type, OperatorComponent operatorComponent) {
        super(id, discountOn, discountPercentage, description, type);
        this.operatorComponent = operatorComponent;
    }

    public Map<Product, double> calculatePrices(Map<Product, double> prices, Map<Product, int> amounts) {
        Calculator calculator = getCalculator();
        if(operatorComponent.shouldApply(amounts)) {
            calculator.calcPrices(prices);
        }
        return prices;
    }

    @Override
    public boolean shouldApply(Map<Product, int> amounts) {
        return operatorComponent.shouldApply(amounts);
    }
}
