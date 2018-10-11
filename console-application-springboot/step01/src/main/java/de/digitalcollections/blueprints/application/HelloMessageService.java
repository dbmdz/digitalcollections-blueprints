package de.digitalcollections.blueprints.application;

import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

@Service
public class HelloMessageService {

  public String getMessage(ApplicationArguments args) {
    StringBuilder message = new StringBuilder("Hello ");

    final List<String> nonOptionArgs = args.getNonOptionArgs();
    if (nonOptionArgs.isEmpty()) {
      message.append("unknown");
    } else {
      message.append(nonOptionArgs.get(0));
    }

    return message.toString();
  }
}
