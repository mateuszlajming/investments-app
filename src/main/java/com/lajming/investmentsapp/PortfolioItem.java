package com.lajming.investmentsapp;

import lombok.Value;
import lombok.experimental.Delegate;
import org.joda.money.Money;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
class PortfolioItem {
    @Delegate
    private final Fund fund;
    private final Money amount;
    private final BigDecimal percentage;

    @Override
    public String toString() {
        return PortfolioItem.class.getSimpleName() + "(fund=" + fund + ", " + "amount=" + amount + ", percentage=" + percentage.toPlainString() + "%)";
    }
}
