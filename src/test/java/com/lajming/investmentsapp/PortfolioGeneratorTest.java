package com.lajming.investmentsapp;

import lombok.val;
import org.junit.Test;

import java.util.Collections;

import static com.lajming.investmentsapp.TestData.*;
import static com.lajming.investmentsapp.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

public class PortfolioGeneratorTest {

    @Test
    public void whenNullValueIsProvidedInsteadOfFundsWhenConstructingTheGenerator_anExceptionShouldBeThrownWithProperMessage() {
        // Arrange
        val availableMoney = amountOfPLN(100);

        // Act
        Throwable thrown = catchThrowable(
            () -> PortfolioGenerator.of(availableMoney, InvestmentStrategy.CONSERVATIVE, null)
        );

        // Assert
        assertThat(thrown)
            .isInstanceOf(NullPointerException.class)
            .hasMessage("provided funds should not be null")
            .hasNoCause();
    }

    @Test
    public void whenNullValueIsProvidedInsteadOfMoneyWhenConstructingTheGenerator_anExceptionShouldBeThrownWithProperMessage() {
        // Arrange
        val availableFunds = Collections.<Fund>emptyList();

        // Act
        Throwable thrown = catchThrowable(
            () -> PortfolioGenerator.of(null, InvestmentStrategy.CONSERVATIVE, availableFunds)
        );

        // Assert
        assertThat(thrown)
            .isInstanceOf(NullPointerException.class)
            .hasMessage("provided money should not be null")
            .hasNoCause();
    }

    @Test
    public void whenNegativeAmountOfMoneyIsProvided_shouldThrowAnExceptionWithProperMessage() {
        // Arrange
        val availableMoney = amountOfPLN(-100);
        val availableFunds = Collections.<Fund>emptyList();

        // Act
        Throwable thrown = catchThrowable(
            () -> PortfolioGenerator.of(availableMoney, InvestmentStrategy.CONSERVATIVE, availableFunds)
        );

        // Assert
        assertThat(thrown)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("negative amount of money provided")
            .hasNoCause();
    }

    @Test
    public void whenNoFundsProvidedAtAll_shouldReturnEmptyPortfolioAndWholeAmountAsAReminder() {
        // Arrange
        val availableMoney = amountOfPLN(100);
        val availableFunds = Collections.<Fund>emptyList();
        val portfolioGenerator = PortfolioGeneratorFactory
            .aggressivePortfolioGenerator(availableMoney, availableFunds);

        // Act
        val portfolio = portfolioGenerator.generatePortfolio();

        // Assert
        val portfolioItems = portfolio.getPortfolioItems();
        val reminder = portfolio.getReminder();

        assertThat(portfolioItems).isEmpty();
        assertThat(reminder).isEqualTo(availableMoney);
    }

    @Test
    public void whenNoFundsProvidedForAtLeastOneTypeOfFund_shouldReturnEmptyPortfolioAndWholeAmountAsAReminder() {
        // Arrange
        val availableMoney = amountOfPLN(100);
        val availableFunds = listOfFunds(polishInvestmentFund(1L, TEST_POLISH_INVESTMENT_FUND_NAME_1));
        val portfolioGenerator = PortfolioGeneratorFactory
            .conservativePortfolioGenerator(availableMoney, availableFunds);

        // Act
        val portfolio = portfolioGenerator.generatePortfolio();

        // Assert
        val portfolioItems = portfolio.getPortfolioItems();
        val reminder = portfolio.getReminder();

        assertThat(portfolioItems).isEmpty();
        assertThat(reminder).isEqualTo(availableMoney);
    }

    @Test
    public void whenOnlyOneFundPerFundType_returnedPercentageForEachItemShouldBeTheSameAsInTheStrategy() {
        // Arrange
        val availableMoney = amountOfPLN(100);
        val availableFunds = listOfFunds(
            polishInvestmentFund(1L, TEST_POLISH_INVESTMENT_FUND_NAME_1),
            foreignInvestmentFund(2L, TEST_FOREIGN_INVESTMENT_FUND_NAME_1),
            moneyMarketFund(3L, TEST_MONEY_MARKET_FUND_NAME_1)
        );
        val portfolioGenerator = PortfolioGeneratorFactory.conservativePortfolioGenerator(availableMoney, availableFunds);

        // Act
        val portfolio = portfolioGenerator.generatePortfolio();

        // Assert
        val portfolioItems = portfolio.getPortfolioItems();
        val reminder = portfolio.getReminder();

        assertThat(portfolioItems)
            .contains(
                PortfolioItem.of(availableFunds.get(0), amountOfPLN(20), bigDecimal("20")),
                PortfolioItem.of(availableFunds.get(1), amountOfPLN(75), bigDecimal("75")),
                PortfolioItem.of(availableFunds.get(2), amountOfPLN(5), bigDecimal("5"))
            );
        assertThat(reminder).isEqualTo(amountOfPLN(0));
    }

