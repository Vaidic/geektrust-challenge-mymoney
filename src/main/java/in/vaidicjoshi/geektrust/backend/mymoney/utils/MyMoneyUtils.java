package in.vaidicjoshi.geektrust.backend.mymoney.utils;

import java.util.List;
import java.util.Objects;

/**
 * @author Vaidic Joshi
 * @date 16/09/21
 */
public class MyMoneyUtils {
  public static int floorToNearestInt(float floatValue) {
    return (int) Math.floor(floatValue);
  }

  public static void display(List<String> outputs) {
    outputs.stream().filter(Objects::nonNull).forEach(System.out::println);
  }
}
