package com.epam.jgmp.dao;

import com.epam.jgmp.config.TestConfig;
import com.epam.jgmp.dao.implementation.TicketDao;
import com.epam.jgmp.dao.model.Ticket;
import com.epam.jgmp.dao.storage.BookingStorage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TicketDaoTest {

  ApplicationContext context;
  BookingStorage bookingStorage;
  Dao<Ticket> ticketDao;
  Ticket ticket;

  public TicketDaoTest() {}

  @Before
  public void setUp() {

    context = new AnnotationConfigApplicationContext(TestConfig.class);
    bookingStorage = context.getBean(BookingStorage.class);
    ticketDao = context.getBean(TicketDao.class);

    ticket = Mockito.mock(Ticket.class);
    Mockito.when(ticket.getId()).thenReturn(666L);
  }

  @Test
  public void createTest() {
    Assert.assertNull(ticketDao.create(ticket));
    Assert.assertEquals(ticket, ticketDao.read(ticket.getId()));
  }

  @Test
  public void readTest() {
    Assert.assertNull(ticketDao.create(ticket));
    Assert.assertEquals(ticket, ticketDao.read(ticket.getId()));
  }

  @Test
  public void readAllTest() {
    Assert.assertNull(ticketDao.create(ticket));
    Assert.assertNotNull(ticketDao.readAll());
  }

  @Test
  public void updateTest() {
    Assert.assertNull(ticketDao.create(ticket));
    Assert.assertEquals(ticket, ticketDao.update(ticket));
  }

  @Test
  public void deleteTest() {
    Assert.assertNull(ticketDao.create(ticket));
    Assert.assertEquals(ticket, ticketDao.delete(ticket.getId()));
  }

  @Test
  public void getMaxIdWhenStorageNotEmptyTest() {
    Assert.assertNull(ticketDao.create(ticket));
    Assert.assertNotNull(ticketDao.getMaxId());
  }

  @Test
  public void getMaxIdWhenStorageIsEmptyTest() {
    bookingStorage.getTickets().clear();
    Assert.assertNotNull(ticketDao.getMaxId());
  }

  @After
  public void cleanUp() {
    bookingStorage.cleanStorage();
  }
}
