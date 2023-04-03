package com.librbary.main.bdd.api.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Service to open DB connection and execute queries
 */

@Component
public class DBService {

  private static final Logger LOGGER = getLogger(DBService.class);

  private Connection connection = null;

  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.username}")
  private String user;

  @Value("${spring.datasource.password}")
  private String password;

  /**
   * Opens the DB Connection.
   *
   * @throws SQLException
   *             SQLException if thrown when opening DB Connection.
   */
  public void openConnection() throws SQLException {
    LOGGER.info("Initialize DB connection");

    connection = DriverManager.getConnection(url, user, password);

    assertNotNull(connection, "DB Connection object not initialized");
    assertFalse(connection.isClosed(), "DB Connection not initialized");
  }

  /**
   * Closes the DB Connection.
   *
   * @throws SQLException
   *             SQL Exception if thrown when closing the DB Connection.
   */
  public void closeConnection() throws SQLException {
    LOGGER.info("Disconnecting from DB");

    this.connection.close();
    this.connection = null;
  }

  /**
   * Executes the update query and returns the number of rows updated.
   *
   * @param query
   *            Update query to execute
   * @return the number of rows updated by the query execution
   * @throws SQLException
   *             SQL Exception if thrown during the query validation & execution.
   */
  public int executeUpdate(final String query) throws SQLException {
    try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      return preparedStatement.executeUpdate();
    }
  }

  /**
   * Executes the query and returns the ResultSet.
   *
   * @param query
   *            search query to execute
   * @return the ResultSet object which stores the result by the query execution
   * @throws SQLException
   *             SQL Exception if thrown during the query validation & execution.
   */
  public ResultSet executeQuery(final String query) throws SQLException {
    final PreparedStatement preparedStatement = connection.prepareStatement(query);
    return preparedStatement.executeQuery();

  }

}
