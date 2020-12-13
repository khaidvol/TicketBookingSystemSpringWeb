package com.epam.jgmp.dao.implementation;

import com.epam.jgmp.dao.Dao;
import com.epam.jgmp.model.Event;
import com.epam.jgmp.storage.BookingStorage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class EventDao implements Dao<Event> {

  private final BookingStorage bookingStorage;

  private EventDao(BookingStorage bookingStorage) {
    this.bookingStorage = bookingStorage;
  }

  @Override
  public Event create(Event event) {
    return bookingStorage.getEvents().put(event.getId(), event);
  }

  @Override
  public Event read(long eventId) {
    return bookingStorage.getEvents().get(eventId);
  }

  @Override
  public List<Event> readAll() {
    return new ArrayList<>(bookingStorage.getEvents().values());
  }

  @Override
  public Event update(Event event) {
    return bookingStorage.getEvents().replace(event.getId(), event);
  }

  @Override
  public Event delete(long eventId) {
    return bookingStorage.getEvents().remove(eventId);
  }

  @Override
  public Long getMaxId() {
    return bookingStorage.getEvents().keySet().isEmpty()
        ? 0L
        : Collections.max(bookingStorage.getEvents().keySet());
  }
}
