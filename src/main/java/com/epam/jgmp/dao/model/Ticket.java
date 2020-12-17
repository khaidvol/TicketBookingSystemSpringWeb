package com.epam.jgmp.dao.model;

public class Ticket {

  public enum Category {
    STANDARD,
    PREMIUM,
    BAR
  }

  private long id;
  private long userId;
  private long eventId;
  private int place;
  private Category category;

  public Ticket() {}

  public Ticket(long userId, long eventId, int place, Category category) {
    this.userId = userId;
    this.eventId = eventId;
    this.place = place;
    this.category = category;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getEventId() {
    return eventId;
  }

  public void setEventId(long eventId) {
    this.eventId = eventId;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public int getPlace() {
    return place;
  }

  public void setPlace(int place) {
    this.place = place;
  }

  public String toString() {
    return "Ticket: "
        + "id="
        + id
        + ", eventId="
        + eventId
        + ", userId="
        + userId
        + ", place="
        + place
        + ", category="
        + category
        + '.';
  }
}
