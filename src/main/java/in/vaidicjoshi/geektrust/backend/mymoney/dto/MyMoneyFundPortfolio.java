package in.vaidicjoshi.geektrust.backend.mymoney.dto;

import in.vaidicjoshi.geektrust.backend.mymoney.entity.FundEntity;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vaidic Joshi
 * @date 14/09/21
 */
@Getter
@Setter
@RequiredArgsConstructor
public class MyMoneyFundPortfolio implements Cloneable {
  @NonNull private final List<FundEntity> funds;

  @Override
  public MyMoneyFundPortfolio clone() {
    try {
      return (MyMoneyFundPortfolio) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError("Unable to copy the state of Funds");
    }
  }

  @Override
  public String toString() {
    return funds.stream()
        .map(entity -> entity.getAmount().toString())
        .collect(Collectors.joining(" "));
  }

  /**
   * This method sums the investment made across all asset class and returns total amount invested
   *
   * @return total investment across all asset class
   */
  public Integer getTotalInvestment() {
    return funds.stream().mapToInt(FundEntity::getAmount).sum();
  }
}
