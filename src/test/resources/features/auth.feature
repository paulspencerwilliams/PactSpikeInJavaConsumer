Feature: Authentication functionality

  Scenario: Successful login
    When I authenticate
    Then I will be welcomed personally

  Scenario: Unsuccessful login due to wrong password
    When I attempt to log in with the wrong password
    Then I will be prompted to try again suggesting why

  Scenario: Registration is validated
    When I attempt to register with partial details
    Then I will be prompted to fill in blanks allowing me to register successfully

  Scenario: Successful registration
    When I register
    Then I will be welcomed personally
    And I will be notified that I need to verify my email