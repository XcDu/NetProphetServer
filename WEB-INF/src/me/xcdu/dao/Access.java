package me.xcdu.dao;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import me.xcdu.bo.ChartBuilder;
import me.xcdu.bo.ChartBuilder.TopErrorRateChart;
import me.xcdu.bo.ChartBuilder.TopViewedChart;
import me.xcdu.bo.UrlIndex;
import me.xcdu.bo.UrlListCharts;
import me.xcdu.dao.SqlStatementBuilder.NetworkType;
import me.xcdu.po.HttpRequestInfo;
import me.xcdu.po.NetworkInfo;

public class Access {
  public static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(Access.class);
  private SqlStatementBuilder sqlStatementBuilder;

  public Access() {
    sqlStatementBuilder = new SqlStatementBuilder();
  }

  public TopViewedChart getTopViewedChart(Connection connection,
      String targetApplication, NetworkType networkType) throws SQLException {
    TopViewedChart chart = new ChartBuilder().createTopViewdChart();
    PreparedStatement preparedStatement =
        connection.prepareStatement(sqlStatementBuilder
            .getOverviewTopViewedSql(targetApplication, networkType));
    ResultSet rs = preparedStatement.executeQuery();
    try {
      while (rs.next()) {
        chart.categories.add(rs.getString("url"));
        chart.data.add(rs.getInt("count"));
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return chart;
  }

  public TopErrorRateChart getTopErrorRateChart(Connection connection,
      String targetTable, NetworkType networkType) throws SQLException {
    TopErrorRateChart chart = new ChartBuilder().createTopErrorRateChart();
    PreparedStatement preparedStatement =
        connection.prepareStatement(sqlStatementBuilder
            .getOverviewTopErrorRateSql(targetTable, networkType));
    ResultSet rs = preparedStatement.executeQuery();
    try {
      while (rs.next()) {
        chart.url.add(rs.getString("url"));
        chart.errorRate.add(rs.getDouble("errorRate"));
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return chart;
  }

  public UrlListCharts getUrlListCharts(Connection connection,
      String targetTable, String targetUrl, NetworkType networkType)
      throws SQLException {
    System.out.println("without redirection part");
    PreparedStatement preparedStatement =
        connection.prepareStatement(sqlStatementBuilder
            .getUrlListChartsSql(targetTable, targetUrl, networkType));
    System.out.println(preparedStatement.toString());
    ResultSet rsWithoutRedirection = preparedStatement.executeQuery();
    System.out.println("redirection part");
    preparedStatement = connection.prepareStatement(sqlStatementBuilder
        .getUrlListRedirectionChartsSql(targetTable, targetUrl, networkType));
    System.out.println(preparedStatement.toString());
    ResultSet rsWithRedirection = preparedStatement.executeQuery();
    UrlListCharts charts = new UrlListCharts();
    charts.setAllCharts(
        castResultSetToHttpRequestInfoArray(rsWithoutRedirection),
        castResultSetToHttpRequestInfoArray(rsWithRedirection));
    return charts;
  }



  // TODU(xcdu): to be refined
  private ArrayList<HttpRequestInfo> castResultSetToHttpRequestInfoArray(
      ResultSet rs) {
    ArrayList<HttpRequestInfo> infoArray = new ArrayList<HttpRequestInfo>();
    try {
      int cnt = 0;
      while (rs.next()) {
        System.out.println(cnt++);
        HttpRequestInfo obj = new HttpRequestInfo(rs.getLong("reqID"),
            rs.getString("url"), rs.getString("method"), rs.getString("userID"),
            rs.getLong("prevReqID"), rs.getLong("nextReqID"),
            rs.getLong("startTime"), rs.getLong("endTime"),
            rs.getLong("overallDelay"), rs.getLong("dnsDelay"),
            rs.getLong("connDelay"), rs.getLong("handshakeDelay"),
            rs.getLong("tlsDelay"), rs.getLong("reqWriteDelay"),
            rs.getLong("serverDelay"), rs.getLong("TTFBDelay"),
            rs.getLong("respTransDelay"), rs.getBoolean("useConnCache"),
            rs.getBoolean("useDNSCache"), rs.getBoolean("useRespCache"),
            rs.getLong("respSize"), rs.getInt("HTTPCode"), rs.getInt("reqSize"),
            rs.getBoolean("isFailedRequest"), rs.getString("errorMsg"),
            rs.getString("detailedErrorMsg"), rs.getLong("transID"),
            rs.getInt("transType"));
        infoArray.add(obj);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return infoArray;
  }

  // private ArrayList<NetworkInfo> castResultSetToNetworkInfo(ResultSet rs) {
  // ArrayList<NetworkInfo> infoArray = new ArrayList<NetworkInfo>();
  // try {
  // while (rs.next()) {
  // NetworkInfo obj = new NetworkInfo(rs.getLong("reqID"),
  // rs.getString("networkType"), rs.getString("networkName"),
  // rs.getInt("WIFISignalLevel"), rs.getInt("cellSignalLevel"),
  // rs.getInt("MCC"), rs.getInt("MNC"), rs.getInt("LAC"),
  // rs.getInt("firstMileLatency"),
  // rs.getInt("firstMilePacketLossRate"));
  // infoArray.add(obj);
  // }
  // } catch (Exception e) {
  // e.printStackTrace();
  // }
  // return infoArray;
  // }

  public ArrayList<UrlIndex> getUrlIndexList(Connection connection,
      String targetTable, String sortBy) throws SQLException {
    PreparedStatement statement = connection.prepareStatement(
        sqlStatementBuilder.getUrlIndexSql(targetTable, sortBy));
    System.out.println("Statement:" + statement.toString());
    ResultSet rs = statement.executeQuery();
    ArrayList<UrlIndex> urlIndexList = new ArrayList<UrlIndex>();
    SortedMap<String, Integer> urlIndexMap = new TreeMap<String, Integer>();
    // Current position of urlIndexList
    Integer curPos = 0;
    try {
      while (rs.next()) {
        URL tmpUrl = new URL(rs.getString("url"));
        String domain = tmpUrl.getHost();
        domain = domain.startsWith("www.") ? domain.substring(4) : domain;
        if (!urlIndexMap.containsKey(domain)) {
          urlIndexList.add(new UrlIndex());
          urlIndexList.get(curPos).setDomain(domain);
          urlIndexList.get(curPos).addSubList(tmpUrl.toString());
          urlIndexMap.put(domain, curPos++);
        } else {
          urlIndexList.get(urlIndexMap.get(domain))
              .addSubList(tmpUrl.toString());
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return urlIndexList;
  }

  public ArrayList<String> getApplicationsList(Connection connection)
      throws SQLException {
    PreparedStatement statement = connection
        .prepareStatement(sqlStatementBuilder.getApplicationsListSql());
    ResultSet rs = statement.executeQuery();
    ArrayList<String> applicationsList = new ArrayList<String>();
    try {
      while (rs.next()) {
        applicationsList.add(rs.getString("application_name"));
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return applicationsList;
  }

  public Map<String, String> getApplicationsMap(Connection connection)
      throws SQLException {
    PreparedStatement statement = connection
        .prepareStatement(sqlStatementBuilder.getApplicationsMapSql());
    ResultSet rs = statement.executeQuery();
    Map<String, String> applicationsMap = new TreeMap<String, String>();
    try {
      while (rs.next()) {
        applicationsMap.put(rs.getString("application_name"),
            rs.getString("table_name"));
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return applicationsMap;
  }

  public String createApplicationTables(Connection connection,
      String targetApplication, int id) throws SQLException {
    String targetTable = "app" + id;
    try {
      connection.createStatement().execute(sqlStatementBuilder
          .InsertToApplicationMapSql(targetApplication, targetTable));
      connection.createStatement()
          .execute(sqlStatementBuilder.createHttpRequestinfoTable(targetTable));
      connection.createStatement()
          .execute(sqlStatementBuilder.createNetworkinfoTable(targetTable));
    } catch (Exception e) {
      logger.error(e.getMessage());
      return null;
    }
    return targetTable;
  }

  public boolean insertHttpRequestInfo(Connection connection,
      String targetTable, HttpRequestInfo info) throws SQLException {
    PreparedStatement statement = connection.prepareStatement(
        "insert into " + targetTable + "_httprequestinfo values("
            + info.getReqID() + "," + "'" + info.getUrl() + "'," + "'"
            + info.getMethod() + "'," + "'" + info.getUserID() + "',"
            + info.getPrevReqID() + "," + info.getNextReqID() + ","
            + info.getStartTime() + "," + info.getEndTime() + ","
            + info.getOverallDelay() + "," + info.getDnsDelay() + ","
            + info.getConnDelay() + "," + info.getHandshakeDelay() + ","
            + info.getTlsDelay() + "," + info.getReqWriteDelay() + ","
            + info.getServerDelay() + "," + info.getTTFBDelay() + ","
            + info.getRespTransDelay() + "," + info.isUseConnCache() + ","
            + info.isUseDNSCache() + "," + info.isUseRespCache() + ","
            + info.getRespSize() + "," + info.getHTTPCode() + ","
            + info.getReqSize() + "," + info.isFailedRequest() + "," + "'"
            + info.getErrorMsg() + "'," + "'" + info.getDetailedErrorMsg()
            + "'," + info.getTransID() + "," + info.getTransType() + ");");
    try {
      return statement.execute();
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }
    return false;
  }

  public boolean insertNetworkInfo(Connection connection, String targetTable,
      NetworkInfo info) throws SQLException {
    PreparedStatement statement = connection.prepareStatement("insert into "
        + targetTable + "_networkinfo values(" + info.getReqID() + "," + "'"
        + info.getNetworkType() + "'," + "'" + info.getNetworkName() + "',"
        + info.getWIFISignalLevel() + "," + info.getCellSignalLevel() + ","
        + info.getMCC() + "," + info.getMNC() + "," + info.getLAC() + ","
        + info.getFirstMileLatency() + "," + info.getFirstMilePacketLossRate()
        + ");");
    try {
      return statement.execute();
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }
    return false;
  }


}
