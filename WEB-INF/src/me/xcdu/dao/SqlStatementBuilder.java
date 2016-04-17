package me.xcdu.dao;

// TODO(xcdu): To be refined;
public class SqlStatementBuilder {
  private static final int OVERVIEW_TOP_VIEWED_NUMBER = 20;
  private static final int OVERVIEW_TOP_ERROR_RATE_NUMBER = 10;

  public enum NetworkType {
    BOTH(""), WIFI("networkinfo.networkType='WIFI'"), MOBILE(
        "networkinfo.networkType='MOBILE'");
    private String statementNetworkType;

    private NetworkType(String statement) {
      statementNetworkType = statement;
    }

    private String getStatement(String targetApplication) {
      if (this.statementNetworkType.length() == 0)
        return "";
      return "and " + targetApplication + "_" + statementNetworkType;
    }
  }

  // TODO(xcdu): filter the data with redirection
  public String getOverviewTopViewedSql(String targetApplication,
      NetworkType networkType) {
    String hTable = targetApplication + "_httprequestinfo";
    String nTable = targetApplication + "_networkinfo";
    String statement = "select url, count(url) as count" + " from " + hTable
        + " inner join " + nTable + " on " + hTable + ".reqID = " + nTable
        + ".reqID" + " " + networkType.getStatement(targetApplication) + " "
        + " group by url" + " order by count desc" + " limit "
        + OVERVIEW_TOP_VIEWED_NUMBER;
    return statement;
  }

  public String getOverviewTopErrorRateSql(String targetApplication,
      NetworkType networkType) {
    String hTable = targetApplication + "_httprequestinfo";
    String nTable = targetApplication + "_networkinfo";
    String statement = "select t_cnt.url, error_cnt/cnt as errorRate"
        + " from ( select url , count(url) as error_cnt" + " from " + hTable
        + " inner join " + nTable + " on " + hTable + ".reqID=" + nTable
        + ".reqID" + " and " + hTable + ".isFailedRequest=true" + " group by "
        + hTable + ".url) as t_error_cnt"
        + " inner join ( select url, count(url) as cnt" + " from " + hTable
        + " inner join " + nTable + "" + " on " + hTable + ".reqID=" + nTable
        + ".reqID" + " " + networkType.getStatement(targetApplication) + " "
        + " group by " + hTable + ".url) as t_cnt"
        + " on t_error_cnt.url=t_cnt.url"
        + " order by errorRate desc, t_cnt.cnt desc, t_cnt.url asc" + " limit "
        + OVERVIEW_TOP_ERROR_RATE_NUMBER;
    return statement;
  }

  public String getUrlListChartsSql(String targetApplication, String targetUrl,
      NetworkType networkType) {
    String hTable = targetApplication + "_httprequestinfo";
    String nTable = targetApplication + "_networkinfo";
    String statement = "select * from " + hTable + " inner join " + nTable + ""
        + " on " + hTable + ".reqID=" + nTable + ".reqID" + " and " + hTable
        + ".nextReqID=0" + " and " + hTable + ".url=\"" + targetUrl + "\" "
        + networkType.getStatement(targetApplication) + " " + " and " + hTable
        + ".useConnCache=false" + " order by " + hTable + ".startTime asc";
    return statement;
  }

  public String getUrlListRedirectionChartsSql(String targetApplication,
      String targetUrl, NetworkType networkType) {
    String hTable = targetApplication + "_httprequestinfo";
    String nTable = targetApplication + "_networkinfo";
    String statement = "select * from " + hTable + " inner join " + nTable + ""
        + " on " + hTable + ".reqID=" + nTable + ".reqID" + " and " + hTable
        + ".nextReqID<>0" + " and " + hTable + ".url=\"" + targetUrl + "\" "
        + networkType.getStatement(targetApplication) + " " + " and " + hTable
        + ".useConnCache=false";
    return statement;
  }

  public String getUrlIndexSql(String targetApplication, String sortBy) {
    String hTable = targetApplication + "_httprequestinfo";
    String nTable = targetApplication + "_networkinfo";
    String statement =
        "select distinct url" + " from " + hTable + " inner join " + nTable + ""
            + " on " + hTable + ".reqID=" + nTable + ".reqID" + " and " + hTable
            + ".useConnCache=false" + " order by " + sortBy;
    return statement;
  }

  public String getApplicationListSql() {
    String statement = "show tables like '%_httprequestinfo'";
    return statement;
  }
}
