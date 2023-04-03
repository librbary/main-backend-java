package com.librbary.main.bdd.api.hooks;

import com.librbary.main.bdd.api.services.DBService;
import io.cucumber.java.Scenario;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Long.MAX_VALUE;
import static java.text.MessageFormat.format;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliterator;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.junit.jupiter.api.Assertions.*;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class DatabaseHooks {

  private static final Logger LOGGER = getLogger(DatabaseHooks.class);

  private static final String QUERY_DELIMETER_REGEX = ";[\\r\\n]*";
  private static final String TAG_SEPARATOR = ":";
  private static final String TEST_DATA_SETUP_TAG = "@TestDataSetup";
  private static final String TEAR_DOWN_TEST_DATA_TAG = "@TearDownTestData";

  private final DBService dbService;

  @Autowired
  public DatabaseHooks(DBService dbService) {
    this.dbService = dbService;
  }

  public void initializeDB() throws SQLException {
    dbService.openConnection();
  }

  public void cleanupConnection() throws SQLException {
    dbService.closeConnection();
  }

  public void setup(final Scenario scenario) throws URISyntaxException, IOException {
    LOGGER.info("Setting up the test data");
    final Optional<String> optionalFilePath = findSetupScriptFilePath(scenario);

    if (optionalFilePath.isPresent()) {
      executeQueriesFromFile(optionalFilePath.get());
    }
  }

  public void tearDown(final Scenario scenario) throws URISyntaxException, IOException {
    LOGGER.info("Tearing down the test data");
    final String filePath = findTeardownScriptFilePath(scenario);

    executeQueriesFromFile(filePath);
  }

  private Optional<String> findSetupScriptFilePath(final Scenario scenario) {
    return scenario.getSourceTagNames().stream().filter(value -> value.contains(TEST_DATA_SETUP_TAG))
        .map(value -> substringAfter(value, TAG_SEPARATOR)).findFirst();
  }

  private String findTeardownScriptFilePath(final Scenario scenario) {
    return scenario.getSourceTagNames().stream().filter(value -> value.contains(TEAR_DOWN_TEST_DATA_TAG))
        .map(value -> substringAfter(value, TAG_SEPARATOR)).findFirst().orElseThrow(
            () -> new IllegalArgumentException("Missing tag with format @TearDownTestData:<FileName>"));
  }

  private void executeQueriesFromFile(final String fileName) throws URISyntaxException, IOException {
    assertNotNull(fileName, "Query script file name cannot be null");

    final URL resource = getClass().getClassLoader().getResource(fileName);
    assertNotNull(resource, "Query script file cannot be found: " + fileName);

    final Scanner scanner = new Scanner(Paths.get(resource.toURI()));
    scanner.useDelimiter(QUERY_DELIMETER_REGEX);

    try (final Stream<String> queries = stream(scanner)) {
      queries.filter(StringUtils::isNotBlank).forEach(query -> {
        try {
          LOGGER.info(format("query: {0}", query));
          final int updatedRows = dbService.executeUpdate(query);
          assertTrue(updatedRows > 0, "No records updated");
        } catch (final SQLException exception) {
          LOGGER.error("Error in preparing query: ", exception);
          fail("Query execution failed due to error: " + exception.getMessage());
        }
      });
    }
  }

  private static Stream<String> stream(final Scanner scanner) {
    final Spliterator<String> spliterator = spliterator(scanner, MAX_VALUE, ORDERED | NONNULL);
    return StreamSupport.stream(spliterator, false).onClose(scanner::close);
  }
}
