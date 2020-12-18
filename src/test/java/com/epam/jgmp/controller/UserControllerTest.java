package com.epam.jgmp.controller;

import com.epam.jgmp.config.TbsApplicationConfig;
import com.epam.jgmp.dao.model.User;
import com.epam.jgmp.facade.BookingFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TbsApplicationConfig.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private BookingFacade bookingFacade;

  private User user;
  private List<User> users;

  private ObjectMapper objectMapper;
  private ObjectNode objectNode;

  @Before
  public void setUp() {

    objectMapper = new ObjectMapper();
    objectNode = objectMapper.createObjectNode();

    long id = 1L;
    String name = "TestUser";
    String email = "test@gmail.com";

    user = new User(name, email);
    user.setId(id);
    users = Collections.singletonList(user);

    Mockito.when(bookingFacade.getUserById(id)).thenReturn(user);
    Mockito.when(bookingFacade.getUserByEmail(email)).thenReturn(user);
    Mockito.when(bookingFacade.getUsersByName(name, 1, 1)).thenReturn(users);
    Mockito.when(bookingFacade.createUser(ArgumentMatchers.any(User.class))).thenReturn(user);
    Mockito.when(bookingFacade.updateUser(ArgumentMatchers.any(User.class))).thenReturn(user);
    Mockito.when(bookingFacade.deleteUser(id)).thenReturn(true);
  }

  @Test
  public void getUserById() throws Exception {
    this.mockMvc
        .perform(get("/user/id?id={id}", user.getId()))
        .andExpect(status().isOk())
        .andExpect(view().name("userTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", user));
  }

  @Test
  public void getUserByEmail() throws Exception {
    this.mockMvc
        .perform(get("/user/email?email={email}", user.getEmail()))
        .andExpect(status().isOk())
        .andExpect(view().name("userTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", user));
  }

  @Test
  public void getUsersByName() throws Exception {
    this.mockMvc
        .perform(get("/user/name?name={name}&pageSize=1&pageNum=1", user.getName()))
        .andExpect(status().isOk())
        .andExpect(view().name("userTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", users));
  }

  @Test
  public void createUser() throws Exception {

    objectNode.put("name", user.getName());
    objectNode.put("email", user.getEmail());

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

    objectNode.put("id", user.getId());
    objectNode.put("name", user.getName());
    objectNode.put("email", user.getEmail());

    this.mockMvc
        .perform(
            put("/user/update/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectNode.toString()))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  public void deleteUser() throws Exception {
    this.mockMvc
        .perform(delete("/user/delete/{id}", user.getId()))
        .andExpect(status().isOk())
        .andReturn();
  }
}
