package in.vaidicjoshi.geektrust.backend.mymoney;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.InputMismatchException;

/**
 * @author Vaidic Joshi
 * @date 12/09/21
 */
@SpringBootApplication
// @Order(-1) //todo: enable
@Log4j2
public class MyMoneyApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(MyMoneyApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    if (args.length < 1) {
      log.error("No input arguments were supplied");
      throw new InputMismatchException(
          "Please specify input file, or to run in CLI mode provide SHELL as argument");
    } else if (args.length != 1) {
      log.error("No input arguments were supplied");
      throw new InputMismatchException(
          "Please specify only the input file, or to run in CLI mode provide SHELL as argument");
    }
    String input = args[0];
    if ("shell".equalsIgnoreCase(input)) {
      log.info("Switching to SHELL Mode");
      return;
    }
    log.info("Switching to BATCH-PROCESSING Mode");
    // todo: process file - executeCommandsFromFile(input);
  }
}