    @Test
    public void whenThereIsLittleAmountOfMoneyButItCanBeDivided_shouldReturnProperlyGeneratedPortfolio() {
        // Arrange
        val availableMoney = amountOfPLN(10);
        val availableFunds = listOfFunds(
            polishInvestmentFund(1L, TEST_POLISH_INVESTMENT_FUND_NAME_1),
            foreignInvestmentFund(2L, TEST_FOREIGN_INVESTMENT_FUND_NAME_1),
            moneyMarketFund(3L, TEST_MONEY_MARKET_FUND_NAME_1)
        );
        val portfolioGenerator = PortfolioGeneratorFactory.balancedPortfolioGenerator(availableMoney, availableFunds);

        // Act
        val portfolio = portfolioGenerator.generatePortfolio();

        // Assert
        val portfolioItems = portfolio.getPortfolioItems();
        val reminder = portfolio.getReminder();

        assertThat(portfolioItems)
            .contains(
                PortfolioItem.of(availableFunds.get(0), amountOfPLN(3), bigDecimal("30")),
                PortfolioItem.of(availableFunds.get(1), amountOfPLN(6), bigDecimal("60")),
                PortfolioItem.of(availableFunds.get(2), amountOfPLN(1), bigDecimal("10"))
            );
        assertThat(reminder).isEqualTo(amountOfPLN(0));
    }

    @Test
    public void whenDueToTheLittleAmountOfMoneyOneOfTheFundGroupsWontGetAnyMoney_shouldReturnEmptyPortfolioAndTheWholeAmountAsAReminder() {
        // Arrange
        val availableMoney = amountOfPLN(10);
        val availableFunds = listOfFunds(
            polishInvestmentFund(1L, TEST_POLISH_INVESTMENT_FUND_NAME_1),
            foreignInvestmentFund(2L, TEST_FOREIGN_INVESTMENT_FUND_NAME_1),
            moneyMarketFund(3L, TEST_MONEY_MARKET_FUND_NAME_1)
        );
        val portfolioGenerator = PortfolioGeneratorFactory.conservativePortfolioGenerator(availableMoney, availableFunds);

        // Act
        val portfolio = portfolioGenerator.generatePortfolio();

        // Assert
        val portfolioItems = portfolio.getPortfolioItems();
        val reminder = portfolio.getReminder();

        assertThat(portfolioItems).isEmpty();
        assertThat(reminder).isEqualTo(availableMoney);
    }

    @Test
    public void whenThereIsAdditionalAmountOfMoneyWhichCantBeDivided_shouldReturnItAsAReminder() {
        // Arrange
        val availableMoney = amountOfPLN(101);
        val availableFunds = listOfFunds(
            polishInvestmentFund(1L, TEST_POLISH_INVESTMENT_FUND_NAME_1),
            foreignInvestmentFund(2L, TEST_FOREIGN_INVESTMENT_FUND_NAME_1),
            moneyMarketFund(3L, TEST_MONEY_MARKET_FUND_NAME_1)
        );
        val portfolioGenerator = PortfolioGeneratorFactory.conservativePortfolioGenerator(availableMoney, availableFunds);

        // Act
        val portfolio = portfolioGenerator.generatePortfolio();

        // Assert
        val portfolioItems = portfolio.getPortfolioItems();
        val reminder = portfolio.getReminder();

        assertThat(portfolioItems)
            .contains(
                PortfolioItem.of(availableFunds.get(0), amountOfPLN(20), bigDecimal("20")),
                PortfolioItem.of(availableFunds.get(1), amountOfPLN(75), bigDecimal("75")),
                PortfolioItem.of(availableFunds.get(2), amountOfPLN(5), bigDecimal("5"))
            );
        assertThat(reminder).isEqualTo(amountOfPLN(1));
    }

