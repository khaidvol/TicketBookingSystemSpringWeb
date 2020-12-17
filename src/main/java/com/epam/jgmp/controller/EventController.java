package com.epam.jgmp.controller;

import com.epam.jgmp.dao.model.Event;
import com.epam.jgmp.facade.BookingFacade;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@RestController
@RequestMapping("/event")
public class EventController {

  public static final String RESULT = "result";
  public static final String EVENT_TEMPLATE = "eventTemplate";
  private final BookingFacade bookingFacade;

  public EventController(BookingFacade bookingFacade) {
    this.bookingFacade = bookingFacade;
  }

  @GetMapping("/id")
  public ModelAndView getEventById(@RequestParam(value = "id") Long id) {

    return new ModelAndView(EVENT_TEMPLATE, RESULT, bookingFacade.getEventById(id));
  }

  @GetMapping("/title")
  public ModelAndView getEventsByTitle(
      @RequestParam(value = "title") String title,
      @RequestParam(value = "pageSize") int pageSize,
      @RequestParam(value = "pageNum") int pageNum) {

    return new ModelAndView(
        EVENT_TEMPLATE, RESULT, bookingFacade.getEventsByTitle(title, pageSize, pageNum));
  }

  @GetMapping("/day")
  public ModelAndView getEventsForDay(
      @RequestParam(value = "day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date day,
      @RequestParam(value = "pageSize") int pageSize,
      @RequestParam(value = "pageNum") int pageNum) {

    return new ModelAndView(
        EVENT_TEMPLATE, RESULT, bookingFacade.getEventsForDay(day, pageSize, pageNum));
  }

  @PostMapping("/new")
  public Event createEvent(@RequestBody Event event) {

    return bookingFacade.createEvent(event);
  }

  @PutMapping("/update/{id}")
  public Event updateEvent(@RequestBody Event event, @PathVariable Long id) {

    event.setId(id);
    return bookingFacade.updateEvent(event);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Long> deleteEvent(@PathVariable Long id) {

    boolean isDeleted = bookingFacade.deleteEvent(id);

    if (!isDeleted) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(id, HttpStatus.OK);
  }
}
