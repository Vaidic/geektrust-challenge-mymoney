package in.vaidicjoshi.geektrust.backend.mymoney.service;

import java.time.Month;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 * @author Vaidic Joshi
 * @date 14/09/21
 */
public interface MyMoneyService {
  void allocate(List<Integer> allocations) throws DataFormatException;

  void sip(List<Integer> sips) throws DataFormatException;

  void change(List<Double> rates, Month month) throws IllegalStateException;

  String balance(Month month);

  String reBalance();
}
