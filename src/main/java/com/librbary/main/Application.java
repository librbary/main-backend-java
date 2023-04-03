package com.librbary.main;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.slf4j.LoggerFactory.getLogger;

@SpringBootApplication
public class Application {

  private static final Logger logger = getLogger(Application.class);
  public static void main(String[] args) throws UnknownHostException {
    logger.info("Start in main application {} on host {}", Application.class,
        InetAddress.getLocalHost().getHostAddress());
    SpringApplication.run(Application.class);
  }

}
