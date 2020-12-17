package com.epam.jgmp;

import com.epam.jgmp.controller.EventControllerTest;
import com.epam.jgmp.controller.TicketControllerTest;
import com.epam.jgmp.controller.TicketsPdfControllerTest;
import com.epam.jgmp.controller.UserControllerTest;
import com.epam.jgmp.dao.EventDaoTest;
import com.epam.jgmp.dao.TicketDaoTest;
import com.epam.jgmp.dao.UserDaoTest;
import com.epam.jgmp.integration.EventControllerIntegrationTest;
import com.epam.jgmp.integration.IntegrationTest;
import com.epam.jgmp.integration.TicketControllerIntegrationTest;
import com.epam.jgmp.integration.UserControllerIntegrationTest;
import com.epam.jgmp.service.EventServiceImplTest;
import com.epam.jgmp.service.TicketServiceImplTest;
import com.epam.jgmp.service.UserServiceImplTest;
import com.epam.jgmp.service.pdf.TicketsPdfBuilderTest;
import com.epam.jgmp.service.xml.ObjXMLMapperTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  UserDaoTest.class,
  EventDaoTest.class,
  TicketDaoTest.class,
  UserServiceImplTest.class,
  EventServiceImplTest.class,
  TicketServiceImplTest.class,
  IntegrationTest.class,
  ObjXMLMapperTest.class,
  TicketsPdfBuilderTest.class,
  UserControllerTest.class,
  EventControllerTest.class,
  TicketControllerTest.class,
  TicketsPdfControllerTest.class,
  UserControllerIntegrationTest.class,
  EventControllerIntegrationTest.class,
  TicketControllerIntegrationTest.class,
})
public class TestAll {}
