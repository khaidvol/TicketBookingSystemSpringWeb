package com.epam.jgmp.service;

import com.epam.jgmp.config.TestConfig;
import com.epam.jgmp.dao.model.Event;
import com.epam.jgmp.dao.storage.BookingStorage;
import com.epam.jgmp.exception.ApplicationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventServiceImplTest {

  ApplicationContext context;
  BookingStorage bookingStorage;
  EventService eventService;
  Event event;

  public EventServiceImplTest() {}

  @Before
  public void setUp() {
    context = new AnnotationConfigApplicationContext(TestConfig.class);
    eventService = context.getBean(EventService.class);
    bookingStorage = context.getBean(BookingStorage.class);
    event = Mockito.mock(Event.class);
  }

  @Test
  public void getEventByIdTest() {
    Assert.assertEquals(event, eventService.createEvent(event));
    Assert.assertEquals(event, eventService.getEventById(event.getId()));
  }

  @Test(expected = ApplicationException.class)
  public void getNotExistingEventByIdTest() {
    eventService.getEventById(1000L);
  }

  @Test
  public void getEventsByTitleTest() {
    Assert.assertNotNull(eventService.getEventsByTitle("Disco", 1, 1));
  }

  @Test
  public void getEventForDayTest() throws ParseException {
    String inputString = "2020-06-28";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date inputDate = dateFormat.parse(inputString);
    Assert.assertNotNull(eventService.getEventsForDay(inputDate, 1, 1));
  }

  @Test
  public void createEventTest() {
    Assert.assertEquals(event, eventService.createEvent(event));
  }

  @Test
  public void updateEventTest() {
    Assert.assertEquals(event, eventService.createEvent(event));
    Assert.assertEquals(event, eventService.updateEvent(event));
  }

  @Test(expected = ApplicationException.class)
  public void updateNotExistingEventTest() {
    Mockito.when(event.getId()).thenReturn(100L);
    eventService.updateEvent(event);
  }

  @Test
  public void deleteEventTest() {
    Assert.assertEquals(event, eventService.createEvent(event));
    Assert.assertTrue(eventService.deleteEvent(event.getId()));
  }

  @After
  public void cleanUp() {
    bookingStorage.cleanStorage();
  }
}
