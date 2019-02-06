package com.lajming.investmentsapp;

import com.google.common.collect.Lists;
import lombok.val;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Multimaps.index;
import static com.lajming.investmentsapp.CollectionUtils.*;
import static com.lajming.investmentsapp.MoneyUtils.*;
import static com.lajming.investmentsapp.Portfolio.emptyPortfolioWithReminder;
import static java.util.function.Function.identity;

public class PortfolioGenerator {

    private final Money allAvailableMoney;
    private final InvestmentStrategy investmentStrategy;
    private final Collection<Fund> availableFunds;
    private final Collection<PortfolioItem> portfolioItems;

    private PortfolioGenerator(Money allAvailableMoney, InvestmentStrategy investmentStrategy, Collection<Fund> availableFunds) {
        this.investmentStrategy = investmentStrategy;
        this.availableFunds = availableFunds;
        this.allAvailableMoney = allAvailableMoney;
        this.portfolioItems = Lists.newLinkedList();

        validateInput();
    }

    public static PortfolioGenerator of(Money allAvailableMoney, InvestmentStrategy investmentStrategy, Collection<Fund> availableFunds) {
        return new PortfolioGenerator(allAvailableMoney, investmentStrategy, availableFunds);
    }

    public Portfolio generatePortfolio() {
        val fundTypesToFundsMap = index(availableFunds, Fund::getType);
        val moneyAmountPerEachFundGroup = toMap(fundTypesToFundsMap.keySet(), identity(), amountForFundType());

        if (notAbleToGeneratePortfolio(moneyAmountPerEachFundGroup)) {
            return emptyPortfolioWithReminder(allAvailableMoney);
        }

        val investedMoney = sumMoney(moneyAmountPerEachFundGroup.values());
        val reminder = allAvailableMoney.minus(investedMoney);

        for (val fundType : fundTypesToFundsMap.keySet()) {
            val funds = fundTypesToFundsMap.get(fundType);
            val moneyAmountForGroup = moneyAmountPerEachFundGroup.get(fundType);
            val averageAmount = divideMoney(moneyAmountForGroup, funds.size());
            val predictedInvestment = averageAmount.multipliedBy(funds.size());
            val groupReminder = moneyAmountForGroup.minus(predictedInvestment);
            val firstItemAmount = averageAmount.plus(groupReminder);

            BigDecimal averagePercentage = calculatePercentage(investedMoney, averageAmount);
            BigDecimal firstItemPercentage = calculatePercentage(investedMoney, firstItemAmount);
            portfolioItems.add(PortfolioItem.of(firstElement(funds), firstItemAmount, firstItemPercentage));

            for (val fund : allElementsExceptFirst(funds)) {
                portfolioItems.add(PortfolioItem.of(fund, averageAmount, averagePercentage));
            }
        }

        return Portfolio.of(portfolioItems, reminder);
    }

    private void validateInput() {
        checkNotNull(investmentStrategy, "provided investment strategy should not be null");
        checkNotNull(availableFunds, "provided funds should not be null");
        checkNotNull(allAvailableMoney, "provided money should not be null");
        checkArgument(allAvailableMoney.isPositiveOrZero(), "negative amount of money provided");
    }

    private boolean notAbleToGeneratePortfolio(Map<FundType, Money> amountPerEachFundGroup) {
        return noFundsProvided()
            || fundsMissingForAtLeastOneTypeOfFunds(amountPerEachFundGroup)
            || anyGroupWillGetAmountLessThanOne(amountPerEachFundGroup);
    }

    private boolean noFundsProvided() {
        return availableFunds.isEmpty();
    }

    private boolean fundsMissingForAtLeastOneTypeOfFunds(Map<FundType, Money> amountPerEachFundGroup) {
        return !amountPerEachFundGroup.keySet().containsAll(investmentStrategy.getFundTypes());
    }

    private boolean anyGroupWillGetAmountLessThanOne(Map<FundType, Money> amountPerEachFundGroup) {
        return anyAmountLessThanOne(amountPerEachFundGroup.values());
    }

    private Function<FundType, Money> amountForFundType() {
        return fundType -> multiplyByPercentage(allAvailableMoney, investmentStrategy.getPercentage(fundType));
    }

}
