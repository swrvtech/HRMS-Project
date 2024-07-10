
Feature: API testing

  Background:
    Given a JWT bearer token is generated

  @api
  Scenario: creating an employee
    Given a request is prepared for creating an employee
    When a POST call is made to create the employee
    Then the status code will be 201 for this call
    And the employee created contains key "Message" and value "Employee Created"
    And the employee id "Employee.employee.id" is stored as a global variable
