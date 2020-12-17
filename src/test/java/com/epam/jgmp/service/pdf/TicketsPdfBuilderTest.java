package com.epam.jgmp.service.pdf;

import com.epam.jgmp.config.TestConfig;
import com.epam.jgmp.dao.model.Ticket;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketsPdfBuilderTest {

  ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfig.class);
  TicketsPdfBuilder ticketsPdfBuilder = applicationContext.getBean(TicketsPdfBuilder.class);

  @Test
  public void buildPdfDocument() throws Exception {

    Document document = new Document();
    Map<String, Object> model = new HashMap<>();
    PdfWriter pdfWriter = Mockito.mock(PdfWriter.class);
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    Ticket ticket = Mockito.mock(Ticket.class);

    Mockito.when(ticket.getUserId()).thenReturn(1L);
    Mockito.when(ticket.getEventId()).thenReturn(1L);
    Mockito.when(ticket.getPlace()).thenReturn(1);
    Mockito.when(ticket.getCategory()).thenReturn(Ticket.Category.PREMIUM);

    List<Ticket> tickets = new ArrayList<>();
    tickets.add(ticket);
    model.put("tickets", tickets);

    document.open();
    ticketsPdfBuilder.buildPdfDocument(model, document, pdfWriter, request, response);
    document.close();

    Assert.assertNotNull(document);
  }
}
