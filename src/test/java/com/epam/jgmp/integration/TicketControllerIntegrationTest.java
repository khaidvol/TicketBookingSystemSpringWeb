package com.epam.jgmp.integration;

import com.epam.jgmp.config.TbsApplicationConfig;
import com.epam.jgmp.facade.BookingFacade;
import com.epam.jgmp.model.Ticket;
import com.epam.jgmp.model.User;
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
public class TicketControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private BookingFacade bookingFacade;

  @Test
  public void bookTicket() throws Exception {

    long userId = 4L;
    long eventId = 1L;
    int place = 666;
    Ticket.Category category = Ticket.Category.STANDARD;

    this.mockMvc
        .perform(
            post(
                "/ticket/book?userId={userId}&eventId={eventId}&place={place}&category={category}",
                userId,
                eventId,
                place,
                category))
        .andExpect(status().isOk())
        .andExpect(view().name("ticketTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(
            model()
                .attribute(
                    "result",
                    bookingFacade
                        .getBookedTickets(bookingFacade.getUserById(userId), 1, 1)
                        .get(0)));
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

    long id = 2L;

    this.mockMvc
        .perform(delete("/ticket/cancel?id={id}", id))
        .andExpect(status().isOk())
        .andExpect(view().name("ticketTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(
            model()
                .attribute("result", hasToString(String.format("Ticket #%s canceled: true", id))));
  }

  @Test
  public void preloadTicket() throws Exception {

    this.mockMvc
        .perform(get("/ticket/preload"))
        .andExpect(status().isOk())
        .andExpect(view().name("ticketTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", hasToString("Tickets preloaded")));
  }
}
