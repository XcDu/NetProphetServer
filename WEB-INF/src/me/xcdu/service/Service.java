package me.xcdu.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import me.xcdu.dto.AccessManager;
import me.xcdu.po.HttpRequestInfo;
import me.xcdu.po.NetworkInfo;

public class Service extends HttpServlet {
  public static final long serialVersionUID = 1L;
  private AccessManager accsessManager = new AccessManager();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    String title = "DatabaseDebug";
    String docType = "<!doctype html public \"-//w3c//dtd html 4.0 "
        + "transitional//en\">\n";
    ArrayList<HttpRequestInfo> httpRequestInfoList =
        new ArrayList<HttpRequestInfo>();
    ArrayList<NetworkInfo> networkInfoList = new ArrayList<NetworkInfo>();
    SortedMap<String, ArrayList<String>> urlIndex =
        new TreeMap<String, ArrayList<String>>();
    try {
      AccessManager accessManager = new AccessManager();
      httpRequestInfoList = accessManager.getHttpRequestInfo("");
      networkInfoList = accessManager.getNetworkInfo();
      urlIndex = accessManager.getUrlIndex();

    } catch (Exception e) {
      e.printStackTrace();
    }
    out.print(docType + "<html>\n" + "<head><title>" + title
        + "</title></head>\n" + "<body bgcolor=\"#f0f0f0\">\n"
        + "<h1 align=\"center\">" + title + "</h1>\n");
    // out.println("<p>TimingTable<br /></p>");
    // for (int i = 0; i < httpRequestInfoList.size(); ++i) {
    // out.print("<ul>\n" + httpRequestInfoList.get(i).toString() + "\n</ul>");
    // }
    // out.println("<p>NetworkingDataTable<br /></p>");
    // for (int i = 0; i < networkInfoList.size(); ++i) {
    // out.print("<ul>\n" + networkInfoList.get(i).toString() + "\n</ul>\n");
    // }
    // out.println("<p>urlIndex</p>");
    // for (String s : urlIndex.keySet()) {
    // out.println(s + "<br />");
    // for (int i = 0; i < urlIndex.get(s).size(); ++i) {
    // out.println(urlIndex.get(s).get(i) + "<br />");
    // }
    // out.println("<br />");
    // }
    out.println(urlIndex);
  }

  @Override
  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
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
      accsessManager.insertHttpRequestInfo(httpRequestInfo);
      accsessManager.insertNetworkInfo(networkInfo);
    } catch (ParseException e) {
      throw new IOException("Error parsing JSON request string");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
