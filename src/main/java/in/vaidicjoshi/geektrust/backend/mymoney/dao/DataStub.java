package in.vaidicjoshi.geektrust.backend.mymoney.dao;

import in.vaidicjoshi.geektrust.backend.mymoney.dto.MyMoneyFundPortfolio;
import in.vaidicjoshi.geektrust.backend.mymoney.enums.AssetClass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import java.time.Month;
import java.util.*;

/**
 * @author Vaidic Joshi
 * @date 14/09/21
 */
@Getter
@Setter
@Scope("singleton")
public class DataStub {
  public TreeMap<Month, MyMoneyFundPortfolio> fundsData = new TreeMap<>();
  public TreeMap<Month, Map<AssetClass, Double>> changes = new TreeMap<>();
  public MyMoneyFundPortfolio initialAllocation;
  public MyMoneyFundPortfolio initialSip;
  public Map<AssetClass, Integer> desiredWeights = new HashMap<>();
  public Set<AssetClass> defaultAssetOrderForIO = new LinkedHashSet<>();
}
