package in.vaidicjoshi.geektrust.backend.mymoney.service;

import in.vaidicjoshi.geektrust.backend.mymoney.dao.DataStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static in.vaidicjoshi.geektrust.backend.mymoney.enums.AssetClass.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vaidic Joshi
 * @date 16/09/21
 */
class MyMoneyDriverTest {
  @Mock private DataStub dataStub;
  @Mock private MyMoneyService myMoneyService;
  @Spy private MyMoneyDriver myMoneyDriver;

  @BeforeEach
  public void setUp() {
    dataStub = new DataStub();
    dataStub.defaultAssetOrderForIO.add(ASSET_EQUITY);
    dataStub.defaultAssetOrderForIO.add(ASSET_DEBT);
    dataStub.defaultAssetOrderForIO.add(ASSET_GOLD);
    myMoneyService = new MyMoneyServiceImpl(dataStub);
    myMoneyDriver = new MyMoneyDriver(myMoneyService);
  }

  @Test
  void testExecuteCommandsFromFileWithInvalidFile() {
    assertThrows(
        IOException.class,
        () -> myMoneyDriver.executeCommandsFromFile("inputFile"),
        "Expected Allocate method to throw Exception, but it didn't.");
  }

  @Test
  void testExecuteCommandsFromFileWithValidFile() throws IOException {
    String inputFile =
        Objects.requireNonNull(this.getClass().getClassLoader().getResource("testInputFile"))
            .getFile();
    String outputFile =
        Objects.requireNonNull(this.getClass().getClassLoader().getResource("testOutputFile"))
            .getFile();
    List<String> output = myMoneyDriver.executeCommandsFromFile(inputFile);
    try (Stream<String> lines = Files.lines(Paths.get(outputFile))) {
      String expectedResult = lines.map(String::trim).collect(Collectors.joining(";"));
      String result =
          output.stream()
              .filter(Objects::nonNull)
              .map(String::trim)
              .collect(Collectors.joining(";"));
      assertEquals(expectedResult, result);
    }
  }
}
