package com.librbary.main.bdd.api.config.e1;

import com.librbary.main.Application;
import com.librbary.main.bdd.api.hooks.DatabaseHooks;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.sql.SQLException;

@ActiveProfiles("bdd-e1")
@CucumberContextConfiguration
@SpringBootTest(classes = Application.class)
@ContextConfiguration(initializers = SpringGlue.PostgresDataSourceInitializer.class)
@Slf4j
public class SpringGlue {

  private static final String DB_PASSWORD = System.getProperty("db.password");

  @Getter
  private static ApplicationContext applicationContext;

  @Autowired
  private DatabaseHooks databaseHooks;

  @Before(value = "@E1Database")
  public void initializeDB() throws SQLException {
    databaseHooks.initializeDB();
  }

  @After(value = "@E1Database")
  public void tearDown() throws SQLException {
    databaseHooks.cleanupConnection();
  }

  public static class PostgresDataSourceInitializer
      implements
        ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
          "spring.datasource.url=" + applicationContext.getEnvironment().getProperty("spring.datasource.url"),
          "spring.datasource.username="
              + applicationContext.getEnvironment().getProperty("spring.datasource.username"),
          "spring.datasource.password=" + DB_PASSWORD);

      SpringGlue.applicationContext = applicationContext;
    }
  }
}
