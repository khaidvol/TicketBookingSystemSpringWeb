package com.epam.jgmp.integration;

import com.epam.jgmp.config.TbsApplicationConfig;
import com.epam.jgmp.facade.BookingFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasToString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TbsApplicationConfig.class)
@WebAppConfiguration
@WebMvcTest()
public class UserControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private BookingFacade bookingFacade;

  @Test
  public void getUserById() throws Exception {
    long id = 1L;
    this.mockMvc
        .perform(get("/user/id?id={id}", id))
        .andExpect(status().isOk())
        .andExpect(view().name("userTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", bookingFacade.getUserById(id)));
  }

  @Test
  public void getUserByEmail() throws Exception {
    String email = "jack@gmail.com";
    this.mockMvc
        .perform(get("/user/email?email={email}", email))
        .andExpect(status().isOk())
        .andExpect(view().name("userTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", bookingFacade.getUserByEmail(email)));
  }

  @Test
  public void getUsersByName() throws Exception {
    String name = "Jack";
    this.mockMvc
        .perform(get("/user/name?name={name}&pageSize=1&pageNum=1", name))
        .andExpect(status().isOk())
        .andExpect(view().name("userTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", bookingFacade.getUsersByName(name, 1, 1)));
  }

  @Test
  public void createUser() throws Exception {
    String name = "TestUser";
    String email = "test@gmail.com";
    this.mockMvc
        .perform(post("/user/new?name={name}&email={email}", name, email))
        .andExpect(status().isOk())
        .andExpect(view().name("userTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(
            model()
                .attribute(
                    "result",
                    "User created: ".concat(bookingFacade.getUserByEmail(email).toString())));
  }

  @Test
  public void updateUser() throws Exception {
    long id = 2L;
    String name = "TestUser2";
    String email = "test2@gmail.com";

    this.mockMvc
        .perform(put("/user/update?id={id}&name={name}&email={email}", id, name, email))
        .andExpect(status().isOk())
        .andExpect(view().name("userTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(
            model()
                .attribute(
                    "result",
                    "User updated: ".concat(bookingFacade.getUserByEmail(email).toString())));
  }

  @Test
  public void deleteUser() throws Exception {
    Long id = 3L;
    this.mockMvc
        .perform(delete(String.format("/user/delete?id=%s", id)))
        .andExpect(status().isOk())
        .andExpect(view().name("userTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(
            model().attribute("result", hasToString(String.format("User #%s deleted: true", id))));
  }
}
