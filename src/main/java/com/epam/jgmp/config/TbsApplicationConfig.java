package com.epam.jgmp.config;

import com.epam.jgmp.xml.ObjXMLMapper;
import com.epam.jgmp.xml.XMLTicket;
import com.epam.jgmp.xml.XMLTicketListContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
@ComponentScan(basePackages = "com.epam.jgmp")
@PropertySource("classpath:application.properties")
public class TbsApplicationConfig extends WebMvcConfigurationSupport {

  private final ApplicationContext applicationContext;

  public TbsApplicationConfig(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /** PDF view bundle */
  @Bean
  public ResourceBundleViewResolver resourceBundleViewResolver() {
    ResourceBundleViewResolver viewResolver = new ResourceBundleViewResolver();
    viewResolver.setOrder(2);
    viewResolver.setBasename("views");
    return viewResolver;
  }

  /** Thymeleaf configuration */
  @Bean
  public SpringResourceTemplateResolver templateResolver() {
    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(this.applicationContext);
    templateResolver.setPrefix("/WEB-INF/views/");
    templateResolver.setSuffix(".html");
    return templateResolver;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver());
    templateEngine.setEnableSpringELCompiler(true);
    return templateEngine;
  }

  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
    resolver.setOrder(1);
    resolver.setTemplateEngine(templateEngine());
    registry.viewResolver(resolver);
  }

  /** XML to Object Mapping configuration */

  @Bean
  public Jaxb2Marshaller jaxb2Marshaller() {
    Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
    jaxb2Marshaller.setClassesToBeBound(XMLTicket.class, XMLTicketListContainer.class);
    return jaxb2Marshaller;
  }

  @Bean
  public ObjXMLMapper objXMLMapper() {
    ObjXMLMapper objXMLMapper = new ObjXMLMapper();
    objXMLMapper.setMarshaller(jaxb2Marshaller());
    return objXMLMapper;
  }
}
