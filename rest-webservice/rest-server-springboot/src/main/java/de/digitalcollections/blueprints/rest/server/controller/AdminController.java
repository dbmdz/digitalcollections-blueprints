package de.digitalcollections.blueprints.rest.server.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "The admin controller", name = "Admin controller")
@RestController
@RequestMapping("/admin")
public class AdminController {

  private final SimpleDateFormat sf = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");

  @ApiMethod(description = "return server time")
  @RequestMapping(value = "/serverTime", method = RequestMethod.GET)
  public String serverTime() {
    return sf.format(new Date());
  }
}
