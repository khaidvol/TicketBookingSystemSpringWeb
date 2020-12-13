package com.epam.jgmp.dao;

import com.epam.jgmp.config.TestConfig;
import com.epam.jgmp.dao.implementation.EventDao;
import com.epam.jgmp.model.Event;
import com.epam.jgmp.storage.BookingStorage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class EventDaoTest {

  ApplicationContext context;
  BookingStorage bookingStorage;
  Dao<Event> eventDao;
  Event event;

  public EventDaoTest() {}

  @Before
  public void setUp() {

    context = new AnnotationConfigApplicationContext(TestConfig.class);
    bookingStorage = context.getBean(BookingStorage.class);
    eventDao = context.getBean(EventDao.class);

    event = Mockito.mock(Event.class);
    Mockito.when(event.getId()).thenReturn(10L);
  }

  @Test
  public void createTest() {
    Assert.assertNull(eventDao.create(event));
    Assert.assertEquals(event, eventDao.read(event.getId()));
  }

  @Test
  public void readTest() {
    Assert.assertNull(eventDao.create(event));
    Assert.assertEquals(event, eventDao.read(event.getId()));
  }

  @Test
  public void readAllTest() {
    Assert.assertNull(eventDao.create(event));
    Assert.assertNotNull(eventDao.readAll());
  }

  @Test
  public void updateTest() {
    Assert.assertNull(eventDao.create(event));
    Assert.assertEquals(event, eventDao.update(event));
  }

  @Test
  public void deleteTest() {
    Assert.assertNull(eventDao.create(event));
    Assert.assertEquals(event, eventDao.delete(event.getId()));
  }

  @Test
  public void getMaxIdWhenStorageNotEmptyTest() {
    Assert.assertNull(eventDao.create(event));
    Assert.assertNotNull(eventDao.getMaxId());
  }

  @Test
  public void getMaxIdWhenStorageIsEmptyTest() {
    bookingStorage.getEvents().clear();
    Assert.assertNotNull(eventDao.getMaxId());
  }

  @After
  public void cleanUp() {
    bookingStorage.cleanStorage();
  }
}
