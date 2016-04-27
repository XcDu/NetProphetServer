package me.xcdu.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import me.xcdu.dto.AccessManager;
import me.xcdu.po.HttpRequestInfo;
import me.xcdu.po.NetworkInfo;

public class JsonToDatabase extends HttpServlet {
  public static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(JsonToDatabase.class);
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
    Map<String, String> applicationsMap = null;
    String targetTable = null;
    try {
      applicationsMap = new AccessManager().getApplicationsMap();
      if (!applicationsMap.containsKey(targetApplication)) {
        targetTable = accsessManager.createApplicationTable(targetApplication,
            applicationsMap.size());
        if (targetTable == null) {
          throw new Exception(
              "Create application table:" + targetTable + " failed.");
        }
      } else {
        targetTable = applicationsMap.get(targetApplication);
      }
      try {
        System.out.println("here1");
        BufferedReader bufferedReader = null;
        if (request.getHeader("Content-Encoding") != null && request
            .getHeader("Content-Encoding").toLowerCase().contains("gzip")) {
          System.out.println("uncompressing...");
          bufferedReader = new BufferedReader(new InputStreamReader(
              new GZIPInputStream(request.getInputStream()),
              Charset.forName("UTF-8")));
        } else {
          bufferedReader = request.getReader();
        }
        String strLine = "";
        StringBuffer sb = new StringBuffer();
        while ((strLine = bufferedReader.readLine()) != null) {
          sb.append(strLine);
        }
        try {
          // int begin = json.indexOf('{'), end = json.indexOf('}');
          // while (begin >= 0 && end >= 0) {
          // Gson gson = new Gson();
          // String jsonObject = json.substring(begin, end + 1);
          // json = json.substring(end + 1);
          // String[] typeJudgementArray = jsonObject.split(",");
          // if (typeJudgementArray.length == HttpRequestInfo.class
          // .getDeclaredFields().length) {
          // accsessManager.insertHttpRequestInfo(targetTable,
          // gson.fromJson(jsonObject, HttpRequestInfo.class));
          // } else if (typeJudgementArray.length == NetworkInfo.class
          // .getDeclaredFields().length) {
          // accsessManager.insertNetworkInfo(targetTable,
          // gson.fromJson(jsonObject, NetworkInfo.class));
          // } else {
          // throw new ServletException("Json converts error");
          // }
          // begin = json.indexOf('{');
          // end = json.indexOf('}');
          String json = sb.toString();
          // System.out.println(json);
          Gson gson = new Gson();
          HttpRequestInfo[] reqRS = null;
          NetworkInfo[] netRS = null;
          try {
            reqRS = gson.fromJson(json, HttpRequestInfo[].class);
            if (reqRS == null || reqRS.length == 0
                || reqRS[0].getUrl() == null) {
              reqRS = null;
              netRS = gson.fromJson(json, NetworkInfo[].class);
            }
          } catch (Exception e) {
            logger.error(e.getMessage());
          }

          // Now we have the objects.
          if (reqRS != null) {
            System.out.println("length of reqRS: " + reqRS.length);
            for (HttpRequestInfo obj : reqRS) {
              // do something here
              logger.info(
                  "  " + obj.getUrl() + " delay:" + obj.getOverallDelay());
              accsessManager.insertHttpRequestInfo(targetTable, obj);
            }
          } else {
            // System.out.println("length of reqRS: 0");
          }

          if (netRS != null) {
            System.out.println("length of netRS: " + netRS.length);
            for (NetworkInfo obj : netRS) {
              // do something here
              logger.info("  networkType:" + obj.getNetworkType());
              accsessManager.insertNetworkInfo(targetTable, obj);
            }
          } else {
            // System.out.println("length of netRS: 0");
          }
        } catch (Exception e) {
          e.printStackTrace();
          logger.error(e.getMessage());
        }
      } catch (Exception e) {
        e.printStackTrace();
        logger.error(e.getMessage());
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
  }
}
