package com.lajming.investmentsapp;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@ToString
enum InvestmentStrategy {
    CONSERVATIVE(ImmutableMap.of(
        FundType.POLISH_INVESTMENT_FUND, 20d,
        FundType.FOREIGN_INVESTMENT_FUND, 75d,
        FundType.MONEY_MARKET_FUND, 5d
    )),
    BALANCED(ImmutableMap.of(
        FundType.POLISH_INVESTMENT_FUND, 30d,
        FundType.FOREIGN_INVESTMENT_FUND, 60d,
        FundType.MONEY_MARKET_FUND, 10d
    )),
    AGGRESSIVE(ImmutableMap.of(
        FundType.POLISH_INVESTMENT_FUND, 40d,
        FundType.FOREIGN_INVESTMENT_FUND, 20d,
        FundType.MONEY_MARKET_FUND, 40d
    ));

    private final Map<FundType, Double> investmentStrategy;

    public double getPercentage(FundType fundType) {
        return investmentStrategy.getOrDefault(fundType, 0.0d);
    }

    public Set<FundType> getFundTypes() {
        return investmentStrategy.keySet();
    }

}
