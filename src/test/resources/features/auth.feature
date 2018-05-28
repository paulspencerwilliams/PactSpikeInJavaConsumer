Feature: Authentication functionality

  Scenario: Successful login
    When I authenticate
    Then I will be welcomed personally

  Scenario: Unsuccessful login due to wrong password
    When I attempt to log in with the wrong password
    Then I will be prompted to try again suggesting why