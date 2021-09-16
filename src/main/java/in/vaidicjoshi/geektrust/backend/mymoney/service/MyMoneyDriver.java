package in.vaidicjoshi.geektrust.backend.mymoney.service;

import com.google.common.base.Strings;
import in.vaidicjoshi.geektrust.backend.mymoney.enums.SupportedCommand;
import in.vaidicjoshi.geektrust.backend.mymoney.utils.MyMoneyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Month;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;

/**
 * @author Vaidic Joshi
 * @date 16/09/21
 */
@Service
public class MyMoneyDriver {
  @Autowired private MyMoneyService myMoneyService;

  public void executeCommandsFromFile(String filename) {
    try (Stream<String> lines = Files.lines(Paths.get(filename))) {
      List<String> outputs =
          lines
              .filter(l -> !Strings.isNullOrEmpty(l))
              .map(this::processLineAsCommand)
              .collect(Collectors.toList());
      MyMoneyUtils.display(outputs);

    } catch (IOException e) {
      System.out.println("Invalid File. Please check the path & name for input file provided.");
    }
  }

  public String processLineAsCommand(String line) {
    String output = null;
    int supportedAssetClass = myMoneyService.getSupportedAssetClass();
    String[] commandAndInputs = line.split(" ");
    try {
      SupportedCommand command = SupportedCommand.valueOf(commandAndInputs[0]);
      switch (command) {
        case ALLOCATE:
          validateInputSize(commandAndInputs, supportedAssetClass);
          List<Double> allocations = getDoubles(1, supportedAssetClass, commandAndInputs);
          myMoneyService.allocate(allocations);
          break;
        case SIP:
          validateInputSize(commandAndInputs, supportedAssetClass);
          List<Double> sips = getDoubles(1, supportedAssetClass, commandAndInputs);
          myMoneyService.sip(sips);
          break;
        case CHANGE:
          validateInputSize(commandAndInputs, supportedAssetClass + 1);
          List<Double> rates =
              Arrays.stream(commandAndInputs)
                  .skip(1)
                  .limit(supportedAssetClass)
                  .map(str -> Double.parseDouble(str.replace("%", "")))
                  .collect(Collectors.toList());
          Month month = Month.valueOf(commandAndInputs[supportedAssetClass + 1]);
          myMoneyService.change(rates, month);
          break;
        case BALANCE:
          validateInputSize(commandAndInputs, 1);
          month = Month.valueOf(commandAndInputs[1]);
          output = myMoneyService.balance(month);
          break;
        case REBALANCE:
          output = myMoneyService.reBalance();
          break;
        default:
          throw new DataFormatException("Invalid Command " + command + " supplied");
      }
    } catch (Exception e) {
      System.out.println(
          "Error Occurred while processing " + String.join(" ", commandAndInputs) + e.getMessage());
    }
    return output;
  }

  private List<Double> getDoubles(int skip, int limit, String[] commandAndInputs) {
    return Arrays.stream(commandAndInputs)
        .skip(skip)
        .limit(limit)
        .map(Double::parseDouble)
        .collect(Collectors.toList());
  }

  private void validateInputSize(String[] commandAndInputs, int size) {
    if (commandAndInputs.length != size + 1) {
      throw new InputMismatchException(
          "Please check the command " + String.join(" ", commandAndInputs));
    }
  }
}
