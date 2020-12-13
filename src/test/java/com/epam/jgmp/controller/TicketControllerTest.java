package com.epam.jgmp.controller;

import com.epam.jgmp.config.TbsApplicationConfig;
import com.epam.jgmp.facade.BookingFacade;
import com.epam.jgmp.model.Event;
import com.epam.jgmp.model.Ticket;
import com.epam.jgmp.model.User;
import com.epam.jgmp.model.implementation.TicketImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasToString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TbsApplicationConfig.class)
@WebMvcTest(TicketController.class)
public class TicketControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private BookingFacade bookingFacade;

  private Ticket ticket;
  private List<Ticket> tickets;

  @Before
  public void setUp() throws ParseException {

    long ticketId = 1L;
    long userId = 1L;
    long eventId = 1L;
    int place = 1;
    Ticket.Category category = Ticket.Category.STANDARD;

    ticket = new TicketImpl(userId, eventId, place, category);
    ticket.setId(ticketId);

    tickets = Collections.singletonList(ticket);
  }

  @Test
  public void bookTicket() throws Exception {

    long userId = ticket.getUserId();
    long eventId = ticket.getEventId();
    int place = ticket.getPlace();

    Ticket.Category category = ticket.getCategory();

    Mockito.when(bookingFacade.bookTicket(userId, eventId, place, category)).thenReturn(ticket);

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
        .andExpect(model().attribute("result", ticket));
  }

  @Test
  public void getBookedTicketsForUser() throws Exception {
    long id = 1L;
    String name = "Test";
    String email = "test@gmail.com";
    Mockito.when(
            bookingFacade.getBookedTickets(
                ArgumentMatchers.any(User.class),
                ArgumentMatchers.any(Integer.class),
                ArgumentMatchers.any(Integer.class)))
        .thenReturn(tickets);
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
        .andExpect(model().attribute("result", tickets));
  }

  @Test
  public void getBookedTicketsForEvent() throws Exception {

    long id = 1L;
    String title = "Test";
    String day = "2020-12-12";

    Mockito.when(
            bookingFacade.getBookedTickets(
                ArgumentMatchers.any(Event.class),
                ArgumentMatchers.any(Integer.class),
                ArgumentMatchers.any(Integer.class)))
        .thenReturn(tickets);

    this.mockMvc
        .perform(
            get(
                "/ticket/eventTickets?id={id}&title={title}&day={day}&pageSize=1&pageNum=1",
                id,
                title,
                day))
        .andExpect(status().isOk())
        .andExpect(view().name("ticketTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", tickets));
  }

  @Test
  public void cancelTicket() throws Exception {

    long id = ticket.getId();

    Mockito.when(bookingFacade.cancelTicket(id)).thenReturn(true);

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
