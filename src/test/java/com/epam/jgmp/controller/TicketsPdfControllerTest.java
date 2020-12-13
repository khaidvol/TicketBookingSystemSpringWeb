package com.epam.jgmp.controller;

import com.epam.jgmp.config.TbsApplicationConfig;
import com.epam.jgmp.facade.BookingFacade;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TbsApplicationConfig.class)
@WebMvcTest(TicketsPdfController.class)
public class TicketsPdfControllerTest {

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
  public void getBookedTickets() throws Exception {
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
                "/printTickets/pdf?id={id}&name={name}&email={email}&pageSize=1&pageNum=1",
                id,
                name,
                email))
        .andExpect(status().isOk())
        .andExpect(view().name("pdfView"))
        .andExpect(model().attributeExists("tickets"))
        .andExpect(model().attribute("tickets", tickets));
  }
}
