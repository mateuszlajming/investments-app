# investments-app

Simple app which **generates a portfolio** based on the:

-  amount of money
-  preferred investment strategy
-  list of available funds

####Investment strategies

|Name|Polish Investment Funds|Foreign Investment Funds|Money Market Funds|
|---|---|---|---|
|conservative|20%|75%|5%|
|balanced|30%|60%|10%|
|aggressive|40%|20%|40%|

## Example

### Input
*  **Amount of money**: 10 000 PLN
*  **Investment strategy**: conservative
*  **Available funds**:

|#|Type|Name|
|---|---|---|
|1|Polish Investment Fund|Polish Investment Fund 1|
|2|Polish Investment Fund|Polish Investment Fund 2|
|3|Foreign Investment Fund|Foreign Investment Fund 1|
|4|Foreign Investment Fund|Foreign Investment Fund 2|
|5|Foreign Investment Fund|Foreign Investment Fund 3|
|6|Money Market Fund|Money Market Fund 1|

### Output
* **Generated portfolio**:

|#|Type|Name|Amount|Percentage|
|---|---|---|---|---|
|1|Polish Investment Fund|Polish Investment Fund 1|1000 PLN|10%|
|2|Polish Investment Fund|Polish Investment Fund 2|1000 PLN|10%|
|3|Foreign Investment Fund|Foreign Investment Fund 1|2500 PLN|25%|
|4|Foreign Investment Fund|Foreign Investment Fund 2|2500 PLN|25%|
|5|Foreign Investment Fund|Foreign Investment Fund 3|2500 PLN|25%|
|6|Money Market Fund|Money Market Fund 1|500 PLN|5%|

---

## Documentation

### Money
```java
val availableMoney = Money.of(CurrencyUnit.of("PLN"), 10000);
```

### Funds
```java
val polishInvestmentFund    = Fund.of(1L, FundType.POLISH_INVESTMENT_FUND,  "Polish Investment Fund");
val foreignInvestmentFund   = Fund.of(2L, FundType.FOREIGN_INVESTMENT_FUND, "Foreign Investment Fund");
val moneyMarketFund         = Fund.of(3L, FundType.MONEY_MARKET_FUND,       "Money Market Fund");

val availableFunds = Arrays.asList(
    polishInvestmentFund,
    foreignInvestmentFund,
    moneyMarketFund
);
```

### PortfolioGenerator
```java
val conservativePortfolioGenerator  = PortfolioGeneratorFactory.conservativePortfolioGenerator(availableMoney, availableFunds);

val portfolio = conservativePortfolioGenerator.generatePortfolio();

System.out.println(portfolio.toMultilineString());
/*
    Portfolio(
    PortfolioItem(fund=Fund(id=1, type=POLISH_INVESTMENT_FUND, name=Polish Investment Fund), amount=PLN 2000.00, percentage=20%)
    PortfolioItem(fund=Fund(id=2, type=FOREIGN_INVESTMENT_FUND, name=Foreign Investment Fund), amount=PLN 7500.00, percentage=75%)
    PortfolioItem(fund=Fund(id=3, type=MONEY_MARKET_FUND, name=Money Market Fund), amount=PLN 500.00, percentage=5%)
    reminder=PLN 0.00)
*/
```

---

## Tests
Running tests:
```
mvn test
```

---

## Notes
This project uses [Lombok](https://projectlombok.org).

Additional configuration may be required to enable Lombok support in the IDE.