package com.epam.jgmp.controller;

import com.epam.jgmp.facade.BookingFacade;
import com.epam.jgmp.model.Event;
import com.epam.jgmp.model.Ticket;
import com.epam.jgmp.model.User;
import com.epam.jgmp.model.implementation.EventImpl;
import com.epam.jgmp.model.implementation.UserImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/ticket")
public class TicketController {

  public static final String RESULT = "result";
  public static final String TICKET_TEMPLATE = "ticketTemplate";
  private BookingFacade bookingFacade;

  public TicketController(BookingFacade bookingFacade) {
    this.bookingFacade = bookingFacade;
  }

  @PostMapping("/book")
  public ModelAndView bookTicket(
      @RequestParam(value = "userId", required = true) Long userId,
      @RequestParam(value = "eventId", required = true) Long eventId,
      @RequestParam(value = "place", required = true) int place,
      @RequestParam(value = "category", required = true) Ticket.Category category) {

    Ticket ticket = bookingFacade.bookTicket(userId, eventId, place, category);

    ModelAndView modelAndView = new ModelAndView(TICKET_TEMPLATE);
    modelAndView.addObject(RESULT, ticket);

    return modelAndView;
  }

  @GetMapping("/userTickets")
  public ModelAndView getBookedTicketsForUser(
      @RequestParam(value = "id", required = true) Long id,
      @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "email", required = true) String email,
      @RequestParam(value = "pageSize", required = true) int pageSize,
      @RequestParam(value = "pageNum", required = true) int pageNum) {

    User user = new UserImpl(id, name, email);
    List<Ticket> tickets = bookingFacade.getBookedTickets(user, pageSize, pageNum);

    ModelAndView modelAndView = new ModelAndView(TICKET_TEMPLATE);
    modelAndView.addObject(RESULT, tickets);

    return modelAndView;
  }

  @GetMapping("/eventTickets")
  public ModelAndView getBookedTicketsForEvent(
      @RequestParam(value = "id", required = true) Long id,
      @RequestParam(value = "title", required = true) String title,
      @RequestParam(value = "day", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          Date day,
      @RequestParam(value = "pageSize", required = true) int pageSize,
      @RequestParam(value = "pageNum", required = true) int pageNum) {

    Event event = new EventImpl(id, title, day);
    List<Ticket> tickets = bookingFacade.getBookedTickets(event, pageSize, pageNum);

    ModelAndView modelAndView = new ModelAndView(TICKET_TEMPLATE);
    modelAndView.addObject(RESULT, tickets);

    return modelAndView;
  }

  @DeleteMapping("/cancel")
  public String cancelTicket(@RequestParam(value = "id", required = true) Long id, Model model) {

    Boolean deleteResult = bookingFacade.cancelTicket(id);
    model.addAttribute(RESULT, String.format("Ticket #%s canceled: %s", id, deleteResult));

    return TICKET_TEMPLATE;
  }

  @GetMapping("/preload")
  public String preloadTicket(Model model) {

    bookingFacade.preloadTickets();
    model.addAttribute(RESULT, "Tickets preloaded");

    return TICKET_TEMPLATE;
  }
}
