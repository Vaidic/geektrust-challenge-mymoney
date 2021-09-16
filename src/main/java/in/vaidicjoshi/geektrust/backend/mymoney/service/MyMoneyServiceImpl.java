package in.vaidicjoshi.geektrust.backend.mymoney.service;

import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import in.vaidicjoshi.geektrust.backend.mymoney.dao.DataStub;
import in.vaidicjoshi.geektrust.backend.mymoney.dto.MyMoneyFundPortfolio;
import in.vaidicjoshi.geektrust.backend.mymoney.entity.FundEntity;
import in.vaidicjoshi.geektrust.backend.mymoney.enums.AssetClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.DataFormatException;

import static in.vaidicjoshi.geektrust.backend.mymoney.constant.MyMoneyConstants.CANNOT_REBALANCE;

/**
 * @author Vaidic Joshi
 * @date 14/09/21
 */
@Service
@Log4j2
public class MyMoneyServiceImpl implements MyMoneyService {
  private final DataStub dataStub;

  public MyMoneyServiceImpl(DataStub dataStub) {
    this.dataStub = dataStub;
  }

  /**
   * This method generates a portfolio with the initial allocation. Also, the desired weights are
   * calculated and cached
   *
   * @param allocations
   * @throws DataFormatException
   */
  @Override
  public void allocate(List<Double> allocations) throws DataFormatException {
    if (Objects.nonNull(dataStub.initialAllocation)) {
      throw new IllegalStateException("The funds are already Allocated Once");
    }
    dataStub.initialAllocation = createMyMoneyFundsWithDefaultOrdering(allocations);
    dataStub.desiredWeights = calculateDesiredWeight();
    log.debug(
        "Portfolio initialized with initial allocation of {} and desired weights of {}",
        dataStub.initialAllocation,
        dataStub.desiredWeights);
  }

  private Map<AssetClass, Double> calculateDesiredWeight() {
    if (Objects.isNull(dataStub.initialAllocation)) {
      throw new IllegalStateException("The funds are not yet Allocated");
    }
    return dataStub.initialAllocation.getFunds().stream()
        .collect(
            Collectors.toMap(
                FundEntity::getAssetClass,
                e -> e.getAmount() * 100 / dataStub.initialAllocation.getTotalInvestment()));
  }

  /**
   * This method takes allocations across all asset classes and creates a portfolio
   *
   * @param allocations
   * @return
   * @throws DataFormatException
   */
  private MyMoneyFundPortfolio createMyMoneyFundsWithDefaultOrdering(List<Double> allocations)
      throws DataFormatException {
    return createMyMoneyFunds(dataStub.defaultAssetOrderForIO, allocations);
  }

  /**
   * This method takes the asset classes and allocations across those classes and creates a
   * portfolio
   *
   * @param assetOrderForIO
   * @param allocations
   * @return
   * @throws DataFormatException
   */
  private MyMoneyFundPortfolio createMyMoneyFunds(
      Set<AssetClass> assetOrderForIO, List<Double> allocations) throws DataFormatException {
    validateInputs(assetOrderForIO, allocations);
    List<FundEntity> fundEntityList =
        Streams.zip(assetOrderForIO.stream(), allocations.stream(), FundEntity::new)
            .collect(Collectors.toList());
    return new MyMoneyFundPortfolio(fundEntityList);
  }

  /**
   * Validates the allocations are for all supported assets class
   *
   * @param assetOrderForIO
   * @param allocations
   * @throws DataFormatException
   */
  private void validateInputs(Set<AssetClass> assetOrderForIO, List<Double> allocations)
      throws DataFormatException {
    if (Objects.isNull(allocations) || allocations.size() != assetOrderForIO.size()) {
      throw new DataFormatException("The input is not in the desired format");
    }
  }

  /**
   * Registers monthly sip amount for each asset class. Once registered sip can not be changed. Sip
   * starts from month of Feb.
   *
   * @param sips
   * @throws DataFormatException
   */
  @Override
  public void sip(List<Double> sips) throws DataFormatException {
    // Since sip always starts from Feb, we disallow entering multiple sips
    if (Objects.nonNull(dataStub.initialSip)) {
      throw new IllegalStateException("The SIP is already registered once");
    }
    dataStub.initialSip = createMyMoneyFundsWithDefaultOrdering(sips);
    log.debug("Portfolio initialized with a monthly sip of {} ", dataStub.initialSip);
  }

  /**
   * Registers the rate of change for each asset class for a particular month. The rate change for a
   * month, once entered cannot be changed.
   *
   * @param rates
   * @param month
   */
  @Override
  public void change(List<Double> rates, Month month) throws IllegalStateException {
    if (Objects.nonNull(dataStub.monthlyMarketChangeRate.getOrDefault(month, null))) {
      throw new IllegalStateException(
          "The Rate of Change for month " + month.name() + " is already registered");
    }
    Map<AssetClass, Double> change =
        Streams.zip(dataStub.defaultAssetOrderForIO.stream(), rates.stream(), Maps::immutableEntry)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    dataStub.monthlyMarketChangeRate.put(month, change);
  }

