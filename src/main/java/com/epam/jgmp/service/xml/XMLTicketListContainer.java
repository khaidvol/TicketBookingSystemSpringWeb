package com.epam.jgmp.service.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "tickets")
public class XMLTicketListContainer {

  private List<XMLTicket> ticketList;

  @XmlElement(name = "ticket")
  public List<XMLTicket> getTicketList() {
    return ticketList;
  }

  public void setTicketList(List<XMLTicket> ticketList) {
    this.ticketList = ticketList;
  }
}
