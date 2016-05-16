package me.xcdu.service;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import me.xcdu.dto.AccessManager;

public class ChartServlet extends HttpServlet {
  public static final long serialVersionUID = 1L;

  private AccessManager accessManager = new AccessManager();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }

  @Override
  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    String app = request.getParameter("app");
    System.out.println("application:" + app);
    // TODO(xcdu): to be refined;
    try {
      String targetTable = accessManager.getTargetTable(app);
      String type = request.getParameter("type");
      if (type.equalsIgnoreCase("overview")) {
        try {
          Gson gson = new Gson();
          String respJson =
              gson.toJson(accessManager.getOverviewCharts(targetTable));
          // System.out.println(respJson);
          response.setContentType("application/json");
          response.setCharacterEncoding("utf-8");
          response.getWriter().write(respJson);
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else if (type.equalsIgnoreCase("urllist")) {
        try {
          URL targetUrl = new URL(request.getParameter("targetUrl"));
          Gson gson = new Gson();
          // Gson gson =
          // new GsonBuilder().serializeSpecialFloatingPointValues().create();
          String respJson = gson.toJson(accessManager
              .getUrlListCharts(targetTable, targetUrl.toString()));
          response.setContentType("application/json");
          response.setCharacterEncoding("utf-8");
          response.getWriter().write(respJson);
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        throw new ServletException("type error");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
