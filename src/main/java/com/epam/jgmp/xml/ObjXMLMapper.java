package com.epam.jgmp.xml;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ObjXMLMapper {

  @Value("${app.tickets}")
  private String filename;

  private Jaxb2Marshaller marshaller;

  public void setMarshaller(Jaxb2Marshaller marshaller) {
    this.marshaller = marshaller;
  }

  // Converting XML to an object graph (unmarshalling)
  public List<XMLTicket> xmlToObj() throws IOException {

    XMLTicketListContainer ticketList;

    try (FileInputStream is = new FileInputStream(filename)) {
      ticketList = (XMLTicketListContainer) this.marshaller.unmarshal(new StreamSource(is));
    }

    return ticketList.getTicketList();
  }
}
