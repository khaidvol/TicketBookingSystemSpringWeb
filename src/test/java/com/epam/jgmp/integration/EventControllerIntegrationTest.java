package com.epam.jgmp.integration;

import com.epam.jgmp.config.TbsApplicationConfig;
import com.epam.jgmp.facade.BookingFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.hasToString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TbsApplicationConfig.class)
@WebAppConfiguration
@WebMvcTest()
public class EventControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private BookingFacade bookingFacade;

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

    String title = "Test1Event";
    String day = "2020-12-12";

    this.mockMvc
        .perform(post("/event/new?title={title}&day={day}", title, day))
        .andExpect(status().isOk())
        .andExpect(view().name("eventTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(
            model()
                .attribute(
                    "result",
                    hasToString(
                        "Event created: "
                            .concat(
                                bookingFacade.getEventsByTitle(title, 1, 1).get(0).toString()))));
  }

  @Test
  public void updateEvent() throws Exception {
    long id = 2L;
    String title = "Test2Event";
    String day = "2020-12-12";
    this.mockMvc
        .perform(put("/event/update?id={id}&title={title}&day={day}", id, title, day))
        .andExpect(status().isOk())
        .andExpect(view().name("eventTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(
            model()
                .attribute(
                    "result",
                    hasToString(
                        "Event updated: ".concat(bookingFacade.getEventById(id).toString()))));
  }

  @Test
  public void deleteEvent() throws Exception {
    long id = 3L;
    this.mockMvc
        .perform(delete(String.format("/event/delete?id=%s", id)))
        .andExpect(status().isOk())
        .andExpect(view().name("eventTemplate"))
        .andExpect(model().attributeExists("result"))
        .andExpect(
            model().attribute("result", hasToString(String.format("Event #%s deleted: true", id))));
  }
}
