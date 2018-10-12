package de.digitalcollections.blueprints.application.springboot.service.impl;

import de.digitalcollections.blueprints.application.springboot.service.HelloMessageService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HelloMessageServiceImpl implements HelloMessageService {

  public String getMessage(List<String> names) {
    StringBuilder message = new StringBuilder("Hello ");
    message.append(String.join(", ", names));
    return message.toString();
  }
}
