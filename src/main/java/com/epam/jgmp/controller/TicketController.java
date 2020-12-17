package com.epam.jgmp.controller;

import com.epam.jgmp.dao.model.Event;
import com.epam.jgmp.dao.model.Ticket;
import com.epam.jgmp.dao.model.User;
import com.epam.jgmp.facade.BookingFacade;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

  public static final String RESULT = "result";
  public static final String TICKET_TEMPLATE = "ticketTemplate";
  private final BookingFacade bookingFacade;

  public TicketController(BookingFacade bookingFacade) {
    this.bookingFacade = bookingFacade;
  }

  @PostMapping("/book")
  public Ticket bookTicket(@RequestBody Ticket ticket) {

    return bookingFacade.bookTicket(ticket.getUserId(), ticket.getEventId(), ticket.getPlace(), ticket.getCategory());
  }

  @GetMapping("/userTickets")
  public ModelAndView getBookedTicketsForUser(
      @RequestParam(value = "id", required = true) Long id,
      @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "email", required = true) String email,
      @RequestParam(value = "pageSize", required = true) int pageSize,
      @RequestParam(value = "pageNum", required = true) int pageNum) {

    User user = new User(id, name, email);
    List<Ticket> tickets = bookingFacade.getBookedTickets(user, pageSize, pageNum);

    return new ModelAndView(TICKET_TEMPLATE, RESULT, tickets);
  }

  @GetMapping("/eventTickets")
  public ModelAndView getBookedTicketsForEvent(
      @RequestParam(value = "id") Long id,
      @RequestParam(value = "title") String title,
      @RequestParam(value = "day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date day,
      @RequestParam(value = "pageSize") int pageSize,
      @RequestParam(value = "pageNum") int pageNum) {

    Event event = new Event(id, title, day);
    List<Ticket> tickets = bookingFacade.getBookedTickets(event, pageSize, pageNum);

    return new ModelAndView(TICKET_TEMPLATE, RESULT, tickets);
  }

  @DeleteMapping("/cancel/{id}")
  public ResponseEntity<Long> cancelTicket(@PathVariable Long id) {

    boolean isCanceled = bookingFacade.cancelTicket(id);
    if(!isCanceled) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(id, HttpStatus.OK);
  }
}
