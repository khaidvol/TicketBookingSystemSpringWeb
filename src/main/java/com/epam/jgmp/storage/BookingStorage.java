package com.epam.jgmp.storage;

import com.epam.jgmp.model.*;
import com.epam.jgmp.model.implementation.EventImpl;
import com.epam.jgmp.model.implementation.TicketImpl;
import com.epam.jgmp.model.implementation.UserImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BookingStorage {

  private Log logger = LogFactory.getLog(BookingStorage.class);

  private Map<Long, User> users;
  private Map<Long, Event> events;
  private Map<Long, Ticket> tickets;

  @Value("${app.data}")
  private String appDataPath;

  public BookingStorage() {
    users = new HashMap<>();
    events = new HashMap<>();
    tickets = new HashMap<>();
  }

  @PostConstruct
  public void init() {
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(appDataPath))) {
      String bufferedLine;

      while ((bufferedLine = bufferedReader.readLine()) != null) {
        String[] line = bufferedLine.split(", ");

        switch (line[0]) {
          case "User":
            User user = new UserImpl(line[1], line[2]);
            user.setId(Integer.parseInt(line[3]));
            users.put(user.getId(), user);
            logger.info("User loaded from file: " + user.toString());
            break;

          case "Event":
            Event event = new EventImpl(line[1], new SimpleDateFormat("yyyy-MM-dd").parse(line[2]));
            event.setId(Integer.parseInt(line[3]));
            events.put(event.getId(), event);
            logger.info("Event loaded from file: " + event.toString());
            break;

          case "Ticket":
            Ticket ticket =
                new TicketImpl(
                    Integer.parseInt(line[1]),
                    Integer.parseInt(line[2]),
                    Integer.parseInt(line[3]),
                    Ticket.Category.valueOf(line[4]));
            ticket.setId(Integer.parseInt(line[5]));
            tickets.put(ticket.getId(), ticket);
            logger.info("Ticket loaded from file: " + ticket.toString());
            break;

          default:
            break;
        }
      }

    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }
  }

  public Map<Long, User> getUsers() {
    return users;
  }

  public Map<Long, Event> getEvents() {
    return events;
  }

  public Map<Long, Ticket> getTickets() {
    return tickets;
  }

  public void setAppDataPath(String appDataPath) {
    this.appDataPath = appDataPath;
  }

  public void cleanStorage() {
    users.clear();
    events.clear();
    tickets.clear();
  }
}
