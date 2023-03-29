package com.librbary.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.DefaultHttpLogWriter;
import org.zalando.logbook.DefaultSink;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.SplunkHttpLogFormatter;

import static org.zalando.logbook.Conditions.exclude;
import static org.zalando.logbook.Conditions.requestTo;

/**
 * This class is used to add logbook configuration
 */

@Configuration
public class LogbookConfig {

  // skipping the health endpoint from logging the request, response and headers
  @Bean
  Logbook excludeHttpLog() {
    return Logbook.builder().sink(new DefaultSink(new SplunkHttpLogFormatter(), new DefaultHttpLogWriter()))
        .condition(exclude(requestTo("/actuator/**"), requestTo("/swagger-ui/**"))).build();
  }
}
