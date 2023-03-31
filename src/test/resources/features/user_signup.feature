@FunctionalTest
Feature: Test for User Registration

  Background: User wants to register themselves to use liBRBary features
    #Given The endpoint is already configured

    #@Database
    #@TestDataSetup:feature/scripts/<setup-queries>.sql
    #@TearDownTestData:feature/scripts/<delete-queries>.sql
    Scenario: Access Test endpoint
      When API endpoint is /v1/users/test
      #And auth token is generated for TEST
      #And headers as
      #  | headername          | headervalue             |
      #  | Accept              | application/json        |
      #  | AX-Correlation-ID   | 123456                  |
      And HTTP Method is GET
      #And payload from file features/data/<fileame>.json
      When executed
      Then HTTP status code should be 200