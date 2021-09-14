package in.vaidicjoshi.geektrust.backend.mymoney.service;

import java.time.Month;
import java.util.List;

/**
 * @author Vaidic Joshi
 * @date 14/09/21
 */
public interface MyMoneyService {
  void allocate(List<Integer> allocations);

  void sip(List<Integer> sips);

  void change(List<Double> rates, Month month);

  String balance(Month month);

  String reBalance();
}
