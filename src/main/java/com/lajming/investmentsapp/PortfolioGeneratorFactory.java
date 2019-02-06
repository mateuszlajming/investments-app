package com.lajming.investmentsapp;

import org.joda.money.Money;

import java.util.Collection;

class PortfolioGeneratorFactory {
    public static PortfolioGenerator conservativePortfolioGenerator(Money money, Collection<Fund> funds) {
        return PortfolioGenerator.of(money, InvestmentStrategy.CONSERVATIVE, funds);
    }

    public static PortfolioGenerator balancedPortfolioGenerator(Money money, Collection<Fund> funds) {
        return PortfolioGenerator.of(money, InvestmentStrategy.BALANCED, funds);
    }

    public static PortfolioGenerator aggressivePortfolioGenerator(Money money, Collection<Fund> funds) {
        return PortfolioGenerator.of(money, InvestmentStrategy.AGGRESSIVE, funds);
    }
}
