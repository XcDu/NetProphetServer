package me.xcdu.service;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import me.xcdu.bo.UrlIndex;
import me.xcdu.dto.AccessManager;



public class UrlIndexServlet extends HttpServlet {
  public static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(UrlIndexServlet.class);

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    AccessManager accessManager = new AccessManager();
    try {
      String app = request.getParameter("app");
      System.out.println("application:" + app);
      String targetTable = accessManager.getTargetTable(app);
      String sortBy = request.getParameter("sortBy");
      System.out.println("Sortby:" + sortBy);
      ArrayList<UrlIndex> urlIndex =
          accessManager.getUrlIndex(targetTable, sortBy);
      String respJson = new Gson().toJson(urlIndex);
      // System.out.println(respJson);
      response.setContentType("application/json");
      response.setCharacterEncoding("utf-8");
      response.getWriter().write(respJson);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

  }

  @Override
  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
}
