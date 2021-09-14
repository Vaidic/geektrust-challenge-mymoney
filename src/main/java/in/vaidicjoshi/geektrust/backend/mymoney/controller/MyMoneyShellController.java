package in.vaidicjoshi.geektrust.backend.mymoney.controller;

import in.vaidicjoshi.geektrust.backend.mymoney.service.MyMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.Month;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 * @author Vaidic Joshi
 * @date 13/09/21
 */
@ShellComponent
public class MyMoneyShellController {
  @Autowired private MyMoneyService myMoneyService;

  @ShellMethod("Receives the initial investment amounts for each fund viz equity, debt and gold.")
  public void allocate(List<Integer> allocations) throws DataFormatException {
    myMoneyService.allocate(allocations);
  }

  @ShellMethod(
      "Receives the investment amount on a monthly basis for each fund viz equity, debt and gold.")
  public void sip(List<Integer> sips) throws DataFormatException {
    myMoneyService.sip(sips);
  }

  @ShellMethod(
      "Receives the monthly rate of change (growth or loss) for each fund viz equity, debt and gold. A negative value represents a loss.")
  public void change(List<Double> rates, Month month) throws DataFormatException {
    myMoneyService.change(rates, month);
  }

  @ShellMethod("Prints the balance for each fund viz equity, debt and gold as on given Month.")
  public String balance(Month month) throws CloneNotSupportedException {
    return myMoneyService.balance(month);
  }

  @ShellMethod(
      "Print the rebalanced amount of each fund viz equity, debt and gold for the last 6 months. If at-least 6 months data is not available then print CANNOT_REBALANCE.")
  public String rebalance() throws CloneNotSupportedException {
    return myMoneyService.reBalance();
  }
}
