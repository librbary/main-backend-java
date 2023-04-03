package com.librbary.main.bdd.api.stepdefs;

import groovy.json.JsonException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.internal.path.json.JsonPrettifier;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.Files.readAllBytes;
import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.*;

public class RestAPIStepDefinition {

  private final RestApiRequest restApiRequest = new RestApiRequest();
  private MockMvcResponse restApiResponse;
  private final RequestSpecification httpRequest = RestAssured.given();
  private Response httpResponse;

  private static final String BEARER_TOKEN = "Token2132414353525254534535";
  private static final String HEADER_NAME = "headername";
  private static final String TEST_FOLDER = "src/test/resources/";
  private static final List<String> ALLOWED_HTTP_METHODS = Arrays.asList(HttpMethod.POST.name(),
      HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.PATCH.name());

  private static boolean isValidJson(final String json) {
    try {
      JsonPrettifier.prettifyJson(json);
    } catch (JsonException exception) {
      return false;
    }

    return true;
  }

  @Given("^The endpoint is already configured$")
  public void theEndpointIsAlreadyConfigured() {
    // Syntactic sugar method used in liBRBary Cucumber bridge for report
    // documentation.
  }

  @When("^API endpoint is (.+)$")
  public void apiEndpointIs(final String endpoint) {
    restApiRequest.setEndpoint(endpoint);
  }

  @When("^auth token is generated for TEST$")
  public void retrieveJwtTokenForConsumer() {
    final HttpHeaders authHeader = new HttpHeaders();
    authHeader.setBearerAuth(BEARER_TOKEN);
    final String token = Objects.requireNonNull(authHeader.get(HttpHeaders.AUTHORIZATION)).stream().findFirst()
        .orElseThrow(IllegalArgumentException::new);
    final Map<String, String> auth = new HashMap<>();
    auth.put(HttpHeaders.AUTHORIZATION, token);

    restApiRequest.addHeaders(auth);
  }

  @When("^headers as$")
  public void headersAs(final Map<String, String> headers) {
    final Map<String, String> headers1 = headers.entrySet().stream()
        .filter(entrySet -> !(entrySet.getKey().equals(HEADER_NAME)))
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    restApiRequest.addHeaders(headers1);
  }

  @When("^HTTP Method is (.+)$")
  public void httpMethod(final String httpMethod) {
    if (ALLOWED_HTTP_METHODS.contains(httpMethod)) {
      restApiRequest.setHttpMethod(httpMethod);
    } else {
      fail("Expected [GET, POST, PUT, PATCH]. HTTP Method value supplied is invalid: " + httpMethod);
    }
  }

  @When("^payload from file (.+)$")
  public void payloadFromFile(final String filePath) throws IOException {
    final String payload = getFromFile(filePath);
    restApiRequest.setPayload(payload);
  }

  @When("^executed$")
  public void executed() {
    final String endpoint = restApiRequest.getEndpoint();
    final String httpMethod = restApiRequest.getHttpMethod();

    assertNotNull(endpoint, "API endpoint must be set through 'API endpoint is' step");
    assertNotNull(httpMethod, "HTTP method must be set through 'HTTP Method is' step");

    final MockMvcRequestSpecification request = RestAssuredMockMvc.given();

    final Map<String, String> headers = restApiRequest.getHeaders();
    if (headers != null) {
      for (final Map.Entry<String, String> headerEntry : headers.entrySet()) {
        request.header(headerEntry.getKey(), headerEntry.getValue());
      }
    }

    if (httpMethod.equals(HttpMethod.GET.name())) {
      restApiResponse = request.get(endpoint);
    } else {
      final String payload = restApiRequest.getPayload();
      assertNotNull(payload, "Request payload must be set before");

      request.body(payload);
      if (httpMethod.equals(HttpMethod.POST.name())) {
        restApiResponse = request.post(endpoint);
      } else if (httpMethod.equals(HttpMethod.PUT.name())) {
        restApiResponse = request.put(endpoint);
      } else if (httpMethod.equals(HttpMethod.PATCH.name())) {
        restApiResponse = request.patch(endpoint);
      }
    }
  }

  @Then("^HTTP status code should be (.+)$")
  public void httpStatusCodeShouldBe(final int value) {
    assertNotNull(restApiResponse, "Response not available. This step should call after 'executed' step");
    assertEquals(value, restApiResponse.getStatusCode(), "HTTP status code mismatch");
  }

  private String getFromFile(final String path) throws IOException {
    String fileContent = "";
    if (!path.isEmpty()) {
      URL url = this.getClass().getClassLoader().getResource(path);
      if (null != url) {
        fileContent = new String(readAllBytes(Paths.get(TEST_FOLDER + path)));
      }
    }
    return fileContent;
  }

  @Data
  static class RestApiRequest {
    private String endpoint;
    private Map<String, String> headers;
    private String httpMethod;
    private String payload;
    private Map<String, String> formParams;

    Map<String, String> addHeaders(Map<String, String> inputHeaders) {
      this.headers = Optional.ofNullable(headers).orElseGet(HashMap::new);
      this.headers.putAll(inputHeaders);
      return this.headers;
    }
  }
}
