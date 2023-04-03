package com.librbary.main.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "junit:target/cucumber-reports/cucumber.xml",
    "html:target/cucumber-reports/cucumber.html",
    "json:target/cucumber-reports/cucumber.json"}, tags = "not @PostDeploymentTests", glue = {
        "com.librbary.main.bdd.api.config.e0",
        "com.librbary.main.bdd.api.stepdefs"}, features = "classpath:features")
public class CucumberIT {
}
