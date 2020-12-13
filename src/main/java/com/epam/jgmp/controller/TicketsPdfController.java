package com.epam.jgmp.controller;

import com.epam.jgmp.facade.BookingFacade;
import com.epam.jgmp.model.Ticket;
import com.epam.jgmp.model.User;
import com.epam.jgmp.model.implementation.UserImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/printTickets")
public class TicketsPdfController {

  public static final String PDF_VIEW = "pdfView";
  public static final String TICKETS = "tickets";
  private BookingFacade bookingFacade;

  public TicketsPdfController(BookingFacade bookingFacade) {
    this.bookingFacade = bookingFacade;
  }

  @GetMapping(value = "/pdf")
  public ModelAndView getBookedTickets(
      @RequestParam(value = "id", required = true) Long id,
      @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "email", required = true) String email,
      @RequestParam(value = "pageSize", required = true) int pageSize,
      @RequestParam(value = "pageNum", required = true) int pageNum) {

    User user = new UserImpl(id, name, email);
    List<Ticket> tickets = bookingFacade.getBookedTickets(user, pageSize, pageNum);

    return new ModelAndView(PDF_VIEW, TICKETS, tickets);
  }
}
