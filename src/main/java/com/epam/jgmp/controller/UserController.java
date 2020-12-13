package com.epam.jgmp.controller;

import com.epam.jgmp.facade.BookingFacade;
import com.epam.jgmp.model.User;
import com.epam.jgmp.model.implementation.UserImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

  public static final String RESULT = "result";
  public static final String USER_TEMPLATE = "userTemplate";
  private BookingFacade bookingFacade;

  public UserController(BookingFacade bookingFacade) {
    this.bookingFacade = bookingFacade;
  }

  @GetMapping("/id")
  public ModelAndView getUserById(@RequestParam(value = "id", required = true) Long id) {

    User user = bookingFacade.getUserById(id);

    ModelAndView modelAndView = new ModelAndView(USER_TEMPLATE);
    modelAndView.addObject(RESULT, user);

    return modelAndView;
  }

  @GetMapping("/email")
  public ModelAndView getUserByEmail(@RequestParam(value = "email", required = true) String email) {

    User user = bookingFacade.getUserByEmail(email);

    ModelAndView modelAndView = new ModelAndView(USER_TEMPLATE);
    modelAndView.addObject(RESULT, user);

    return modelAndView;
  }

  @GetMapping("/name")
  public ModelAndView getUsersByName(
      @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "pageSize", required = true) int pageSize,
      @RequestParam(value = "pageNum", required = true) int pageNum) {

    List<User> users = bookingFacade.getUsersByName(name, pageSize, pageNum);

    ModelAndView modelAndView = new ModelAndView(USER_TEMPLATE);
    modelAndView.addObject(RESULT, users);

    return modelAndView;
  }

  @PostMapping("/new")
  public String createUser(
      @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "email", required = true) String email,
      Model model) {

    User user = new UserImpl(name, email);
    user = bookingFacade.createUser(user);

    model.addAttribute(RESULT, "User created: ".concat(user.toString()));

    return USER_TEMPLATE;
  }

  @PutMapping("/update")
  public String updateUser(
      @RequestParam(value = "id", required = true) Long id,
      @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "email", required = true) String email,
      Model model) {

    User user = new UserImpl(id, name, email);
    user = bookingFacade.updateUser(user);

    model.addAttribute(RESULT, "User updated: ".concat(user.toString()));

    return USER_TEMPLATE;
  }

  @DeleteMapping("/delete")
  public String deleteUser(@RequestParam(value = "id", required = true) Long id, Model model) {

    Boolean deleteResult = bookingFacade.deleteUser(id);

    model.addAttribute(RESULT, String.format("User #%s deleted: %s", id, deleteResult));

    return USER_TEMPLATE;
  }
}
