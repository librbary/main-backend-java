package com.librbary.main.controller;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/v1/users")
public class UsersController {
  private static final Logger logger = getLogger(UsersController.class);

}
