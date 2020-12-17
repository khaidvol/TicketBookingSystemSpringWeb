package com.epam.jgmp.controller;

import com.epam.jgmp.config.TbsApplicationConfig;
import com.epam.jgmp.dao.model.Event;
import com.epam.jgmp.facade.BookingFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TbsApplicationConfig.class)
@WebMvcTest(EventController.class)
public class EventControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private BookingFacade bookingFacade;

  private Event event;
  private List<Event> events;

  private ObjectMapper objectMapper;
  private ObjectNode objectNode;

  @Before
  public void setUp() throws ParseException {

    objectMapper = new ObjectMapper();
    objectNode = objectMapper.createObjectNode();

    long id = 1L;
    String title = "TestEvent";
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String dateInString = "2020-12-12";
    Date date = formatter.parse(dateInString);

    event = new Event(title, date);
    event.setId(id);
    events = Collections.singletonList(event);

    Mockito.when(bookingFacade.getEventById(id)).thenReturn(event);
    Mockito.when(
            bookingFacade.getEventsForDay(
                ArgumentMatchers.any(Date.class),
                ArgumentMatchers.any(Integer.class),
                ArgumentMatchers.any(Integer.class)))
        .thenReturn(events);
    Mockito.when(bookingFacade.getEventsByTitle(title, 1, 1)).thenReturn(events);
    Mockito.when(bookingFacade.createEvent(ArgumentMatchers.any(Event.class))).thenReturn(event);
    Mockito.when(bookingFacade.updateEvent(ArgumentMatchers.any(Event.class))).thenReturn(event);
    Mockito.when(bookingFacade.deleteEvent(id)).thenReturn(true);
  }

  @Test
  public void getEventById() throws Exception {
    this.mockMvc
        .perform(get("/event/id?id={id}", event.getId()))
        .andExpect(status().isOk())
        .andExpect(view().name("eventTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", event));
  }

  @Test
  public void getEventsByTitle() throws Exception {
    String title = "TestEvent";
    this.mockMvc
        .perform(get("/event/title?title={title}&pageSize=1&pageNum=1", event.getTitle()))
        .andExpect(status().isOk())
        .andExpect(view().name("eventTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", events));
  }

  @Test
  public void getEventsForDay() throws Exception {
    String day = "2020-12-12";
    this.mockMvc
        .perform(get("/event/day?day={day}&pageSize=1&pageNum=1", day))
        .andExpect(status().isOk())
        .andExpect(view().name("eventTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", events));
  }

  @Test
  public void createEvent() throws Exception {

    objectNode.put("title", event.getTitle());
    objectNode.put("day", event.getDate().toString());

    this.mockMvc
        .perform(
            post("/event/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectNode.toString()))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  public void updateEvent() throws Exception {

    objectNode.put("id", event.getId());
    objectNode.put("title", event.getTitle());
    objectNode.put("day", event.getDate().toString());

    this.mockMvc
        .perform(
            put("/event/update/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectNode.toString()))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  public void deleteEvent() throws Exception {
    this.mockMvc
        .perform(delete("/event/delete/{id}", event.getId()))
        .andExpect(status().isOk())
        .andReturn();
  }
}
