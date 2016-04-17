package me.xcdu.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import me.xcdu.dto.AccessManager;

public class ChartServlet extends HttpServlet {
  public static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String app = request.getParameter("app");
    System.out.println("application:" + app);
    String type = request.getParameter("type");
    if (type.equalsIgnoreCase("overview")) {
      try {
        Gson gson = new Gson();
        String respJson =
            gson.toJson(new AccessManager().getOverviewCharts(app));
        // System.out.println(respJson);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(respJson);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (type.equalsIgnoreCase("urllist")) {
      try {
        String targetUrl = request.getParameter("targetUrl");
        Gson gson = new Gson();
        String respJson =
            gson.toJson(new AccessManager().getUrlListCharts(app, targetUrl));
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(respJson);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      throw new ServletException("type error");
    }
  }

  @Override
  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    throw new ServletException("Request Error");
  }
}
