package com.lajming.investmentsapp;

import com.google.common.base.Preconditions;
import lombok.val;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

class MoneyUtils {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    static BigDecimal calculatePercentage(Money all, Money part) {
        return part.getAmount()
            .divide(all.getAmount())
            .multiply(ONE_HUNDRED)
            .stripTrailingZeros();
    }

    static Money multiplyByPercentage(Money money, double percentage) {
        return money
            .multipliedBy(percentage/100, RoundingMode.DOWN)
            .rounded(0, RoundingMode.DOWN);
    }

    static boolean anyAmountLessThanOne(Collection<Money> amounts) {
        return amounts.stream().anyMatch(MoneyUtils::isLessThanOneUnit);
    }

    static Money divideMoney(Money amount, double divider) {
        return amount.dividedBy(divider, RoundingMode.DOWN).rounded(0, RoundingMode.DOWN);
    }

    static Money sumMoney(Collection<Money> amounts) {
        Preconditions.checkArgument(!amounts.isEmpty());

        val currencyUnit = amounts.iterator().next().getCurrencyUnit();
        val sum = amounts.stream().map(Money::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        return Money.of(currencyUnit, sum);
    }

    private static boolean isLessThanOneUnit(Money amount) {
        return amount.getAmount().compareTo(BigDecimal.ONE) < 0;
    }

}
