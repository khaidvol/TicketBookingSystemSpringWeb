package com.epam.jgmp.service;


import com.epam.jgmp.dao.Dao;
import com.epam.jgmp.exception.ApplicationException;
import com.epam.jgmp.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

  private static final Log LOGGER = LogFactory.getLog(UserService.class);
  private final Dao<User> userDao;

  // constructor-injection
  private UserService(Dao<User> userDao) {
    this.userDao = userDao;
  }

  public User getUserById(long userId) {

    User user = userDao.read(userId);

    if (user == null) {
      LOGGER.error("User not found.");
      throw new ApplicationException("User not found", HttpStatus.NOT_FOUND);
    }

    LOGGER.info("User found: " + user.toString());

    return user;
  }

  public User getUserByEmail(String email) {

    for (User user : userDao.readAll()) {
      if (user.getEmail().equals(email)) {
        LOGGER.info("User found: " + user.toString());
        return user;
      }
    }
    LOGGER.error("User not found.");
    throw new ApplicationException("User not found", HttpStatus.NOT_FOUND);
  }

  public List<User> getUsersByName(String name, int pageSize, int pageNum) {

    List<User> foundUsers = new ArrayList<>();

    for (User user : userDao.readAll()) {
      if (user.getName().contains(name)) {
        foundUsers.add(user);
      }
    }

    LOGGER.info(String.format("%s user(s) found: ", foundUsers.size()));
    foundUsers.forEach(LOGGER::info);

    return foundUsers;
  }

  public User createUser(User user) {

    user.setId(userDao.getMaxId() + 1);

    if (!isMailFree(user)) {
      LOGGER.error("User not created because of provided email address already used.");
      throw new ApplicationException("User not created", HttpStatus.BAD_REQUEST);
    }

    userDao.create(user);
    LOGGER.info("User created successfully. User details: " + user.toString());

    return userDao.read(user.getId());
  }

  public User updateUser(User user) {

    if (userDao.read(user.getId()) == null || !isMailFree(user)) {
      LOGGER.error("User not updated because not found or provided email address already used.");
      throw new ApplicationException("User not updated", HttpStatus.NOT_FOUND);
    }

    userDao.update(user);
    LOGGER.info("User updated successfully. User details: " + user.toString());

    return userDao.read(user.getId());
  }

  public boolean deleteUser(long userId) {

    boolean isUserDeleted = false;

    if (userDao.read(userId) != null) {
      userDao.delete(userId);
      isUserDeleted = true;
    }
    LOGGER.info("User deleted: " + isUserDeleted);

    return isUserDeleted;
  }

  // private method for mail check
  private boolean isMailFree(User user) {

    boolean isMailFree = true;

    for (User storedUser : userDao.readAll()) {
      if (storedUser.getId() != user.getId() && storedUser.getEmail().equals(user.getEmail()))
        isMailFree = false;
    }

    return isMailFree;
  }
}
