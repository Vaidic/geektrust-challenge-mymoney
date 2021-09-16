package in.vaidicjoshi.geektrust.backend.mymoney.service;

import in.vaidicjoshi.geektrust.backend.mymoney.dao.DataStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.zip.DataFormatException;

import static in.vaidicjoshi.geektrust.backend.mymoney.constant.MyMoneyConstants.CANNOT_REBALANCE;
import static in.vaidicjoshi.geektrust.backend.mymoney.enums.AssetClass.*;
import static java.time.Month.*;
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
  void testAllocateNull() {
    assertThrows(
        DataFormatException.class,
        () -> myMoneyService.allocate(null),
        "Expected Allocate method to throw Exception, but it didn't.");
  }

  @Test
  void testAllocateCorrectValues() throws DataFormatException {
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
  void testAllocateInCorrectValues() {
    assertThrows(
        DataFormatException.class,
        () -> myMoneyService.allocate(Arrays.asList(10d, 20d, 30d, 40d)),
        "Expected Allocate method to throw Exception, but it didn't.");
  }

  @Test
  void testAllocateAlreadyAllocated() throws DataFormatException {
    List<Double> initialAllocation = Arrays.asList(10d, 20d, 30d);
    myMoneyService.allocate(initialAllocation);

    assertThrows(
        IllegalStateException.class,
        () -> myMoneyService.allocate(initialAllocation),
        "Expected Allocate method to throw Exception, but it didn't.");
  }

  @Test
  void testSipWithNullValues() {
    assertThrows(
        DataFormatException.class,
        () -> myMoneyService.sip(null),
        "Expected Sip method to throw Exception, but it didn't.");
  }

  @Test
  void testSipWithInCorrectValues() {
    assertThrows(
        DataFormatException.class,
        () -> myMoneyService.sip(Arrays.asList(10d, 20d, 30d, 40d)),
        "Expected Sip method to throw Exception, but it didn't.");
  }

  @Test
  void testSipWithCorrectValues() throws DataFormatException {
    List<Double> sipAmounts = Arrays.asList(10d, 20d, 30d);
    myMoneyService.sip(sipAmounts);
    assertEquals(sipAmounts.size(), dataStub.initialSip.getFunds().size());
    assertEquals(
        sipAmounts.stream().mapToDouble(Double::doubleValue).sum(),
        dataStub.initialSip.getTotalInvestment());
  }

  @Test
  void testSipAlreadyAllocated() throws DataFormatException {
    List<Double> sipAmounts = Arrays.asList(10d, 20d, 30d);
    myMoneyService.sip(sipAmounts);
    assertThrows(
        IllegalStateException.class,
        () -> myMoneyService.sip(sipAmounts),
        "Expected Sip method to throw Exception, but it didn't.");
  }

  @Test
  void testChangeWithNullValues() {
    assertThrows(
        InputMismatchException.class,
        () -> myMoneyService.change(null, JANUARY),
        "Expected Change method to throw Exception, but it didn't.");
  }

  @Test
  void testChangeWithInCorrectValues() {
    assertThrows(
        DataFormatException.class,
        () -> myMoneyService.change(Arrays.asList(10d, 20d, 30d, 40d), JANUARY),
        "Expected Change method to throw Exception, but it didn't.");
  }

  @Test
  void testChangeWithCorrectValues() throws DataFormatException {
    List<Double> changeRate = Arrays.asList(10d, 20d, 30d);
    myMoneyService.change(changeRate, JANUARY);
    assertEquals(changeRate.size(), dataStub.monthlyMarketChangeRate.get(JANUARY).size());
  }

  @Test
  void testChangeAlreadyAllocatedForMonth() throws DataFormatException {
    List<Double> changeRate = Arrays.asList(10d, 20d, 30d);
    myMoneyService.change(changeRate, JANUARY);
    assertThrows(
        IllegalStateException.class,
        () -> myMoneyService.change(changeRate, JANUARY),
        "Expected Change method to throw Exception, but it didn't.");
  }

  @Test
  void testBalanceInSufficientData() throws DataFormatException {
    myMoneyService.allocate(Arrays.asList(6000d, 3000d, 1000d));
    myMoneyService.sip(Arrays.asList(2000d, 1000d, 500d));
    assertThrows(
        IllegalStateException.class,
        () -> myMoneyService.balance(JANUARY),
        "Expected Change method to throw Exception, but it didn't.");
  }

  @Test
  void testBalance() throws DataFormatException {
    initializePortfolio();
    assertEquals("10593 7897 2272", myMoneyService.balance(MARCH));
  }

  private void initializePortfolio() throws DataFormatException {
    myMoneyService.allocate(Arrays.asList(6000d, 3000d, 1000d));
    myMoneyService.sip(Arrays.asList(2000d, 1000d, 500d));
    myMoneyService.change(Arrays.asList(4d, 10d, 2d), JANUARY);
    myMoneyService.change(Arrays.asList(-10.00d, 40.00d, 0.00d), FEBRUARY);
    myMoneyService.change(Arrays.asList(12.50d, 12.50d, 12.50d), MARCH);
    myMoneyService.change(Arrays.asList(8.00d, -3.00d, 7.00d), APRIL);
    myMoneyService.change(Arrays.asList(13.00d, 21.00d, 10.50d), MAY);
    myMoneyService.change(Arrays.asList(10.00d, 8.00d, -5.00d), JUNE);
  }

  @Test
  void testReBalance() throws DataFormatException {
    initializePortfolio();
    assertEquals("23619 11809 3936", myMoneyService.reBalance());
  }

  @Test
  void testReBalanceWithInsufficientData() throws DataFormatException {
    myMoneyService.allocate(Arrays.asList(6000d, 3000d, 1000d));
    myMoneyService.sip(Arrays.asList(2000d, 1000d, 500d));
    myMoneyService.change(Arrays.asList(4d, 10d, 2d), JANUARY);
    String result = myMoneyService.reBalance();
    assertEquals(CANNOT_REBALANCE, result);
  }

  @Test
  void testGetSupportedAssetClass() {
    assertEquals(3, myMoneyService.getSupportedAssetClass());
  }
}
