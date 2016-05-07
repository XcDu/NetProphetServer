package me.xcdu.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
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

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    throw new ServletException("error");
  }

  @Override
  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    AccessManager accessManager = new AccessManager();
    String targetApplication = request.getHeader("X-Application-Name");
    try {
      String targetTable = accessManager.getTargetTable(targetApplication);
      if (targetTable == null) {
        targetTable = accessManager.createApplicationTable(targetApplication);
      }
      System.out.println("JsonToDatabase: targetapp: " + targetApplication
          + " targetTable: " + targetTable);
      try {
        BufferedReader bufferedReader = null;
        if (request.getHeader("Content-Encoding") != null && request
            .getHeader("Content-Encoding").toLowerCase().contains("gzip")) {
          System.out.println("JsonToDatabase servlet: uncompressing...");
          bufferedReader = new BufferedReader(new InputStreamReader(
              new GZIPInputStream(request.getInputStream()),
              Charset.forName("UTF-8")));
        } else {
          bufferedReader =
              new BufferedReader(new InputStreamReader(request.getInputStream(),
                  Charset.forName("UTF-8")));
        }
        String strLine = "";
        StringBuffer sb = new StringBuffer();
        while ((strLine = bufferedReader.readLine()) != null) {
          sb.append(strLine);
        }
        try {
          String json = sb.toString();
          System.out.println("received post data:\n" + json);
          Gson gson = new Gson();
          HttpRequestInfo[] reqRS = null;
          NetworkInfo[] netRS = null;
          try {
            reqRS = gson.fromJson(json, HttpRequestInfo[].class);
            for (int i = 0; i < reqRS.length - 1; ++i) {
              for (int j = i + 1; j < reqRS.length; ++j) {
                if (reqRS[i].getReqID() == reqRS[j].getReqID()
                    && reqRS[i].getUserID() == reqRS[i].getUserID())
                  System.out.println(
                      "\n Two data duplicate in " + targetApplication + ":\n"
                          + reqRS[i].toString() + "\n" + reqRS[j].toString());
              }
            }
            if (reqRS == null || reqRS.length == 0
                || reqRS[0].getUrl() == null) {
              reqRS = null;
              netRS = gson.fromJson(json, NetworkInfo[].class);
              // Test(xcdu)
              for (int i = 0; i < netRS.length - 1; ++i) {
                for (int j = i + 1; j < netRS.length; ++j) {
                  if (netRS[i].getReqID() == netRS[j].getReqID()
                      && netRS[i].getUserID() == netRS[i].getUserID())
                    System.out.println(
                        "\n Two data duplicate in " + targetApplication + ":\n"
                            + netRS[i].toString() + "\n" + netRS[j].toString());
                }
              }
            }
          } catch (Exception e) {
            logger.error(e.getMessage());
          }

          // Now we have the objects.
          if (reqRS != null) {
            System.out.println(
                "[" + targetApplication + "]:length of reqRS: " + reqRS.length);
            for (HttpRequestInfo obj : reqRS) {
              // do something here
              // logger.info(obj.toString());
              accessManager.insertHttpRequestInfo(targetTable, obj);
            }
          } else {
            // System.out.println("length of reqRS: 0");
          }

          if (netRS != null) {
            System.out.println(
                "[" + targetApplication + "]:length of netRS: " + netRS.length);
            for (NetworkInfo obj : netRS) {
              // do something here
              // logger.info(obj.toString());
              accessManager.insertNetworkInfo(targetTable, obj);
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
