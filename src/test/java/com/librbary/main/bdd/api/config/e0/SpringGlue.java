package com.librbary.main.bdd.api.config.e0;

import com.librbary.main.Application;
import com.librbary.main.bdd.api.TestDatabaseContainer;
import com.librbary.main.bdd.api.hooks.DatabaseHooks;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Testcontainers
@ActiveProfiles("bdd")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@ContextConfiguration(initializers = SpringGlue.DockerPostgresDataSourceInitializer.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class SpringGlue {

  @Container
  private static final TestDatabaseContainer postgresContainer = TestDatabaseContainer.getInstance();

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private DatabaseHooks databaseHooks;

  @Before(order = 1)
  public void initializeRestAssuredMockMvcWebApplicationContext() {
    RestAssuredMockMvc.mockMvc(mockMvc);
    RestAssuredMockMvc.config();
  }

  @Before(value = "@Database")
  public void initializeDB(final Scenario scenario) throws SQLException, URISyntaxException, IOException {
    databaseHooks.initializeDB();
    databaseHooks.setup(scenario);
  }

  @After(value = "@Database")
  public void tearDown(final Scenario scenario) throws URISyntaxException, IOException, SQLException {
    log.info("Tearing down the test data");
    databaseHooks.tearDown(scenario);
    databaseHooks.cleanupConnection();
  }

  public static class DockerPostgresDataSourceInitializer
      implements
        ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      postgresContainer.start();
      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
          "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
          "spring.datasource.username=" + postgresContainer.getUsername(),
          "spring.datasource.password=" + postgresContainer.getPassword());
    }
  }
}
