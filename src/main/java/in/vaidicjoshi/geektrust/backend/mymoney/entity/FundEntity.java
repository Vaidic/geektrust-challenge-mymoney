package in.vaidicjoshi.geektrust.backend.mymoney.entity;

import in.vaidicjoshi.geektrust.backend.mymoney.enums.AssetClass;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Vaidic Joshi
 * @date 14/09/21
 */
@Getter
@Setter
@RequiredArgsConstructor
public class FundEntity {
  @NonNull private AssetClass assetClass;
  @NonNull private Double amount;
}
