package de.digitalcollections.blueprints.springmvc.frontend.webgui;

import de.digitalcollections.blueprints.springmvc.config.SpringConfigWeb;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringConfigWeb.class})
public class AppTests {

  private MockMvc mockMvc;

  @Autowired
  protected WebApplicationContext wac;

  @Before
  public void setup() {
    this.mockMvc = webAppContextSetup(this.wac).build();
  }

  @Test
  public void simple() throws Exception {
    mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("main"));
  }
}
