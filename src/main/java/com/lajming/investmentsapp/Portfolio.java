package com.lajming.investmentsapp;

import lombok.Value;
import lombok.val;
import org.joda.money.Money;

import java.util.Collection;

import static com.google.common.base.Joiner.on;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@Value(staticConstructor = "of")
public class Portfolio {
    private final Collection<PortfolioItem> portfolioItems;
    private final Money reminder;

    static Portfolio emptyPortfolioWithReminder(Money reminder) {
        return Portfolio.of(emptyList(), reminder);
    }

    public String toMultilineString() {
        val classNameString = Portfolio.class.getSimpleName();
        val portfolioItemsString = on(lineSeparator()).join(portfolioItems);
        val reminderString = "reminder="+reminder;

        return on(lineSeparator()).join(asList(classNameString+"(", portfolioItemsString, reminderString+")"));
    }
}
