package me.xcdu.dto;

import java.sql.Connection;
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

  public ArrayList<UrlIndex> getUrlIndex(String sortBy) throws Exception {
    return access.getUrlIndexList(connection, sortBy);
  }

  public Charts<OverviewCharts> getOverviewCharts() throws Exception {
    Charts<OverviewCharts> overviewCharts = new Charts<OverviewCharts>();
    overviewCharts.setWifi(new OverviewCharts(
        access.getTopViewedChart(connection, NetworkType.WIFI),
        access.getTopErrorRateChart(connection, NetworkType.WIFI)));
    overviewCharts.setMobile(new OverviewCharts(
        access.getTopViewedChart(connection, NetworkType.MOBILE),
        access.getTopErrorRateChart(connection, NetworkType.MOBILE)));
    return overviewCharts;
  }

  public Charts<UrlListCharts> getUrlListCharts(String targetUrl)
      throws Exception {
    Charts<UrlListCharts> urlListCharts = new Charts<UrlListCharts>();
    urlListCharts.setWifi(
        access.getUrlListCharts(connection, targetUrl, NetworkType.WIFI));
    urlListCharts.setMobile(
        access.getUrlListCharts(connection, targetUrl, NetworkType.MOBILE));
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



  public boolean insertNetworkInfo(NetworkInfo info) throws Exception {
    return access.insertNetworkInfo(connection, info);
  }

  public boolean insertHttpRequestInfo(HttpRequestInfo info) throws Exception {
    return access.insertHttpRequestInfo(connection, info);
  }
}
