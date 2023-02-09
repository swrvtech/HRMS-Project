package steps;

import io.cucumber.java.en.And;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddEmployeeSteps extends CommonMethods {

    public static String empID;
    public static String empName;
    String firstName, lastName;

    @Given("user navigates to AddEmployeePage")
    public void user_navigates_to_AddEmployeePage() {

        dash.navigateToAddEmployee();
    }

    @When("admin user enters {string} {string} and {string}")
    public void admin_user_enters_and(String firstName, String middleName, String lastName) {
        this.firstName=firstName;
        this.lastName=lastName;
        empName = firstName+" "+middleName+" "+lastName;
        sendText(addEmployee.firstName, firstName);
        sendText(addEmployee.middleName, middleName);
        sendText(addEmployee.lastName, lastName);
        sendText(addEmployee.photograph, Constants.BUG_BUSTERS_PICTURE_FILEPATH);
        empID = addEmployee.empID.getAttribute("value");
    }

    @When("admin user click on save button")
    public void admin_user_click_on_save_button() {

        click(addEmployee.saveBtn);
    }

    @Then("employee added successfully")
    public void employee_added_successfully() {
        Assert.assertEquals("Assertion Failed!", empID, pDetails.actual_employeeID.getAttribute("value"));
    }

    @Then("employee added successfully in Employee List")
    public void employee_added_successfully_in_employee_list() {
        click(dash.pimOption);
        boolean flag = false;
        for (WebElement obj : empList.employeeInfoTable) {
            if (obj.getText().contains(empID)) {
                flag = true;
                break;
            }
        }
        Assert.assertTrue("Assertion Failed!", flag);
    }


    @When("user adds multiple employee from excel using {string} and verify it")
    public void userAddsMultipleEmployeeFromExcelUsingAndVerifyIt(String EmployeeData) throws InterruptedException {

        List<Map<String, String>> empFromExcel = ExcelUtility.excelIntoMap(Constants.TESTDATA_FILEPATH, EmployeeData);

        //it returns one map from list of maps
        Iterator<Map<String, String>> itr = empFromExcel.iterator();
        while (itr.hasNext()) {
            //it returns the key and value for employee from excel
            Map<String, String> mapNewEmp = itr.next();

            sendText(addEmployee.firstName, mapNewEmp.get("firstName"));
            sendText(addEmployee.middleName, mapNewEmp.get("middleName"));
            sendText(addEmployee.lastName, mapNewEmp.get("lastName"));
            String empIdValue = addEmployee.empID.getAttribute("value");
            click(addEmployee.saveBtn);
            Thread.sleep(3000);

            click(dash.employeeListOption);
            Thread.sleep(2000);

            //to search the employee, we use emp id what we captured from attribute
            sendText(employeeListPage.idEmployee, empIdValue);
            click(employeeListPage.searchButton);

            //verifying the employee added from the excel file
            List<WebElement> rowData = employeeListPage.employeeInfoTable;

            for (int i = 0; i < rowData.size(); i++) {
                System.out.println("I am inside the loop and worried about josh");
                //getting the text of every element from here and storing it into string
                String rowText = rowData.get(i).getText();
                System.out.println(rowText);

                String expectedData = empIdValue + " " + mapNewEmp.get("firstName") + " " + mapNewEmp.get("middleName") + " " + mapNewEmp.get("lastName");

                //verifying the exact details  of the employee
                Assert.assertEquals(expectedData, rowText);
            }
        }
    }

    @And("user deletes employee id")
    public void userDeletesEmployeeId() {
        addEmployee.empID.clear();
    }

    @And("user clicks on create login checkbox")
    public void userClicksOnCreateLoginCheckbox() {
        jsClick(addEmployee.checkBox);
    }

    @And("user enters login credentials as {string} and {string}")
    public void userEntersLoginCredentialsAsAnd(String username, String password) {
        sendText(addEmployee.createUsername, username);
        sendText(addEmployee.createPassword, password);
        sendText(addEmployee.confirmPassword, password);
    }

    @And("user captures employee id")
    public void userCapturesEmployeeId() {

        empID = addEmployee.empID.getAttribute("value");
    }

    @And("added employee is displayed in database")
    public void addedEmployeeIsDisplayedInDatabase() {

        String query=DatabaseSteps.getFnameLnameQuery()+empID;
        List<Map<String, String>> dataFromDatabase= DatabaseUtils.getListOfMapsFromRset(query);

        String fNameFromDb=dataFromDatabase.get(0).get("emp_firstname");
        String lNameFromDb=dataFromDatabase.get(0).get("emp_lastname");

        Assert.assertEquals(firstName, fNameFromDb);
        Assert.assertEquals(lastName, lNameFromDb);
    }
}
