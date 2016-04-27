package me.xcdu.service;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

public class LogServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  public LogServlet() {
    super();
    // TODO Auto-generated constructor stub
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    System.out.println("Log4JInitServlet initializing...");
    String log4jLocation = config.getInitParameter("log4j-properties-location");

    ServletContext sc = config.getServletContext();

    if (log4jLocation == null) {
      System.err.println("*** No log4j-properties-location variable found,"
          + " initialized by BasicConfigurator");
      BasicConfigurator.configure();
    } else {
      String webAppPath = sc.getRealPath("/");
      String log4jProp = webAppPath + log4jLocation;
      File yoMamaYesThisSaysYoMama = new File(log4jProp);
      if (yoMamaYesThisSaysYoMama.exists()) {
        System.out.println("use: " + log4jProp + " to initializing...");
        PropertyConfigurator.configure(log4jProp);
      } else {
        System.err.println("*** " + log4jProp
            + " file not found. Initialized by BasicConfigurator");
        BasicConfigurator.configure();
      }
    }
    super.init(config);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {}

  @Override
  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {}

}
