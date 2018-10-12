package de.digitalcollections.blueprints.application;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HelloMessageService {

  public String getMessage(List<String> names) {
    StringBuilder message = new StringBuilder("Hello ");
    message.append(String.join(", ", names));
    return message.toString();
  }
}
