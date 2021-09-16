package in.vaidicjoshi.geektrust.backend.mymoney.service;

import in.vaidicjoshi.geektrust.backend.mymoney.dao.DataStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.zip.DataFormatException;

import static in.vaidicjoshi.geektrust.backend.mymoney.enums.AssetClass.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vaidic Joshi
 * @date 16/09/21
 */
@ExtendWith(MockitoExtension.class)
class MyMoneyServiceTest {
  @Spy private DataStub dataStub;
  @InjectMocks private MyMoneyServiceImpl myMoneyService;

  @BeforeEach
  public void setUp() {
    dataStub.defaultAssetOrderForIO.add(ASSET_EQUITY);
    dataStub.defaultAssetOrderForIO.add(ASSET_DEBT);
    dataStub.defaultAssetOrderForIO.add(ASSET_GOLD);
    myMoneyService = new MyMoneyServiceImpl(dataStub);
  }

  @Test
  void allocateNull() {
    assertThrows(
        DataFormatException.class,
        () -> myMoneyService.allocate(null),
        "Expected Allocate method to throw Exception, but it didn't.");
  }

  @Test
  void allocateCorrectValues() throws DataFormatException {
    List<Double> initialAllocation = Arrays.asList(10d, 20d, 30d);
    myMoneyService.allocate(initialAllocation);
    assertEquals(initialAllocation.size(), dataStub.initialAllocation.getFunds().size());
    assertEquals(
        initialAllocation.stream().mapToDouble(Double::doubleValue).sum(),
        dataStub.initialAllocation.getTotalInvestment());
  }

  @Test
  void allocateInCorrectValues() {
    assertThrows(
        DataFormatException.class,
        () -> myMoneyService.allocate(Arrays.asList(10d, 20d, 30d, 40d)),
        "Expected Allocate method to throw Exception, but it didn't.");
  }

  @Test
  void allocateAlreadyAllocated() throws DataFormatException {
    List<Double> initialAllocation = Arrays.asList(10d, 20d, 30d);
    myMoneyService.allocate(initialAllocation);

    assertThrows(
        IllegalStateException.class,
        () -> myMoneyService.allocate(initialAllocation),
        "Expected Allocate method to throw Exception, but it didn't.");
  }

  @Test
  void sip() {}

  @Test
  void change() {}

  @Test
  void balance() {}

  @Test
  void reBalance() {}

  @Test
  void getSupportedAssetClass() {}
}
