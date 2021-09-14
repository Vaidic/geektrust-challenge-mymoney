package in.vaidicjoshi.geektrust.backend.mymoney.service;

import com.google.common.collect.Streams;
import in.vaidicjoshi.geektrust.backend.mymoney.dao.DataStub;
import in.vaidicjoshi.geektrust.backend.mymoney.dto.MyMoneyFundPortfolio;
import in.vaidicjoshi.geektrust.backend.mymoney.entity.FundEntity;
import in.vaidicjoshi.geektrust.backend.mymoney.enums.AssetClass;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

/**
 * @author Vaidic Joshi
 * @date 14/09/21
 */
@Service
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
  public void allocate(List<Integer> allocations) throws DataFormatException {
    if (Objects.nonNull(dataStub.initialAllocation)) {
      throw new IllegalStateException("The funds are already Allocated Once");
    }
    dataStub.initialAllocation = createMyMoneyFundsWithDefaultOrdering(allocations);
    dataStub.desiredWeights = calculateDesiredWeight();
  }

  private Map<AssetClass, Integer> calculateDesiredWeight() {
    if (Objects.isNull(dataStub.initialAllocation)) {
      throw new IllegalStateException("The funds are not yet Allocated");
    }
    return dataStub.initialAllocation.getFunds().stream()
        .collect(
            Collectors.toMap(
                FundEntity::getAssetClass,
                e -> e.getAmount() / dataStub.initialAllocation.getTotalInvestment()));
  }

  /**
   * This method takes allocations across all asset classes and creates a portfolio
   *
   * @param allocations
   * @return
   * @throws DataFormatException
   */
  private MyMoneyFundPortfolio createMyMoneyFundsWithDefaultOrdering(List<Integer> allocations)
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
      Set<AssetClass> assetOrderForIO, List<Integer> allocations) throws DataFormatException {
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
  private void validateInputs(Set<AssetClass> assetOrderForIO, List<Integer> allocations)
      throws DataFormatException {
    if (Objects.isNull(allocations) || allocations.size() != assetOrderForIO.size()) {
      throw new DataFormatException("The input is not in the desired format");
    }
  }

  @Override
  public void sip(List<Integer> sips) {}

  @Override
  public void change(List<Double> rates, Month month) {}

  @Override
  public String balance(Month month) {
    return null;
  }

  @Override
  public String reBalance() {
    return null;
  }
}
