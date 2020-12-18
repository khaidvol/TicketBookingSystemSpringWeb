package com.epam.jgmp.integration;

import com.epam.jgmp.config.TbsApplicationConfig;
import com.epam.jgmp.facade.BookingFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TbsApplicationConfig.class)
@WebAppConfiguration
@WebMvcTest()
public class EventControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private BookingFacade bookingFacade;

  private ObjectMapper objectMapper;
  private ObjectNode objectNode;

  @Before
  public void setUp() throws ParseException {

    objectMapper = new ObjectMapper();
    objectNode = objectMapper.createObjectNode();
  }

  @Test
  public void getEventById() throws Exception {
    long id = 1L;
    this.mockMvc
        .perform(get("/event/id?id={id}", id))
        .andExpect(status().isOk())
        .andExpect(view().name("eventTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", bookingFacade.getEventById(id)));
  }

  @Test
  public void getEventsByTitle() throws Exception {
    String title = "Disco";
    this.mockMvc
        .perform(get("/event/title?title={title}&pageSize=1&pageNum=1", title))
        .andExpect(status().isOk())
        .andExpect(view().name("eventTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", bookingFacade.getEventsByTitle(title, 1, 1)));
  }

  @Test
  public void getEventsForDay() throws Exception {

    String day = "2020-06-28";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = dateFormat.parse(day);

    this.mockMvc
        .perform(get("/event/day?day={day}&pageSize=1&pageNum=1", day))
        .andExpect(status().isOk())
        .andExpect(view().name("eventTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(model().attribute("result", bookingFacade.getEventsForDay(date, 1, 1)));
  }

  @Test
  public void createEvent() throws Exception {

    objectNode.put("title", "Test1Event");
    objectNode.put("date", "2020-06-28");

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

    objectNode.put("id", 2L);
    objectNode.put("title", "Test2Event");
    objectNode.put("date", "2020-06-28");

    this.mockMvc
        .perform(
            put("/event/update/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectNode.toString()))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  public void deleteEvent() throws Exception {
    this.mockMvc.perform(delete("/event/delete/{id}", 3L)).andExpect(status().isOk()).andReturn();
  }
}
