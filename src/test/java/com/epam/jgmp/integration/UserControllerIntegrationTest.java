package com.epam.jgmp.integration;

import com.epam.jgmp.config.TbsApplicationConfig;
import com.epam.jgmp.facade.BookingFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TbsApplicationConfig.class)
@WebAppConfiguration
@WebMvcTest()
public class UserControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private BookingFacade bookingFacade;

  private ObjectMapper objectMapper;
  private ObjectNode objectNode;

  @Before
  public void setUp() throws ParseException {

    objectMapper = new ObjectMapper();
    objectNode = objectMapper.createObjectNode();
  }

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

    objectNode.put("name", "TestUser");
    objectNode.put("email", "test@gmail.com");

    this.mockMvc
        .perform(
            post("/user/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectNode.toString()))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  public void updateUser() throws Exception {

    objectNode.put("id", 2L);
    objectNode.put("name", "TestUser2");
    objectNode.put("email", "test2@gmail.com");

    this.mockMvc
        .perform(
            put("/user/update/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectNode.toString()))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  public void deleteUser() throws Exception {
    this.mockMvc.perform(delete("/user/delete/{id}", 3L)).andExpect(status().isOk()).andReturn();
  }
}
