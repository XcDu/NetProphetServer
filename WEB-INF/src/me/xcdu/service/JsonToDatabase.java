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
    ArrayList<String> applicationList = null;
    try {
      applicationList = new AccessManager().getApplicationList();
      if (!applicationList.contains(targetApplication)) {
        accsessManager.createApplicationTable(targetApplication);
      }

      StringBuffer jsonBuffer = new StringBuffer();
      try {
        BufferedReader bufferedReader = request.getReader();
        String tmpLine = null;
        while ((tmpLine = bufferedReader.readLine()) != null) {
          jsonBuffer.append(tmpLine);
        }
        String json = jsonBuffer.toString();
        try {
          int begin = json.indexOf('{'), end = json.indexOf('}');
          while (begin >= 0 && end >= 0) {
            Gson gson = new Gson();
            String jsonObject = json.substring(begin, end + 1);
            json = json.substring(end + 1);
            String[] typeJudgementArray = jsonObject.split(",");
            if (typeJudgementArray.length == HttpRequestInfo.class
                .getDeclaredFields().length) {
              accsessManager.insertHttpRequestInfo(targetApplication,
                  gson.fromJson(jsonObject, HttpRequestInfo.class));
            } else if (typeJudgementArray.length == NetworkInfo.class
                .getDeclaredFields().length) {
              accsessManager.insertNetworkInfo(targetApplication,
                  gson.fromJson(jsonObject, NetworkInfo.class));
            } else {
              throw new ServletException("Json converts error");
            }
            begin = json.indexOf('{');
            end = json.indexOf('}');
          }

        } catch (ParseException e) {
          throw new IOException("Error parsing JSON request string");
        } catch (Exception e) {
          e.printStackTrace();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
  }
}
