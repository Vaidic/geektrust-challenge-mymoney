package in.vaidicjoshi.geektrust.backend.mymoney.entity;

import in.vaidicjoshi.geektrust.backend.mymoney.enums.AssetClass;
import lombok.Data;
import lombok.NonNull;

/**
 * @author Vaidic Joshi
 * @date 14/09/21
 */
@Data
public class FundEntity {
  @NonNull private AssetClass assetClass;
  @NonNull private Double amount;
}
