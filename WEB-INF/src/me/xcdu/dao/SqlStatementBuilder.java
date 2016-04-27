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

    private String getStatement(String targetTable) {
      if (this.statementNetworkType.length() == 0)
        return "";
      return "and " + targetTable + "_" + statementNetworkType;
    }
  }

  // TODO(xcdu): filter the data with redirection
  public String getOverviewTopViewedSql(String targetTable,
      NetworkType networkType) {
    String hTable = targetTable + "_httprequestinfo";
    String nTable = targetTable + "_networkinfo";
    String statement =
        "select url, count(url) as count" + " from " + hTable + " inner join "
            + nTable + " on " + hTable + ".reqID = " + nTable + ".reqID" + " "
            + networkType.getStatement(targetTable) + " " + " group by url"
            + " order by count desc" + " limit " + OVERVIEW_TOP_VIEWED_NUMBER;
    return statement;
  }

  public String getOverviewTopErrorRateSql(String targetTable,
      NetworkType networkType) {
    String hTable = targetTable + "_httprequestinfo";
    String nTable = targetTable + "_networkinfo";
    String statement = "select t_cnt.url, error_cnt/cnt as errorRate"
        + " from ( select url , count(url) as error_cnt" + " from " + hTable
        + " inner join " + nTable + " on " + hTable + ".reqID=" + nTable
        + ".reqID" + " and " + hTable + ".isFailedRequest=true" + " group by "
        + hTable + ".url) as t_error_cnt"
        + " inner join ( select url, count(url) as cnt" + " from " + hTable
        + " inner join " + nTable + "" + " on " + hTable + ".reqID=" + nTable
        + ".reqID" + " " + networkType.getStatement(targetTable) + " "
        + " group by " + hTable + ".url) as t_cnt"
        + " on t_error_cnt.url=t_cnt.url"
        + " order by errorRate desc, t_cnt.cnt desc, t_cnt.url asc" + " limit "
        + OVERVIEW_TOP_ERROR_RATE_NUMBER;
    return statement;
  }

  public String getUrlListChartsSql(String targetTable, String targetUrl,
      NetworkType networkType) {
    String hTable = targetTable + "_httprequestinfo";
    String nTable = targetTable + "_networkinfo";
    String statement = "select * from " + hTable + " inner join " + nTable + ""
        + " on " + hTable + ".reqID=" + nTable + ".reqID" + " and " + hTable
        + ".nextReqID=0" + " and " + hTable + ".url=\"" + targetUrl + "\" "
        + networkType.getStatement(targetTable) + " " + " and " + hTable
        + ".useConnCache=false" + " order by " + hTable + ".startTime asc";
    return statement;
  }

  public String getUrlListRedirectionChartsSql(String targetTable,
      String targetUrl, NetworkType networkType) {
    String hTable = targetTable + "_httprequestinfo";
    String nTable = targetTable + "_networkinfo";
    String statement = "select * from " + hTable + " inner join " + nTable + ""
        + " on " + hTable + ".reqID=" + nTable + ".reqID" + " and " + hTable
        + ".nextReqID<>0" + " and " + hTable + ".url=\"" + targetUrl + "\" "
        + networkType.getStatement(targetTable) + " " + " and " + hTable
        + ".useConnCache=false";
    return statement;
  }

  public String getUrlIndexSql(String targetTable, String sortBy) {
    String hTable = targetTable + "_httprequestinfo";
    String nTable = targetTable + "_networkinfo";
    String statement =
        "select distinct url" + " from " + hTable + " inner join " + nTable + ""
            + " on " + hTable + ".reqID=" + nTable + ".reqID" + " and " + hTable
            + ".useConnCache=false" + " order by " + sortBy;
    return statement;
  }

  public String getApplicationsListSql() {
    String statement = "select distinct application_name from applications_map "
        + "order by application_name asc";
    return statement;
  }

  public String getApplicationsMapSql() {
    String statement =
        "select application_name, table_name from applications_map";
    return statement;
  }

  public String InsertToApplicationMapSql(String targetApplication,
      String targetTable) {
    String statement = "insert ignore into applications_map values(\""
        + targetApplication + "\", \"" + targetTable + "\");";
    System.out.println(statement);
    return statement;
  }

  public String createHttpRequestinfoTable(String targetTable) {
    String statement = "CREATE TABLE IF NOT EXISTS " + targetTable
        + "_httprequestinfo (" + " reqID bigint(20) NOT NULL AUTO_INCREMENT,"
        + " url varchar(2083) DEFAULT NULL,"
        + " method varchar(256) DEFAULT NULL,"
        + " userID varchar(256) DEFAULT NULL,"
        + " prevReqID bigint(20) DEFAULT NULL,"
        + " nextReqID bigint(20) DEFAULT NULL,"
        + " startTime bigint(20) DEFAULT NULL,"
        + " endTime bigint(20) DEFAULT NULL,"
        + " overallDelay bigint(20) DEFAULT NULL,"
        + " dnsDelay bigint(20) DEFAULT NULL,"
        + " connDelay bigint(20) DEFAULT NULL,"
        + " handshakeDelay bigint(20) DEFAULT NULL,"
        + " tlsDelay bigint(20) DEFAULT NULL,"
        + " reqWriteDelay bigint(20) DEFAULT NULL,"
        + " serverDelay bigint(20) DEFAULT NULL,"
        + " TTFBDelay bigint(20) DEFAULT NULL,"
        + " respTransDelay bigint(20) DEFAULT NULL,"
        + " useConnCache tinyint(1) DEFAULT NULL,"
        + " useDNSCache tinyint(1) DEFAULT NULL,"
        + " useRespCache tinyint(1) DEFAULT NULL,"
        + " respSize bigint(20) DEFAULT NULL,"
        + " HTTPCode int(11) DEFAULT NULL," + " reqSize int(11) DEFAULT NULL,"
        + " isFailedRequest tinyint(1) DEFAULT NULL,"
        + " errorMsg text, detailedErrorMsg text,"
        + " transID bigint(20) DEFAULT NULL,"
        + " transType int(11) DEFAULT NULL," + " PRIMARY KEY (reqID))"
        + " ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;";

    return statement;
  }

  public String createNetworkinfoTable(String targetTable) {
    String statement = " CREATE TABLE IF NOT EXISTS " + targetTable
        + "_networkinfo (" + " reqID bigint(20) NOT NULL AUTO_INCREMENT,"
        + " networkType varchar(256) DEFAULT NULL,"
        + " networkName varchar(256) DEFAULT NULL,"
        + " WIFISignalLevel int(11) DEFAULT NULL,"
        + " cellSignalLevel int(11) DEFAULT NULL, MCC int(11) DEFAULT NULL,"
        + " MNC int(11) DEFAULT NULL," + " LAC int(11) DEFAULT NULL,"
        + " firstMileLatency int(11) DEFAULT NULL,"
        + " firstMilePacketLossRate int(11) DEFAULT NULL,"
        + " PRIMARY KEY (reqID))"
        + " ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;";
    return statement;
  }
}
