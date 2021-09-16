# Solution for [Geektrust Backend Challenge - MyMoney](https://www.geektrust.in/coding-problem/backend/mymoney)

[![gradle-coverage](https://github.com/vaidic/geektrust-challenge-mymoney/actions/workflows/gradle-coverage.yml/badge.svg)](https://github.com/Vaidic/geektrust-challenge-mymoney/actions/workflows/gradle-coverage.yml)
[![codecov](https://codecov.io/gh/Vaidic/geektrust-challenge-mymoney/branch/main/graph/badge.svg?token=JG7TPKWSQF)](https://codecov.io/gh/Vaidic/geektrust-challenge-mymoney)
[![CodeFactor](https://www.codefactor.io/repository/github/vaidic/geektrust-challenge-mymoney/badge/main)](https://www.codefactor.io/repository/github/vaidic/geektrust-challenge-mymoney/overview/main) \
[![License](https://img.shields.io/github/license/Vaidic/geektrust-challenge-mymoney?style=plastic)](LICENSE)
![Java](https://img.shields.io/badge/OpenJDK-11-red) \
[![commits](https://badgen.net/github/commits/vaidic/geektrust-challenge-mymoney/main)](https://github.com/Vaidic/geektrust-challenge-mymoney/commits/main)
[![last-commit](https://badgen.net/github/last-commit/vaidic/geektrust-challenge-mymoney/main)](https://github.com/Vaidic/geektrust-challenge-mymoney/commits/main)
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
> [Complete problem statement](Geektrust.in_MyMoney.pdf)

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

## Running the Project

The project can be run in following modes:

1. **`Batch Mode`** - takes a file as input & produce output for execution of each and every command on the file
2. **`Interactive Shell Mode`** - takes a command as input & produce output corresponding to that command

### Running in **`Batch Mode`**

1. Download the [latest geektrust.jar]()
2. Execute the following command replacing _<path-to-inputfile>_ with the path for input file

```shell
java -jar geektrust.jar <path-to-inputfile>
```

3. The output will be printed on the console

### Running in  **`Interactive Shell Mode`**

1. Download the [latest geektrust.jar]()
2. Execute the following command

```shell
java -jar geektrust.jar SHELL
```

3. Execute one of the available command `ALLOCATE`, `SIP`, `CHANGE`, `BALANCE`, `REBALANCE`
    - **Note:** in shell node the command should be entered in _lower-case_
4. The output will be printed on the console

## Sample Input/Output

### Running in **`Batch Mode`**

1. Say we have a `inputfile` with following commands

```text
ALLOCATE 6000 3000 1000
SIP 2000 1000 500
CHANGE 4.00% 10.00% 2.00% JANUARY
CHANGE -10.00% 40.00% 0.00% FEBRUARY
CHANGE 12.50% 12.50% 12.50% MARCH
CHANGE 8.00% -3.00% 7.00% APRIL
CHANGE 13.00% 21.00% 10.50% MAY
CHANGE 10.00% 8.00% -5.00% JUNE
BALANCE MARCH
REBALANCE
```

2. Command to execute:

```shell
java -jar geektrust.jar inputfile
```

3. The output printed on the console

```shell
Sep 12, 2021 12:04:01 PM org.hibernate.validator.internal.util.Version <clinit>
INFO: HV000001: Hibernate Validator 6.1.7.Final
10593 7897 2272
23619 11809 3936
```

**Note:** The project uses [Spring Boot framework](https://spring.io/projects/spring-boot) and the first two lines of
logs are from the framework, and are safe to be ignored.

### Running in  **`Interactive Shell Mode`**

1. We will execute the following commands in sequence on the interactive shell

```text
allocate 6000,3000,1000
sip 2000,1000,500
change 4,10,2 JANUARY
change -10,40,0 FEBRUARY
change 12.50,12.50,12.50 MARCH
change 8,-3,7 APRIL
change 13,21,10.5 MAY
change 10,8,-5 JUNE
balance JANUARY
balance MARCH
rebalance
```

2. Open the Interactive Shell using:

```shell
java -jar geektrust.jar SHELL
```

3. The Interactive Shell opens a `my-money-cli:> ` prompt
4. Execute the commands & get the output

```shell
my-money-cli:> allocate 6000,3000,1000
my-money-cli:> sip 2000,1000,500
my-money-cli:> change 4,10,2 JANUARY
my-money-cli:> change -10,40,0 FEBRUARY
my-money-cli:> change 12.50,12.50,12.50 MARCH
my-money-cli:> change 8,-3,7 APRIL
my-money-cli:> change 13,21,10.5 MAY
my-money-cli:> change 10,8,-5 JUNE
my-money-cli:> balance JANUARY
6240 3300 1020
my-money-cli:> balance MARCH
10593 7897 2272
my-money-cli:> balance MARCH
10593 7897 2272
my-money-cli:> rebalance
23619 11809 3936
my-money-cli:> exit
```

5. Use `exit` to exit the shell

## Key Points regarding Design

- [x] Calculations done lazily - not unless the balance is asked or re-balancing is required,
- [x] Calculated values are cached to avoid re-calculations

## Points to Ponder

1. Is using Spring Boot required?
    - No, This could be implemented using POJOs as well.
    - Spring Boot is used to make the solution mimic the Enterprise Java Solution preferred by companies over POJO
      solutions.
    - Spring Boot comes with additional features like inherent dependency-injection that makes code more maintainable.

2. The Exceptions thrown by service class are being propagated but not handled.
    - This can be improved; it is not good user experience to display Exceptions, rather user-friendly error messages
      should be used to handle these exceptions.

3. Using a lazy-calculation scheme - i.e. we compute the balance only when user checks for balance or re-balacing is
   required.
    - This can be replaced with eager calculation to improve response time for user.
    - Can also be done as a background batch job, without impacting performance.

