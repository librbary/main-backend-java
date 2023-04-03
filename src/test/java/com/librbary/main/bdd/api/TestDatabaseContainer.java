package com.librbary.main.bdd.api;

import com.github.dockerjava.api.command.InspectContainerResponse;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;

@Slf4j
public class TestDatabaseContainer extends PostgreSQLContainer<TestDatabaseContainer> {

  private static final String IMAGE_VERSION = "postgres:latest";
  private static TestDatabaseContainer container;

  private TestDatabaseContainer() {
    super(IMAGE_VERSION);
  }

  @SuppressWarnings({"java:S2095"})
  public static TestDatabaseContainer getInstance() {
    if (container == null) {
      container = new TestDatabaseContainer().withInitScript("create_tablespace.sql").withUsername("postgres")
          .withPassword("postgres");
    }

    return container;
  }

  @SuppressWarnings({"java:S2142"})
  @Override
  protected void containerIsStarted(InspectContainerResponse containerInfo) {
    log.info("Creating custom tablespace in testcontainer");
    try {
      log.debug("M=containerIsStarted, creating database namespace folders and setting permissions");
      ExecResult echo = execInContainer("mkdir", "/var/lib/postgresql/tblspc_data");
      Assertions.assertThat(echo.getExitCode()).isZero();
      echo = execInContainer("chown", "-R", "postgres.postgres", "/var/lib/postgresql/tblspc_data");
      Assertions.assertThat(echo.getExitCode()).isZero();
    } catch (IOException | InterruptedException e) {
      log.error("Failed to create custom tablespace", e);
    }

    super.containerIsStarted(containerInfo);
  }
}
