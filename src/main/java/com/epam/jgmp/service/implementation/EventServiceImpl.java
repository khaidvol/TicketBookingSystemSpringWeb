package com.epam.jgmp.service.implementation;

import com.epam.jgmp.dao.Dao;
import com.epam.jgmp.dao.model.Event;
import com.epam.jgmp.exception.ApplicationException;
import com.epam.jgmp.service.EventService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

  private static final Log logger = LogFactory.getLog(EventServiceImpl.class);
  private final Dao<Event> eventDao;

  // constructor-injection
  private EventServiceImpl(Dao<Event> eventDao) {
    this.eventDao = eventDao;
  }

  public Event getEventById(long eventId) {

    Event event = eventDao.read(eventId);

    if (event == null) {
      logger.error("Event not found.");
      throw new ApplicationException("Event not found", HttpStatus.NOT_FOUND);
    }

    logger.info("Event found: " + event.toString());

    return event;
  }

  public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {

    List<Event> foundEvents = new ArrayList<>();

    for (Event event : eventDao.readAll()) {
      if (event.getTitle().contains(title)) {
        foundEvents.add(event);
      }
    }

    logger.info(String.format("%s event(s) found: ", foundEvents.size()));
    foundEvents.forEach(logger::info);

    return foundEvents;
  }

  public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {

    List<Event> foundEvents = new ArrayList<>();

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(day);
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

    for (Event event : eventDao.readAll()) {
      calendar.setTime(event.getDate());

      if (calendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
        foundEvents.add(event);
      }
    }

    logger.info(String.format("%s event(s) found: ", foundEvents.size()));
    foundEvents.forEach(logger::info);

    return foundEvents;
  }

  public Event createEvent(Event event) {

    event.setId(eventDao.getMaxId() + 1);
    eventDao.create(event);
    logger.info("Event created successfully. Event details: " + event.toString());

    return eventDao.read(event.getId());
  }

  public Event updateEvent(Event event) {

    if (eventDao.read(event.getId()) == null) {
      logger.error("Event not updated because of not found.");
      throw new ApplicationException("Event not updated", HttpStatus.NOT_FOUND);
    }

    eventDao.update(event);
    logger.info("Event updated successfully. Event details: " + event.toString());

    return eventDao.read(event.getId());
  }

  public boolean deleteEvent(long eventId) {

    boolean isEventDeleted = false;

    if (eventDao.read(eventId) != null) {
      eventDao.delete(eventId);
      isEventDeleted = true;
    }
    logger.info("Event deleted: " + isEventDeleted);

    return isEventDeleted;
  }
}
