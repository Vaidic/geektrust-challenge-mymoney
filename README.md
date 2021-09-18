# Solution for [Geektrust Backend Challenge - MyMoney](https://www.geektrust.in/coding-problem/backend/mymoney)

[![gradle-coverage](https://github.com/vaidic/geektrust-challenge-mymoney/actions/workflows/gradle-coverage.yml/badge.svg)](https://github.com/Vaidic/geektrust-challenge-mymoney/actions/workflows/gradle-coverage.yml)
[![codecov](https://codecov.io/gh/Vaidic/geektrust-challenge-mymoney/branch/pojo-solution/graph/badge.svg?token=JG7TPKWSQF)](https://codecov.io/gh/Vaidic/geektrust-challenge-mymoney)
[![CodeFactor](https://www.codefactor.io/repository/github/vaidic/geektrust-challenge-mymoney/badge/pojo-solution)](https://www.codefactor.io/repository/github/vaidic/geektrust-challenge-mymoney/overview/pojo-solution)
[![DeepSource](https://deepsource.io/gh/Vaidic/geektrust-challenge-mymoney.svg/?label=active+issues&show_trend=true&token=Dfz5PcRjXpGyTVzeQEAaW5DI)](https://deepsource.io/gh/Vaidic/geektrust-challenge-mymoney/?ref=repository-badge) \
[![License](https://img.shields.io/github/license/Vaidic/geektrust-challenge-mymoney?style=plastic)](LICENSE)
![Java](https://img.shields.io/badge/OpenJDK-11-red) \
[![commits](https://badgen.net/github/commits/vaidic/geektrust-challenge-mymoney/pojo-solution)](https://github.com/Vaidic/geektrust-challenge-mymoney/commits/pojo-solution)
[![last-commit](https://badgen.net/github/last-commit/vaidic/geektrust-challenge-mymoney/pojo-solution)](https://github.com/Vaidic/geektrust-challenge-mymoney/commits/pojo-solution)
[![releases](https://badgen.net/github/release/Vaidic/geektrust-challenge-mymoney)](https://github.com/Vaidic/geektrust-challenge-mymoney/releases)

## Problem Statement

MyMoney platform lets investors consolidated portfolio value across **equity**, **debt**, and **gold**. We need to
ensure that the desired allocation percentages are equal to the actual percentages invested. The desired allocation
percentage should be derived from the initial allocation made.

**Your program should take as input:**

1. The money allocated in equity, debt and gold funds.
2. Monthly SIP payments.
3. Monthly change rate (loss or growth) for each type of fund.

**The output should be:**

1. Balanced amount of each fund for a certain month.
2. Rebalanced amount of each month if applicable.

The supported commands are: \
`ALLOCATE`, `SIP`, `CHANGE`, `BALANCE`, `REBALANCE`

## Assumptions

### From Problem Statement

1. The money allocated in equity, debt and gold funds.
2. Monthly SIP payments.
3. Monthly change rate (loss or growth) for each type of fund.

### Additional Assumptions Made

1. Only 3 allocation class are currently available.
2. The default input order for asset class is - EQUITY, DEBT, GOLD.
3. All the inputs must be explicitly provided, no default value as 0 is used.
4. SIP, Change percentages can be declared only once for a month.
5. The system only works for 1-year (Jan-Dec) as there is no way to distinguish between years from input.

## Running the project

1. Download the [latest geektrust.jar](https://github.com/Vaidic/geektrust-challenge-mymoney/releases)
2. Execute the following command replacing _<path-to-inputfile>_ with the path for input file

```shell
java -jar geektrust.jar <path-to-inputfile>
```

**OR**

1. Download the source code.
2. Build the project using -

```shell
./gradlew clean build
```

3. Use the generated `jar` from `build/lib` folder and execute -

```shell
java -jar geektrust.jar <path-to-inputfile>
```
