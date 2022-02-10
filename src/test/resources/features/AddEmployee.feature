@addEmployee
Feature: Add Employee

  Background:
    Given user is logged in with admin credentials
    When admin user clicks on PIM button
    And admin user clicks on Add Employee button

 @regression
  Scenario: scenario of adding employee
    And admin user enters "Bug" "Buster" and "Group"
    And admin user click on save button
    Then employee added successfully