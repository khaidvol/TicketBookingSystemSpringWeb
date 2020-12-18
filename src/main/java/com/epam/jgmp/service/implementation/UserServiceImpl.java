package com.epam.jgmp.service.implementation;

import com.epam.jgmp.dao.Dao;
import com.epam.jgmp.dao.model.User;
import com.epam.jgmp.exception.ApplicationException;
import com.epam.jgmp.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

  private static final Log logger = LogFactory.getLog(UserServiceImpl.class);
  private final Dao<User> userDao;

  // constructor-injection
  private UserServiceImpl(Dao<User> userDao) {
    this.userDao = userDao;
  }

  public User getUserById(long userId) {

    User user = userDao.read(userId);

    if (user == null) {
      logger.error("User not found.");
      throw new ApplicationException("User not found", HttpStatus.NOT_FOUND);
    }

    logger.info("User found: " + user.toString());

    return user;
  }

  public User getUserByEmail(String email) {

    for (User user : userDao.readAll()) {
      if (user.getEmail().equals(email)) {
        logger.info("User found: " + user.toString());
        return user;
      }
    }
    logger.error("User not found.");
    throw new ApplicationException("User not found", HttpStatus.NOT_FOUND);
  }

  public List<User> getUsersByName(String name, int pageSize, int pageNum) {

    List<User> foundUsers = new ArrayList<>();

    for (User user : userDao.readAll()) {
      if (user.getName().contains(name)) {
        foundUsers.add(user);
      }
    }

    logger.info(String.format("%s user(s) found: ", foundUsers.size()));
    foundUsers.forEach(logger::info);

    return foundUsers;
  }

  public User createUser(User user) {

    user.setId(userDao.getMaxId() + 1);

    if (!isMailFree(user)) {
      logger.error("User not created because of provided email address already used.");
      throw new ApplicationException("User not created", HttpStatus.BAD_REQUEST);
    }

    userDao.create(user);
    logger.info("User created successfully. User details: " + user.toString());

    return userDao.read(user.getId());
  }

  public User updateUser(User user) {

    if (userDao.read(user.getId()) == null || !isMailFree(user)) {
      logger.error("User not updated because not found or provided email address already used.");
      throw new ApplicationException("User not updated", HttpStatus.NOT_FOUND);
    }

    userDao.update(user);
    logger.info("User updated successfully. User details: " + user.toString());

    return userDao.read(user.getId());
  }

  public boolean deleteUser(long userId) {

    boolean isUserDeleted = false;

    if (userDao.read(userId) != null) {
      userDao.delete(userId);
      isUserDeleted = true;
    }
    logger.info("User deleted: " + isUserDeleted);

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
