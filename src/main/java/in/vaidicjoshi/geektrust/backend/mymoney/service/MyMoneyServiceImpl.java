package in.vaidicjoshi.geektrust.backend.mymoney.service;

import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;

/**
 * @author Vaidic Joshi
 * @date 14/09/21
 */
@Service
public class MyMoneyServiceImpl implements MyMoneyService {
  @Override
  public void allocate(List<Integer> allocations) {}

  @Override
  public void sip(List<Integer> sips) {}

  @Override
  public void change(List<Double> rates, Month month) {}

  @Override
  public String balance(Month month) {
    return null;
  }

  @Override
  public String reBalance() {
    return null;
  }
}
