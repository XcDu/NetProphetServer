package me.xcdu.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
  private String connectionURL = "jdbc:mysql://localhost:3306/netprophet";
  private final String username = "root";
  private final String password = "23621169qqo";
  private Connection connection = null;

  public Database() {
    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      connection =
          DriverManager.getConnection(connectionURL, username, password);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Connection getConnection() {
    return connection;
  }
}
