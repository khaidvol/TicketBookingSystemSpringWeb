package com.epam.jgmp.integration;

import com.epam.jgmp.config.TbsApplicationConfig;
import com.epam.jgmp.dao.model.Ticket;
import com.epam.jgmp.dao.model.User;
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
public class TicketControllerIntegrationTest {

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
  public void bookTicket() throws Exception {

    objectNode.put("userId", 1L);
    objectNode.put("eventId", 1L);
    objectNode.put("place", 666);
    objectNode.put("category", Ticket.Category.STANDARD.toString());

    this.mockMvc
        .perform(
            post("/ticket/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectNode.toString()))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  public void getBookedTicketsForUser() throws Exception {

    User user = bookingFacade.getUserById(1L);

    long id = user.getId();
    String name = user.getName();
    String email = user.getEmail();

    this.mockMvc
        .perform(
            get(
                "/ticket/userTickets?id={id}&name={name}&email={email}&pageSize=1&pageNum=1",
                id,
                name,
                email))
        .andExpect(status().isOk())
        .andExpect(view().name("ticketTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", bookingFacade.getBookedTickets(user, 1, 1)));
  }

  @Test
  public void cancelTicket() throws Exception {

    this.mockMvc.perform(delete("/ticket/cancel/{id}", 2L)).andExpect(status().isOk()).andReturn();
  }
}
