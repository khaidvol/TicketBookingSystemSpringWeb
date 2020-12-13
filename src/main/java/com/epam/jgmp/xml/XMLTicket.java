package com.epam.jgmp.xml;

import com.epam.jgmp.model.Ticket;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class XMLTicket {

  private long userId;
  private long eventId;
  private int place;
  private Ticket.Category category;

  public XMLTicket() {}

  public XMLTicket(long userId, long eventId, int place, Ticket.Category category) {
    this.userId = userId;
    this.eventId = eventId;
    this.place = place;
    this.category = category;
  }

  @XmlAttribute(name = "event")
  public long getEventId() {
    return eventId;
  }

  public void setEventId(long eventId) {
    this.eventId = eventId;
  }

  @XmlAttribute(name = "user")
  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  @XmlAttribute(name = "place")
  public int getPlace() {
    return place;
  }

  public void setPlace(int place) {
    this.place = place;
  }

  @XmlAttribute(name = "category")
  public Ticket.Category getCategory() {
    return category;
  }

  public void setCategory(Ticket.Category category) {
    this.category = category;
  }
}
