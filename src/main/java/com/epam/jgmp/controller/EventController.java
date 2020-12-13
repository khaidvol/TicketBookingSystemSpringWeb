package com.epam.jgmp.controller;

import com.epam.jgmp.facade.BookingFacade;
import com.epam.jgmp.model.Event;
import com.epam.jgmp.model.implementation.EventImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/event")
public class EventController {

  public static final String RESULT = "result";
  public static final String EVENT_TEMPLATE = "eventTemplate";
  private BookingFacade bookingFacade;

  public EventController(BookingFacade bookingFacade) {
    this.bookingFacade = bookingFacade;
  }

  @GetMapping("/id")
  public ModelAndView getEventById(@RequestParam(value = "id", required = true) Long id) {

    Event event = bookingFacade.getEventById(id);

    ModelAndView modelAndView = new ModelAndView(EVENT_TEMPLATE);
    modelAndView.addObject(RESULT, event);

    return modelAndView;
  }

  @GetMapping("/title")
  public ModelAndView getEventsByTitle(
      @RequestParam(value = "title", required = true) String title,
      @RequestParam(value = "pageSize", required = true) int pageSize,
      @RequestParam(value = "pageNum", required = true) int pageNum) {

    List<Event> events = bookingFacade.getEventsByTitle(title, pageSize, pageNum);

    ModelAndView modelAndView = new ModelAndView(EVENT_TEMPLATE);
    modelAndView.addObject(RESULT, events);

    return modelAndView;
  }

  @GetMapping("/day")
  public ModelAndView getEventsForDay(
      @RequestParam(value = "day", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          Date day,
      @RequestParam(value = "pageSize", required = true) int pageSize,
      @RequestParam(value = "pageNum", required = true) int pageNum) {

    List<Event> events = bookingFacade.getEventsForDay(day, pageSize, pageNum);

    ModelAndView modelAndView = new ModelAndView(EVENT_TEMPLATE);
    modelAndView.addObject(RESULT, events);

    return modelAndView;
  }

  @PostMapping("/new")
  public String createEvent(
      @RequestParam(value = "title", required = true) String title,
      @RequestParam(value = "day", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          Date day,
      Model model) {

    Event event = new EventImpl(title, day);
    event = bookingFacade.createEvent(event);

    model.addAttribute(RESULT, "Event created: ".concat(event.toString()));

    return EVENT_TEMPLATE;
  }

  @PutMapping("/update")
  public String updateEvent(
      @RequestParam(value = "id", required = true) Long id,
      @RequestParam(value = "title", required = true) String title,
      @RequestParam(value = "day", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          Date day,
      Model model) {

    Event event = new EventImpl(id, title, day);
    event = bookingFacade.updateEvent(event);

    model.addAttribute(RESULT, "Event updated: ".concat(event.toString()));

    return EVENT_TEMPLATE;
  }

  @DeleteMapping("/delete")
  public String deleteEvent(@RequestParam(value = "id", required = true) Long id, Model model) {

    Boolean deleteResult = bookingFacade.deleteEvent(id);

    model.addAttribute(RESULT, String.format("Event #%s deleted: %s", id, deleteResult));

    return EVENT_TEMPLATE;
  }
}