  /**
   * Calculates and caches the total available balance upto the entered month.
   *
   * @param month
   * @return
   */
  @Override
  public String balance(Month month) {
    updateBalance();
    MyMoneyFundPortfolio fund =
        Optional.ofNullable(dataStub.monthlyBalance.get(month))
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "The balance is requested for the month  "
                            + month.name()
                            + "with no data"));
    return fund.toString();
  }

  /** Calculates the total available balance upto the entered month. Calculates the */
  private void updateBalance() {
    Map.Entry<Month, MyMoneyFundPortfolio> lastCalculatedBalance =
        dataStub.monthlyBalance.lastEntry();
    Map.Entry<Month, Map<AssetClass, Double>> lastKnownChange =
        dataStub.monthlyMarketChangeRate.lastEntry();
    if (Objects.isNull(lastKnownChange)) {
      throw new IllegalStateException("Rate of Change is not defined");
    }
    if (Objects.isNull(lastCalculatedBalance)) {
      log.debug("Calculating balance for month of {}", lastCalculatedBalance);
      MyMoneyFundPortfolio myMoneyFund =
          calculateBalance(
              dataStub.initialAllocation,
              null,
              dataStub.monthlyMarketChangeRate.get(Month.JANUARY));
      dataStub.monthlyBalance.put(Month.JANUARY, myMoneyFund);
      lastCalculatedBalance = dataStub.monthlyBalance.lastEntry();
    }
    if (lastCalculatedBalance.getKey() != lastKnownChange.getKey()) {
      Month startMonth = lastCalculatedBalance.getKey();
      Month endMonth = lastKnownChange.getKey();
      for (int index = startMonth.getValue(); index < endMonth.getValue(); index++) {
        Month lastUpdatedMonth = Month.of(index);
        Month currentCalculationMonth = Month.of(index + 1);
        log.debug("Calculating balance for month of {}", currentCalculationMonth);
        MyMoneyFundPortfolio carryOverBalance =
            dataStub.monthlyBalance.get(lastUpdatedMonth).clone();
        Map<AssetClass, Double> changeRate =
            dataStub.monthlyMarketChangeRate.get(currentCalculationMonth);
        MyMoneyFundPortfolio availableBalance =
            calculateBalance(carryOverBalance, dataStub.initialSip, changeRate);
        if (shouldReBalance(currentCalculationMonth)) {
          availableBalance = doReBalance(availableBalance);
        }
        dataStub.monthlyBalance.putIfAbsent(currentCalculationMonth, availableBalance);
      }
    }
  }

  /**
   * Calculates the total balance after applying the sip & market change
   *
   * @param carryOverBalance
   * @param monthlySip
   * @param changeRate
   * @return
   */
  private MyMoneyFundPortfolio calculateBalance(
      MyMoneyFundPortfolio carryOverBalance,
      MyMoneyFundPortfolio monthlySip,
      Map<AssetClass, Double> changeRate) {
    log.debug(
        "Updating current balance of {}, with a sip of {} and market change rate of {}",
        carryOverBalance,
        monthlySip,
        changeRate);
    MyMoneyFundPortfolio balAfterSip = applySipInvestment(carryOverBalance, monthlySip);
    return applyMarketChange(balAfterSip, changeRate);
  }

  /**
   * Applies market change to the available balance.
   *
   * @param carryOverBalance
   * @param changeRate
   * @return
   */
  private MyMoneyFundPortfolio applyMarketChange(
      MyMoneyFundPortfolio carryOverBalance, Map<AssetClass, Double> changeRate) {
    List<FundEntity> funds = carryOverBalance.getFunds();
    funds.forEach(
        entity -> {
          double rate = changeRate.get(entity.getAssetClass());
          double updatedAmount = entity.getAmount() * (1 + rate / 100);
          entity.setAmount(updatedAmount);
        });
    return carryOverBalance;
  }

  /**
   * Adds sip amount for the current month to the previous balance.
   *
   * @param carryOverBalance
   * @param initialSip
   * @return
   */
  private MyMoneyFundPortfolio applySipInvestment(
      MyMoneyFundPortfolio carryOverBalance, MyMoneyFundPortfolio initialSip) {
    List<FundEntity> funds = carryOverBalance.getFunds();
    if (Objects.nonNull(initialSip)) {
      IntStream.range(0, funds.size())
          .forEach(
              index -> {
                FundEntity fundEntity = funds.get(index);
                double sipAmount = initialSip.getFunds().get(index).getAmount();
                fundEntity.setAmount(fundEntity.getAmount() + sipAmount);
              });
    }
    return carryOverBalance;
  }

  /**
   * Returns balance after last re-balance operation
   *
   * @return
   */
  @Override
  public String reBalance() {
    updateBalance();
    Month lastUpdatedMonth = dataStub.monthlyBalance.lastEntry().getKey();
    Month lastRebalancedMonth = getLastReBalancedMonth(lastUpdatedMonth);
    MyMoneyFundPortfolio balance = dataStub.monthlyBalance.getOrDefault(lastRebalancedMonth, null);
    return Objects.nonNull(balance) ? balance.toString() : CANNOT_REBALANCE;
  }

  /**
   * Return the month when last re-balancing happened.
   *
   * @param month
   * @return
   */
  private Month getLastReBalancedMonth(Month month) {
    return month == Month.DECEMBER ? month : Month.JUNE;
  }

  /**
   * Check if the portfolio should be rebalanced.
   *
   * @return
   */
  private boolean shouldReBalance(Month month) {
    // Assumption#3: The re-balancing happens on 6 and 12 months.
    return month.equals(Month.JUNE) || month.equals(Month.DECEMBER);
  }

  /**
   * Re-balances the available balance to match the desired asset class weight.
   *
   * @param currentFunds
   * @return
   */
  private MyMoneyFundPortfolio doReBalance(MyMoneyFundPortfolio currentFunds) {
    List<FundEntity> funds = currentFunds.getFunds();
    double totalInvestment = currentFunds.getTotalInvestment();
    funds.forEach(
        entity -> {
          double desiredWeight = dataStub.desiredWeights.get(entity.getAssetClass());
          entity.setAmount(totalInvestment * desiredWeight / 100);
        });
    log.debug(
        "Re-balanced the current total balance of {} to desired weights of {} to {}",
        currentFunds.getTotalInvestment(),
        dataStub.desiredWeights,
        currentFunds);
    return currentFunds;
  }
}
