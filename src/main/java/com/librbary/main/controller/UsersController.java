package com.librbary.main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/v1/users")
@Tag(name = "liBRBary endpoints")
public class UsersController {
  private static final Logger logger = getLogger(UsersController.class);

  @Operation(summary = "Test endpoint")
  @GetMapping(value = "/test")
  public String getTest() {
    return "Test";
  }

}
