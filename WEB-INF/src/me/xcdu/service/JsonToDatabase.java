package me.xcdu.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import me.xcdu.dto.AccessManager;
import me.xcdu.po.HttpRequestInfo;
import me.xcdu.po.NetworkInfo;

public class JsonToDatabase extends HttpServlet {
  public static final long serialVersionUID = 1L;
  private AccessManager accsessManager = new AccessManager();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    throw new ServletException("error");
  }

  @Override
  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    String targetApplication = request.getHeader("X-Application-Name");
    System.out.println(targetApplication);
    ArrayList<String> applicationList = null;
    try {
      applicationList = new AccessManager().getApplicationList();
      if (!applicationList.contains(targetApplication)) {
        new AccessManager().createApplicationTable(targetApplication);
      }

      StringBuffer jsonBuffer = new StringBuffer();
      String tmpLine = null;
      try {
        BufferedReader bufferedReader = request.getReader();
        while ((tmpLine = bufferedReader.readLine()) != null)
          jsonBuffer.append(tmpLine);
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        int splitIndex = jsonBuffer.indexOf("}") + 1;
        String httpRequestInfoJson =
            jsonBuffer.toString().substring(1, splitIndex);
        String networkInfoJson = jsonBuffer.toString().substring(splitIndex + 1,
            jsonBuffer.length() - 1);
        Gson gson = new Gson();
        HttpRequestInfo httpRequestInfo =
            gson.fromJson(httpRequestInfoJson, HttpRequestInfo.class);
        NetworkInfo networkInfo =
            gson.fromJson(networkInfoJson, NetworkInfo.class);
        accsessManager.insertHttpRequestInfo(targetApplication,
            httpRequestInfo);
        accsessManager.insertNetworkInfo(targetApplication, networkInfo);
        response.getWriter().write("Succeed");
      } catch (ParseException e) {
        throw new IOException("Error parsing JSON request string");
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
  }
}
