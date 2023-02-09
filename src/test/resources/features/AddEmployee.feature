@addEmployee
Feature: Add Employee

  Background:
    Given user is logged in with admin credentials
    And user navigates to AddEmployeePage

  @smoke @sprint5
  Scenario: Add Employee
    When admin user enters "John" "J" and "Doe"
    And admin user click on save button
    Then employee added successfully

  @regression
  Scenario: Add Employee without employee id
    When admin user enters "Jane" "J" and "Smith"
    And user deletes employee id
    And admin user click on save button
    Then employee added successfully

  @regression
  Scenario: AddEmployee and create Login Credentials
    When admin user enters "Sumair" "J" and "Jawaid"
    And user clicks on create login checkbox
    And user enters login credentials as "SumairJ" and "!Sumair123!"
    And admin user click on save button
    Then employee added successfully

  @regression
  Scenario Outline: Adding multiple employees
    And admin user enters "<FirstName>" "<MiddleName>" and "<LastName>"
    And admin user click on save button
    Then employee added successfully
    Examples:
      | FirstName | MiddleName | LastName |
      | Gulnam    | G          | Mazar    |
      | Rampal    | R          | Chambel  |
      | Azam      | A          | Kamar    |

  @excel
  Scenario: Adding multiple employees using excel file
    When user adds multiple employee from excel using "EmployeeData" and verify it

  @db @regression
  Scenario: Adding employee and verifying it is stored in database
    And admin user enters "Asel" "A" and "Umurzakova"
    And user captures employee id
    And admin user click on save button
    And added employee is displayed in database