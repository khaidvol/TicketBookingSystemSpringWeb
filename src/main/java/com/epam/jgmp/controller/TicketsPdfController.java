package com.epam.jgmp.controller;

import com.epam.jgmp.dao.model.Ticket;
import com.epam.jgmp.dao.model.User;
import com.epam.jgmp.facade.BookingFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/printTickets")
public class TicketsPdfController {

  public static final String PDF_VIEW = "pdfView";
  public static final String TICKETS = "tickets";
  private final BookingFacade bookingFacade;

  public TicketsPdfController(BookingFacade bookingFacade) {
    this.bookingFacade = bookingFacade;
  }

  @GetMapping(value = "/pdf")
  public ModelAndView getBookedTickets(
      @RequestParam(value = "id") Long id,
      @RequestParam(value = "name") String name,
      @RequestParam(value = "email") String email,
      @RequestParam(value = "pageSize") int pageSize,
      @RequestParam(value = "pageNum") int pageNum) {

    User user = new User(id, name, email);
    List<Ticket> tickets = bookingFacade.getBookedTickets(user, pageSize, pageNum);

    return new ModelAndView(PDF_VIEW, TICKETS, tickets);
  }
}
