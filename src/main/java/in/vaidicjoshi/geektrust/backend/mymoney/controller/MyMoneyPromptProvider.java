package in.vaidicjoshi.geektrust.backend.mymoney.controller;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

import static in.vaidicjoshi.geektrust.backend.mymoney.constant.MyMoneyConstants.SHELL_PROMPT;

/**
 * @author Vaidic Joshi
 * @date 13/09/21
 */
@Component
public class MyMoneyPromptProvider implements PromptProvider {
  @Override
  public AttributedString getPrompt() {
    return new AttributedString(
        SHELL_PROMPT, AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE));
  }
}
