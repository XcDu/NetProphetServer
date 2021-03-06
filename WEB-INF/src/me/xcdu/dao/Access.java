package me.xcdu.dao;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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

    System.out
        .println("TopViewedSqlStatement:\n" + preparedStatement.toString());

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

    System.out
        .println("TopErrorSqlStatement:\n" + preparedStatement.toString());

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
  // NetworkInfo obj = new NetworkInfo(rs.getLong("reqID"),rs.getString("userID"),
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
    Set<String> urlSet = new HashSet<String>();
    try {
      while (rs.next()) {
        String url = rs.getString("url");
        if (url.indexOf('?') >= 0) {
          url = url.substring(0, url.indexOf('?'));
        }
        urlSet.add(url);
      }
    } catch (Exception e) {
      // TODO: handle exception
    }
    System.out.println(urlSet.toString());
    ArrayList<UrlIndex> urlIndexList = new ArrayList<UrlIndex>();
    SortedMap<String, Integer> urlGroupMap = new TreeMap<String, Integer>();
    // Current position of urlIndexList
    Integer curPos = 0;
    try {
      Iterator<String> it = urlSet.iterator();
      while (it.hasNext()) {
        URL tmpUrl = new URL(it.next());
        String host = tmpUrl.getHost();
        if (!urlGroupMap.containsKey(host)) {
          urlIndexList.add(new UrlIndex());
          urlIndexList.get(curPos).setDomain(host);
          urlIndexList.get(curPos).addSubList(tmpUrl.getPath());
          urlGroupMap.put(host, curPos++);
        } else {
          urlIndexList.get(urlGroupMap.get(host)).addSubList(tmpUrl.getPath());
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    // System.out.println(urlIndexList.toString());
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

  public String getTargetTable(Connection connection, String targetApplication)
      throws SQLException {
    String targetTable = null;
    PreparedStatement preparedStatement = connection.prepareStatement(
        sqlStatementBuilder.getTargetTableSql(targetApplication));
    ResultSet rs = preparedStatement.executeQuery();
    try {
      ArrayList<Integer> idList = new ArrayList<Integer>();
      while (rs.next()) {
        idList.add(rs.getInt("id"));
      }
      if (idList.size() > 1) {
        System.out.println(idList);
        throw new Exception("MultiResult when fetch the target table");
      } else if (idList.size() == 1) {
        targetTable = "app" + idList.get(0);
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return targetTable;
  }

  public String createApplicationTables(Connection connection,
      String targetApplication) throws SQLException {
    String targetTable = null;
    try {
      connection.createStatement().execute(
          sqlStatementBuilder.InsertToApplicationMapSql(targetApplication));
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }

    targetTable = getTargetTable(connection, targetApplication);
    if (targetTable == null) {
      throw new SQLException("Null target table");
    }

    try {
      connection.createStatement()
          .execute(sqlStatementBuilder.createHttpRequestinfoTable(targetTable));
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    try {
      connection.createStatement()
          .execute(sqlStatementBuilder.createNetworkinfoTable(targetTable));
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return targetTable;
  }

  public boolean insertHttpRequestInfo(Connection connection,
      String targetTable, HttpRequestInfo info) throws SQLException {
    PreparedStatement statement = connection.prepareStatement(
        "insert into " + targetTable + "_httprequestinfo values( null,"
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
      System.out.println(targetTable + ":\n" + info.toString());
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return false;
  }

  public boolean insertNetworkInfo(Connection connection, String targetTable,
      NetworkInfo info) throws SQLException {
    PreparedStatement statement = connection.prepareStatement("insert into "
        + targetTable + "_networkinfo values( null, " + info.getReqID() + ","
        + "'" + info.getUserID() + "'," + "'" + info.getNetworkType() + "',"
        + "'" + info.getNetworkName() + "'," + info.getWIFISignalLevel() + ","
        + info.getCellSignalLevel() + "," + info.getMCC() + "," + info.getMNC()
        + "," + info.getLAC() + "," + info.getFirstMileLatency() + ","
        + info.getFirstMilePacketLossRate() + ");");
    try {
      return statement.execute();
    } catch (SQLException e) {
      System.out.println(targetTable + ":\n" + info.toString());
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return false;
  }


}
