package com.lajming.investmentsapp;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

class TestUtils {

    static Money amountOfPLN(double amount) {
        return Money.of(currencyPLN(), amount);
    }

    static List<Fund> listOfFunds(Fund... funds) {
        return Arrays.asList(funds);
    }

    static BigDecimal bigDecimal(String number) {
        return new BigDecimal(number).stripTrailingZeros();
    }

    static Fund polishInvestmentFund(Long id, String name) {
        return Fund.of(id, FundType.POLISH_INVESTMENT_FUND, name);
    }

    static Fund foreignInvestmentFund(Long id, String name) {
        return Fund.of(id, FundType.FOREIGN_INVESTMENT_FUND, name);
    }

    static Fund moneyMarketFund(Long id, String name) {
        return Fund.of(id, FundType.MONEY_MARKET_FUND, name);
    }

    private static CurrencyUnit currencyPLN() {
        return CurrencyUnit.of("PLN");
    }

}
