package me.xcdu.dto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import me.xcdu.bo.Charts;
import me.xcdu.bo.OverviewCharts;
import me.xcdu.bo.UrlIndex;
import me.xcdu.bo.UrlListCharts;
import me.xcdu.dao.Access;
import me.xcdu.dao.Database;
import me.xcdu.dao.SqlStatementBuilder.NetworkType;
import me.xcdu.po.HttpRequestInfo;
import me.xcdu.po.NetworkInfo;

public class AccessManager {
  private Connection connection;
  private Access access;

  public AccessManager() {
    connection = new Database().getConnection();
    access = new Access();
  }

  public ArrayList<UrlIndex> getUrlIndex(String targetApplication,
      String sortBy) throws Exception {
    return access.getUrlIndexList(connection, targetApplication, sortBy);
  }

  public Charts<OverviewCharts> getOverviewCharts(String targetApplication)
      throws Exception {
    Charts<OverviewCharts> overviewCharts = new Charts<OverviewCharts>();
    overviewCharts.setWifi(new OverviewCharts(
        access.getTopViewedChart(connection, targetApplication,
            NetworkType.WIFI),
        access.getTopErrorRateChart(connection, targetApplication,
            NetworkType.WIFI)));
    overviewCharts.setMobile(new OverviewCharts(
        access.getTopViewedChart(connection, targetApplication,
            NetworkType.MOBILE),
        access.getTopErrorRateChart(connection, targetApplication,
            NetworkType.MOBILE)));
    return overviewCharts;
  }

  public Charts<UrlListCharts> getUrlListCharts(String targetApplication,
      String targetUrl) throws Exception {
    Charts<UrlListCharts> urlListCharts = new Charts<UrlListCharts>();
    urlListCharts.setWifi(access.getUrlListCharts(connection, targetApplication,
        targetUrl, NetworkType.WIFI));
    urlListCharts.setMobile(access.getUrlListCharts(connection,
        targetApplication, targetUrl, NetworkType.MOBILE));
    return urlListCharts;
  }


  // public ArrayList<NetworkInfo> getNetworkInfo() throws Exception {
  // return access.getNetworkInfo(connection);
  // }
  //
  // public ArrayList<HttpRequestInfo> getHttpRequestInfo(String targetUrl)
  // throws Exception {
  // return access.getHttpRequestInfo(connection, targetUrl,
  // ErrorFilterType.BOTH, "", SortType.DISABLE);
  // }
  //
  // public ArrayList<HttpRequestInfo> getHttpRequestInfo(String targetUrl,
  // ErrorFilterType errorFilter, String sortBy, SortType sortType)
  // throws Exception {
  // return access.getHttpRequestInfo(connection, targetUrl, errorFilter, sortBy,
  // sortType);
  // }
  public ArrayList<String> getApplicationList() throws SQLException {
    return access.getApplicationList(connection);
  }


  public boolean insertNetworkInfo(String targetApplication, NetworkInfo info)
      throws Exception {
    return access.insertNetworkInfo(connection, targetApplication, info);
  }

  public boolean insertHttpRequestInfo(String targetApplication,
      HttpRequestInfo info) throws Exception {
    return access.insertHttpRequestInfo(connection, targetApplication, info);
  }

}
