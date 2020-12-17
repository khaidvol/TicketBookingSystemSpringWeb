package com.epam.jgmp.controller;

import com.epam.jgmp.dao.model.User;
import com.epam.jgmp.facade.BookingFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/user")
public class UserController {

  public static final String RESULT = "result";
  public static final String USER_TEMPLATE = "userTemplate";
  private final BookingFacade bookingFacade;

  public UserController(BookingFacade bookingFacade) {
    this.bookingFacade = bookingFacade;
  }

  @GetMapping("/id")
  public ModelAndView getUserById(@RequestParam(value = "id") Long id) {

    return new ModelAndView(USER_TEMPLATE, RESULT, bookingFacade.getUserById(id));
  }

  @GetMapping("/email")
  public ModelAndView getUserByEmail(@RequestParam(value = "email") String email) {

    return new ModelAndView(USER_TEMPLATE, RESULT, bookingFacade.getUserByEmail(email));
  }

  @GetMapping("/name")
  public ModelAndView getUsersByName(
      @RequestParam(value = "name") String name,
      @RequestParam(value = "pageSize") int pageSize,
      @RequestParam(value = "pageNum") int pageNum) {

    return new ModelAndView(
        USER_TEMPLATE, RESULT, bookingFacade.getUsersByName(name, pageSize, pageNum));
  }

  @PostMapping(value = "/new")
  public User createUser(@RequestBody User user) {

    return bookingFacade.createUser(user);
  }

  @PutMapping(value = "/update/{id}")
  public User updateUser(@RequestBody User user, @PathVariable Long id) {

    user.setId(id);
    return bookingFacade.updateUser(user);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Long> deleteUser(@PathVariable Long id) {

    boolean isDeleted = bookingFacade.deleteUser(id);
    if(!isDeleted) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(id, HttpStatus.OK);
  }
}
