package me.xcdu.dao;

// TODO(xcdu): To be refined;
public class SqlStatementBuilder {
  private static final int OVERVIEW_TOP_VIEWED_NUMBER = 20;
  private static final int OVERVIEW_TOP_ERROR_RATE_NUMBER = 10;

  public enum NetworkType {
    BOTH(""), WIFI("and networkinfo.networkType='WIFI'"), MOBILE(
        "and networkinfo.networkType='MOBILE'");
    private String statementNetworkType;

    private NetworkType(String statement) {
      statementNetworkType = statement;
    }

    private String getStatement() {
      return statementNetworkType;
    }
  }

  // TODO(xcdu): filter the data with redirection
  public String getOverviewTopViewedSql(NetworkType networkType) {
    String statement = "select url, count(url) as count"
        + " from httprequestinfo inner join networkinfo"
        + " on httprequestinfo.reqID = networkinfo.reqID" + " "
        + networkType.getStatement() + " " + " group by url"
        + " order by count desc" + " limit " + OVERVIEW_TOP_VIEWED_NUMBER;
    return statement;
  }

  public String getOverviewTopErrorRateSql(NetworkType networkType) {
    String statement = "select t_cnt.url, error_cnt/cnt as errorRate"
        + " from ( select url , count(url) as error_cnt"
        + " from httprequestinfo inner join networkinfo"
        + " on httprequestinfo.reqID=networkinfo.reqID"
        + " and httprequestinfo.isFailedRequest=true"
        + " and networkinfo.networkType='MOBILE'"
        + " group by httprequestinfo.url) as t_error_cnt"
        + " inner join ( select url, count(url) as cnt"
        + " from httprequestinfo inner join networkinfo"
        + " on httprequestinfo.reqID=networkinfo.reqID" + " "
        + networkType.getStatement() + " "
        + " group by httprequestinfo.url) as t_cnt"
        + " on t_error_cnt.url=t_cnt.url"
        + " order by errorRate desc, t_cnt.cnt desc, t_cnt.url asc" + " limit "
        + OVERVIEW_TOP_ERROR_RATE_NUMBER;
    return statement;
  }

  public String getUrlListChartsSql(String targetUrl, NetworkType networkType) {
    String statement = "select * from httprequestinfo inner join networkinfo"
        + " on httprequestinfo.reqID=networkinfo.reqID"
        + " and httprequestinfo.nextReqID=0" + " and httprequestinfo.url=\""
        + targetUrl + "\" " + networkType.getStatement() + " "
        + " and httprequestinfo.useConnCache=false"
        + " order by httprequestinfo.startTime asc";
    return statement;
  }

  public String getUrlListRedirectionChartsSql(String targetUrl,
      NetworkType networkType) {
    String statement = "select * from httprequestinfo inner join networkinfo"
        + " on httprequestinfo.reqID=networkinfo.reqID"
        + " and httprequestinfo.nextReqID<>0" + " and httprequestinfo.url=\""
        + targetUrl + "\" " + networkType.getStatement() + " "
        + " and httprequestinfo.useConnCache=false";
    return statement;
  }

  public String getUrlIndexSql(String sortBy) {
    String statement =
        "select distinct url" + " from httprequestinfo inner join networkinfo"
            + " on httprequestinfo.reqID=networkinfo.reqID"
            + " and httprequestinfo.useConnCache=false" + " order by " + sortBy;
    return statement;
  }
}
