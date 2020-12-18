package com.epam.jgmp.service.implementation;

import com.epam.jgmp.dao.Dao;
import com.epam.jgmp.dao.model.Event;
import com.epam.jgmp.dao.model.Ticket;
import com.epam.jgmp.dao.model.User;
import com.epam.jgmp.dao.storage.BookingStorage;
import com.epam.jgmp.exception.ApplicationException;
import com.epam.jgmp.service.TicketService;
import com.epam.jgmp.service.xml.ObjXMLMapper;
import com.epam.jgmp.service.xml.XMLTicket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

  private final Log logger = LogFactory.getLog(TicketServiceImpl.class);
  private final Dao<Ticket> ticketDao;
  private final BookingStorage bookingStorage;
  private final ObjXMLMapper objXMLMapper;

  // constructor-injection
  private TicketServiceImpl(
      Dao<Ticket> ticketDao, BookingStorage bookingStorage, ObjXMLMapper objXMLMapper) {
    this.ticketDao = ticketDao;
    this.bookingStorage = bookingStorage;
    this.objXMLMapper = objXMLMapper;
  }

  @PostConstruct
  private void init() {
    preloadTickets();
  }

  public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {

    Ticket ticket = new Ticket(userId, eventId, place, category);
    ticket.setId(ticketDao.getMaxId() + 1);

    if (!isPlaceFree(ticket)) {
      logger.error(
          String.format("Ticket booking failed. Place %s is already booked.", ticket.getPlace()));
      throw new ApplicationException("Ticket booking failed", HttpStatus.BAD_REQUEST);
    }

    ticketDao.create(ticket);
    logger.info("Ticket booked successfully. Ticket details: " + ticket.toString());

    return ticketDao.read(ticket.getId());
  }

  public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {

    List<Ticket> tickets = new ArrayList<>();

    for (Ticket ticket : ticketDao.readAll()) {
      if (ticket.getUserId() == user.getId()) {
        tickets.add(ticket);
      }
    }

    tickets.sort(
        (t1, t2) ->
            bookingStorage
                .getEvents()
                .get(t2.getEventId())
                .getDate()
                .compareTo(bookingStorage.getEvents().get(t1.getEventId()).getDate()));

    logger.info(String.format("%s ticket(s) found: ", tickets.size()));
    tickets.forEach(logger::info);

    return tickets;
  }

  public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {

    List<Ticket> tickets = new ArrayList<>();

    for (Ticket ticket : ticketDao.readAll()) {
      if (ticket.getEventId() == event.getId()) {
        tickets.add(ticket);
      }
    }
    tickets.sort(
        Comparator.comparing(t -> bookingStorage.getUsers().get(t.getUserId()).getEmail()));

    logger.info(String.format("%s ticket(s) found: ", tickets.size()));
    tickets.forEach(logger::info);

    return tickets;
  }

  public boolean cancelTicket(long ticketId) {

    boolean isTicketCanceled = false;

    if (ticketDao.read(ticketId) != null) {
      ticketDao.delete(ticketId);
      isTicketCanceled = true;
    }

    logger.info("Ticket canceled: " + isTicketCanceled);

    return isTicketCanceled;
  }

  // private method for place check
  private boolean isPlaceFree(Ticket ticket) {

    boolean isPlaceFree = true;

    for (Ticket storedTicket : ticketDao.readAll()) {
      if (storedTicket.getEventId() == ticket.getEventId()
          && storedTicket.getPlace() == ticket.getPlace()) {
        isPlaceFree = false;
      }
    }

    return isPlaceFree;
  }

  public void preloadTickets() {

    try {
      // parse tickets from xml file
      List<XMLTicket> tickets = objXMLMapper.xmlToObj();

      // book each XMLTicket (id is assigned and ticket is stored to the storage)
      tickets.forEach(
          ticket ->
              bookTicket(
                  ticket.getUserId(),
                  ticket.getEventId(),
                  ticket.getPlace(),
                  ticket.getCategory()));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
