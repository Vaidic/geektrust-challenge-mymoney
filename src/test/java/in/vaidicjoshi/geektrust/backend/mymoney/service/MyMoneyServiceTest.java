package in.vaidicjoshi.geektrust.backend.mymoney.service;

import in.vaidicjoshi.geektrust.backend.mymoney.dao.DataStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;
import java.util.Arrays;
import java.util.InputMismatchException;
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
    assertEquals(
        100, dataStub.desiredWeights.values().stream().mapToDouble(Double::doubleValue).sum());
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
  void sipWithNullValues() {
    assertThrows(
        DataFormatException.class,
        () -> myMoneyService.sip(null),
        "Expected Sip method to throw Exception, but it didn't.");
  }

  @Test
  void sipWithInCorrectValues() {
    assertThrows(
        DataFormatException.class,
        () -> myMoneyService.sip(Arrays.asList(10d, 20d, 30d, 40d)),
        "Expected Sip method to throw Exception, but it didn't.");
  }

  @Test
  void sipWithCorrectValues() throws DataFormatException {
    List<Double> sipAmounts = Arrays.asList(10d, 20d, 30d);
    myMoneyService.sip(sipAmounts);
    assertEquals(sipAmounts.size(), dataStub.initialSip.getFunds().size());
    assertEquals(
        sipAmounts.stream().mapToDouble(Double::doubleValue).sum(),
        dataStub.initialSip.getTotalInvestment());
  }

  @Test
  void sipAlreadyAllocated() throws DataFormatException {
    List<Double> sipAmounts = Arrays.asList(10d, 20d, 30d);
    myMoneyService.sip(sipAmounts);
    assertThrows(
        IllegalStateException.class,
        () -> myMoneyService.sip(sipAmounts),
        "Expected Sip method to throw Exception, but it didn't.");
  }

  @Test
  void changeWithNullValues() {
    assertThrows(
        InputMismatchException.class,
        () -> myMoneyService.change(null, Month.JANUARY),
        "Expected Change method to throw Exception, but it didn't.");
  }

  @Test
  void changeWithInCorrectValues() {
    assertThrows(
        DataFormatException.class,
        () -> myMoneyService.change(Arrays.asList(10d, 20d, 30d, 40d), Month.JANUARY),
        "Expected Change method to throw Exception, but it didn't.");
  }

  @Test
  void changeWithCorrectValues() throws DataFormatException {
    List<Double> changeRate = Arrays.asList(10d, 20d, 30d);
    myMoneyService.change(changeRate, Month.JANUARY);
    assertEquals(changeRate.size(), dataStub.monthlyMarketChangeRate.get(Month.JANUARY).size());
  }

  @Test
  void changeAlreadyAllocatedForMonth() throws DataFormatException {
    List<Double> changeRate = Arrays.asList(10d, 20d, 30d);
    myMoneyService.change(changeRate, Month.JANUARY);
    assertThrows(
        IllegalStateException.class,
        () -> myMoneyService.change(changeRate, Month.JANUARY),
        "Expected Change method to throw Exception, but it didn't.");
  }

  @Test
  void balance() {}

  @Test
  void reBalance() {}

  @Test
  void getSupportedAssetClass() {}
}
