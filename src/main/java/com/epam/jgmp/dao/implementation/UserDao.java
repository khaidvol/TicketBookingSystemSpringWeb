package com.epam.jgmp.dao.implementation;

import com.epam.jgmp.dao.Dao;
import com.epam.jgmp.dao.model.User;
import com.epam.jgmp.dao.storage.BookingStorage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class UserDao implements Dao<User> {

  private BookingStorage bookingStorage;

  private UserDao(BookingStorage bookingStorage) {
    this.bookingStorage = bookingStorage;
  }
  @Override
  public User create(User user) {
    return bookingStorage.getUsers().put(user.getId(), user);
  }

  @Override
  public User read(long userId) {
    return bookingStorage.getUsers().get(userId);
  }

  @Override
  public List<User> readAll() {
    return new ArrayList<>(bookingStorage.getUsers().values());
  }

  @Override
  public User update(User user) {
    return bookingStorage.getUsers().replace(user.getId(), user);
  }

  @Override
  public User delete(long userId) {
    return bookingStorage.getUsers().remove(userId);
  }

  @Override
  public Long getMaxId() {
    return bookingStorage.getUsers().keySet().isEmpty()
        ? 0L
        : Collections.max(bookingStorage.getUsers().keySet());
  }
}