    @Test
    public void whenAveragePercentageForFundInAGroupIsARepeatingDecimal_theAveragePercentageShouldBeRoundedAndMissingAmountShouldBeAddedToTheFirstFundInTheGroup() {
        // Arrange
        val availableMoney = amountOfPLN(10000);
        val availableFunds = listOfFunds(
            polishInvestmentFund(1L, TEST_POLISH_INVESTMENT_FUND_NAME_1),
            polishInvestmentFund(2L, TEST_POLISH_INVESTMENT_FUND_NAME_2),
            polishInvestmentFund(3L, TEST_POLISH_INVESTMENT_FUND_NAME_3),
            foreignInvestmentFund(4L, TEST_FOREIGN_INVESTMENT_FUND_NAME_1),
            foreignInvestmentFund(5L, TEST_FOREIGN_INVESTMENT_FUND_NAME_2),
            moneyMarketFund(6L, TEST_MONEY_MARKET_FUND_NAME_1)
        );
        val portfolioGenerator = PortfolioGeneratorFactory.conservativePortfolioGenerator(availableMoney, availableFunds);

        // Act
        val portfolio = portfolioGenerator.generatePortfolio();

        // Assert
        val portfolioItems = portfolio.getPortfolioItems();
        val reminder = portfolio.getReminder();

        assertThat(portfolioItems)
            .contains(
                PortfolioItem.of(availableFunds.get(0), amountOfPLN(668), bigDecimal("6.68")),
                PortfolioItem.of(availableFunds.get(1), amountOfPLN(666), bigDecimal("6.66")),
                PortfolioItem.of(availableFunds.get(2), amountOfPLN(666), bigDecimal("6.66")),
                PortfolioItem.of(availableFunds.get(3), amountOfPLN(3750), bigDecimal("37.5")),
                PortfolioItem.of(availableFunds.get(4), amountOfPLN(3750), bigDecimal("37.5")),
                PortfolioItem.of(availableFunds.get(5), amountOfPLN(500), bigDecimal("5"))
            );
        assertThat(reminder).isEqualTo(amountOfPLN(0));
    }

    @Test
    public void whenPercentageForSomeFundsHasLongFractionalPart_thePercentageShouldBeCalculatedAccordingToTheAmountOfMoney() {
        // Arrange
        val availableMoney = amountOfPLN(100000);
        val availableFunds = listOfFunds(
            polishInvestmentFund(1L, TEST_POLISH_INVESTMENT_FUND_NAME_1),
            polishInvestmentFund(2L, TEST_POLISH_INVESTMENT_FUND_NAME_2),
            polishInvestmentFund(3L, TEST_POLISH_INVESTMENT_FUND_NAME_3),
            foreignInvestmentFund(4L, TEST_FOREIGN_INVESTMENT_FUND_NAME_1),
            foreignInvestmentFund(5L, TEST_FOREIGN_INVESTMENT_FUND_NAME_2),
            moneyMarketFund(6L, TEST_MONEY_MARKET_FUND_NAME_1)
        );
        val portfolioGenerator = PortfolioGeneratorFactory.conservativePortfolioGenerator(availableMoney, availableFunds);

        // Act
        val portfolio = portfolioGenerator.generatePortfolio();

        // Assert
        val portfolioItems = portfolio.getPortfolioItems();
        val reminder = portfolio.getReminder();

        assertThat(portfolioItems)
            .contains(
                PortfolioItem.of(availableFunds.get(0), amountOfPLN(6668), bigDecimal("6.668")),
                PortfolioItem.of(availableFunds.get(1), amountOfPLN(6666), bigDecimal("6.666")),
                PortfolioItem.of(availableFunds.get(2), amountOfPLN(6666), bigDecimal("6.666")),
                PortfolioItem.of(availableFunds.get(3), amountOfPLN(37500), bigDecimal("37.5")),
                PortfolioItem.of(availableFunds.get(4), amountOfPLN(37500), bigDecimal("37.5")),
                PortfolioItem.of(availableFunds.get(5), amountOfPLN(5000), bigDecimal("5"))
            );
        assertThat(reminder).isEqualTo(amountOfPLN(0));
    }

    @Test
    public void whenAverageAmountForItemsInAGroupIsZero_theFirstFundInAGroupShouldGetTheWholeAmountAssignedToThatGroup() {
        // Arrange
        val availableMoney = amountOfPLN(10);
        val availableFunds = listOfFunds(
            polishInvestmentFund(1L, TEST_POLISH_INVESTMENT_FUND_NAME_1),
            foreignInvestmentFund(2L, TEST_FOREIGN_INVESTMENT_FUND_NAME_1),
            moneyMarketFund(3L, TEST_MONEY_MARKET_FUND_NAME_1),
            moneyMarketFund(4L, TEST_MONEY_MARKET_FUND_NAME_2)
        );
        val portfolioGenerator = PortfolioGeneratorFactory.balancedPortfolioGenerator(availableMoney, availableFunds);

        // Act
        val portfolio = portfolioGenerator.generatePortfolio();

        // Assert
        val portfolioItems = portfolio.getPortfolioItems();
        val reminder = portfolio.getReminder();

        assertThat(portfolioItems)
            .contains(
                PortfolioItem.of(availableFunds.get(0), amountOfPLN(3), bigDecimal("30")),
                PortfolioItem.of(availableFunds.get(1), amountOfPLN(6), bigDecimal("60")),
                PortfolioItem.of(availableFunds.get(2), amountOfPLN(1), bigDecimal("10")),
                PortfolioItem.of(availableFunds.get(3), amountOfPLN(0), bigDecimal("0"))
            );
        assertThat(reminder).isEqualTo(amountOfPLN(0));
    }

}
