package com.example.CarDealerShip;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObjects.AddEditPage;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.SignUpPage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT  ,
               properties = {"spring.datasource.url=jdbc:h2:mem:testdb;",
                             "spring.datasource.username=root",
                             "spring.datasource.password=@Hv66VpVvyqGP"})

class CarDealerShipApplicationTests {

    private WebDriver driver;

    @BeforeAll
    public static void beforeAll() {
//        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        driver = new ChromeDriver();

    }
    @AfterEach
    public void afterEach() {
        driver.quit();

    }

    public static List<List<String>> ListOfCarSpec() {

        return List.of(List.of("Id", "Make",
                "Model",
                "Year",
                "Purchase Date",
                "Price",
                "Power Train",
                "Condition",
                "Horse Power", "Documnets"));
    }

    private void doMockSignUp(String fname, String lname, String email, LocalDate dob, String username, String password) {
  
        WebDriverWait WebDriverWait  = new WebDriverWait(driver, Duration.ofSeconds(3));
        
        driver.get("http://localhost:8086/loginpage");

        LoginPage login = new LoginPage(driver, WebDriverWait);
        login.headToSigeUp();

        SignUpPage signup = new SignUpPage(driver, WebDriverWait);
        signup.performSignUp(fname, lname, email, dob, username, password);

    }

    private void doLogInAfterSignUp(String username, String password) {

        WebDriverWait WebDriverWait = new WebDriverWait(driver, Duration.ofSeconds(3));

        LoginPage LoginPage = new LoginPage(driver, WebDriverWait);
        LoginPage.performLogIn(username, password);

    }
    
    private void doLogOut(WebDriverWait waite) {

        waite.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.navbar-nav button.dropbtn")));
        
        // Create an Actions object
        Actions actions = new Actions(driver);

        WebElement accountButton = driver.findElement(By.cssSelector("ul.navbar-nav button.dropbtn"));

        // Perform the hover-over-the-element action
        actions.moveToElement(accountButton).perform();

        waite.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.navbar-nav div.dropdown-content a[href= '/pre-logout']")));
        WebElement logoutButton = driver.findElement(By.cssSelector("ul.navbar-nav div.dropdown-content a[href= '/pre-logout']"));

        logoutButton.click();

    }

    @Test
    public void test_RedirectionToLoginPageAfterSignUp() {
        doMockSignUp("kian", "pd", "kian1986@live.com", LocalDate.of(1980, Month.MARCH, 25), "user", "@Hv66VpVvyqGP");

        WebDriverWait WebDriverWait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#success")));

        WebElement findElement = driver.findElement(By.cssSelector("div#success"));

        Assertions.assertEquals("You successfully signed up! Please continue to login.", findElement.getText());
        Assertions.assertEquals("http://localhost:8086/loginpage?sigUpSuccess", driver.getCurrentUrl());
        Assertions.assertEquals("Login-Page", driver.getTitle());
        Assertions.assertTrue(driver.getPageSource().contains("<button type=\"submit\" class=\"btn btn-primary\" id=\"login-button\">Login</button>"));

    }


    @ParameterizedTest
    @MethodSource("ListOfCarSpec")
    public void test_RedirectToHomePageAfterLogin_test_BadUrl(List<String> specs) {
        
        doMockSignUp("kian", "pd", "kian1986@gmail.com", LocalDate.of(1980, Month.MARCH, 25), "BadUrl", "@Hv66VpVvyqGP");
        doLogInAfterSignUp("BadUrl", "@Hv66VpVvyqGP");

        WebDriverWait WebDriverWait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebDriverWait.until(ExpectedConditions.titleContains("All Available Vehicles"));

        HomePage home = new HomePage(driver, WebDriverWait);
        List<String> thCollect = home.carDetailsHeader();

        Assertions.assertEquals("http://localhost:8086/cars/home/showallcars", driver.getCurrentUrl());
        Assertions.assertEquals("All Available Vehicles", driver.getTitle());
        Assertions.assertEquals(specs, thCollect);

        driver.get("http://localhost:8086/some-bad-url");

        WebElement h2 = driver.findElement(By.tagName("h2"));
        WebElement errormsg = driver.findElement(By.id("errormsg"));

        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
        Assertions.assertEquals("Custom Errors Page", driver.getTitle());
        Assertions.assertEquals("Page Not Found", h2.getText());
        Assertions.assertEquals("The page you are looking for does not exist !", errormsg.getText());

    }

    @Test
    public void test_LargeUpload() throws InterruptedException {
        
        doMockSignUp("kian", "pd", "kian1986@aol.com", LocalDate.of(1980, Month.MARCH, 25), "LargeUpload", "@Hv66VpVvyqGP");
        doLogInAfterSignUp("LargeUpload", "@Hv66VpVvyqGP");
        
        WebDriverWait WebDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        
        HomePage home = new HomePage(driver, WebDriverWait);
        home.clickOnAddVehicle();
        
        AddEditPage add  = new AddEditPage(driver, WebDriverWait);
        String editButtonText = add.getAdd_UpdateButtonText();
        add.clickOnUploadMore(4);
        add.uploadDocumnet(new File("15MB.zip").getAbsolutePath(),2);
        
        String addpage = "http://localhost:8086/cars/allcars/addpage?act=add&carFormstate=true";
        String addPageTitle = "Add Or Update Page";
        
        String text1 = add.getErrorMsgOnLargFileAtRow(2);
 
        Assertions.assertTrue(editButtonText.equals("Add"));
        Assertions.assertEquals(addpage, driver.getCurrentUrl());
        Assertions.assertEquals(addPageTitle, driver.getTitle());
        
        add.uploadDocumnet(new File("course-presentation-master-spring-and-spring-boot.pdf").getAbsolutePath(),5);
        String text2 = add.getErrorMsgOnLargFileAtRow(5);
        
        Assertions.assertEquals(addpage, driver.getCurrentUrl());
        Assertions.assertEquals(addPageTitle, driver.getTitle());
        
        add.addAMake("BMW");
        add.clickOnAddOrUpdateButton();
        
        home.clickOnViewButtonAtRecord(1);
        
        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table[name = 'docViewTable'] tr:nth-of-type(1)")));
        List<WebElement> tableRows = driver.findElements(By.cssSelector("table[name = 'docViewTable'] tr"));

        WebElement tdInsideTr = tableRows.getFirst().findElement(By.tagName("td"));
        
        Assertions.assertEquals("Maximum upload size exceeded", text1);
        Assertions.assertEquals("Maximum upload size exceeded", text2);
        Assertions.assertTrue(tableRows.size()==1);
        Assertions.assertEquals("No File Uploaded", tdInsideTr.getText());
 
    }

    @Test
    public void test_HomePage_Not_Accessible_Without_Login() throws InterruptedException {

        String url = "http://localhost:8086/cars/home/showallcars";
        String usernameInputTag = """
                                 <input type="input" class="form-control" name="username" id="inputUsername" placeholder="Enter Username" maxlength="20" required="">
                                 """;
        
        driver.get(url);

        WebDriverWait waite = new WebDriverWait(driver, Duration.ofSeconds(2));
        waite.until(ExpectedConditions.titleContains("Login-Page"));
        
        Assertions.assertNotEquals(url, driver.getCurrentUrl());
        Assertions.assertNotEquals("All Available Vehicles", driver.getTitle());
        Assertions.assertEquals("http://localhost:8086/loginpage", driver.getCurrentUrl());
        Assertions.assertEquals("Login-Page", driver.getTitle());
        Assertions.assertTrue(driver.getPageSource().contains(usernameInputTag));

        String url2 = "http://localhost:8086/cars/allcars/addpage?act=add&carFormstate=true";

        driver.get(url2);

        waite.until(ExpectedConditions.titleContains("Login-Page"));

        Assertions.assertNotEquals(url2, driver.getCurrentUrl());
        Assertions.assertNotEquals("Add Or Update Page", driver.getTitle());
        Assertions.assertEquals("http://localhost:8086/loginpage", driver.getCurrentUrl());
        Assertions.assertEquals("Login-Page", driver.getTitle());
        Assertions.assertTrue(driver.getPageSource().contains(usernameInputTag));

    }

    @Test
    public void test_Signup_Login_Logout_Flow() throws InterruptedException {
        
        doMockSignUp("kian", "pd", "kian1986@hotmail.com", LocalDate.of(1980, Month.MARCH, 25), "SignupLoginLogout", "@Hv66VpVvyqGP");
        doLogInAfterSignUp("SignupLoginLogout", "@Hv66VpVvyqGP");
        
        String homePageUrl = "http://localhost:8086/cars/home/showallcars";
        
        String preLogOut = "http://localhost:8086/pre-logout";
        
        WebDriverWait waite = new WebDriverWait(driver, Duration.ofSeconds(2));        
        waite.until(ExpectedConditions.titleContains("All Available Vehicles"));
        
        HomePage home = new HomePage(driver, waite);
        String recordm = home.getRecordMessage();
       
        Assertions.assertEquals("All Available Vehicles", driver.getTitle());
        Assertions.assertEquals(homePageUrl, driver.getCurrentUrl());
        Assertions.assertEquals("Number of records found : 0",recordm); 
        
        doLogOut(waite);
          
        waite.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.container h3")));

        WebElement h3 = driver.findElement(By.cssSelector("div.container h3"));

        Assertions.assertEquals("Logout-verify", driver.getTitle());
        Assertions.assertEquals(preLogOut, driver.getCurrentUrl());
        Assertions.assertEquals("Are you sure you want to logout ?", h3.getText());

        waite.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type = 'button']")));

        WebElement cancelInput = driver.findElement(By.cssSelector("input[type = 'button']"));
        cancelInput.click();
                
        recordm = home.getRecordMessage();
        
        Assertions.assertEquals("All Available Vehicles", driver.getTitle());
        Assertions.assertEquals(homePageUrl, driver.getCurrentUrl());
        Assertions.assertEquals("Number of records found : 0",recordm); 

        doLogOut(waite);
 
        waite.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.container h3")));

        h3 = driver.findElement(By.cssSelector("div.container h3"));

        Assertions.assertEquals("Logout-verify", driver.getTitle());
        Assertions.assertEquals(preLogOut, driver.getCurrentUrl());
        Assertions.assertEquals("Are you sure you want to logout ?", h3.getText());
        
        waite.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type = 'submit']")));

        WebElement sumbitInput = driver.findElement(By.cssSelector("input[type = 'submit']"));
        sumbitInput.click();
        
        waite.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#loggedout")));
        WebElement logoutMsg = driver.findElement(By.cssSelector("div#loggedout"));
        
        String loginSubmitButton = """
                                    <button type="submit" class="btn btn-primary" id="login-button">Login</button>
                                   """;
        
        Assertions.assertEquals("You have been logged out", logoutMsg.getText());
        Assertions.assertEquals("http://localhost:8086/loginpage?logout", driver.getCurrentUrl());
        Assertions.assertEquals("Login-Page", driver.getTitle());
        Assertions.assertTrue(driver.getPageSource().contains(loginSubmitButton));
        
        driver.get(homePageUrl);

        waite.until(ExpectedConditions.titleContains("Login-Page"));
        
        Assertions.assertEquals("http://localhost:8086/loginpage", driver.getCurrentUrl());
        Assertions.assertEquals("Login-Page", driver.getTitle());
        Assertions.assertTrue(driver.getPageSource().contains(loginSubmitButton));
    }

    @Test
    public void test_Invalid_Credentials_Login_Failure() throws InterruptedException {
        
        doMockSignUp("kian", "pd", "kian@gmail.com", LocalDate.of(1980, Month.MARCH, 25), "Login_Failure", "@Hv66VpVvyqGP");
        doLogInAfterSignUp("Login_Failure", "@Hv66VpVvyq"); //Invalid credentials
        
        WebDriverWait waite = new WebDriverWait(driver, Duration.ofSeconds(2));

        waite.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#invalid")));
        WebElement invalidMsg = driver.findElement(By.cssSelector("div#invalid"));
        
        Assertions.assertEquals("Invalid username or password", invalidMsg.getText());
        Assertions.assertEquals("http://localhost:8086/loginpage?error", driver.getCurrentUrl());
        Assertions.assertEquals("Login-Page", driver.getTitle());
        Assertions.assertTrue(driver.getPageSource().contains("""
                                                                          <button type="submit" class="btn btn-primary" id="login-button">Login</button>
                                                                         """));
        
    }

    @Test
    public void test_Add_Edit_Delete_Car() throws InterruptedException, InterruptedException {

        doMockSignUp("kian", "pd", "kian1986@msn.com", LocalDate.of(1980, Month.MARCH, 25), "Add_Edit_Delete", "@Hv66VpVvyqGP");
        doLogInAfterSignUp("Add_Edit_Delete", "@Hv66VpVvyqGP");

        String homePageUrlSuccessfulAdd = """
                             http://localhost:8086/cars/home/showallcars?addOrEditMsg=Car%20Id:%201%20%20has%20successfully%20added""";

        WebDriverWait WebDriverWait = new WebDriverWait(driver, Duration.ofSeconds(3));

        HomePage home = new HomePage(driver, WebDriverWait);
        home.clickOnAddVehicle();
        List<String> fileNamesInput = new ArrayList<>(Arrays.asList("sample-5.zip", "1.5-MB.pdf", "file_example_PNG_3MB.png"));
        List<String> data = new ArrayList<>(Arrays.asList("BMW", "Z4", String.valueOf(2015),
                LocalDate.of(2020, 03, 10).toString(),
                String.valueOf(50000d), "AWD", "Used", String.valueOf(250d)));

        AddEditPage addOrEdit = new AddEditPage(driver, WebDriverWait);
        addOrEdit.clickOnUploadMore(2);
        String editButtonText = addOrEdit.getAdd_UpdateButtonText();
        
        addOrEdit.uploadDocumnet(new File(fileNamesInput.get(0)).getAbsolutePath(), 1);
        addOrEdit.uploadDocumnet(new File(fileNamesInput.get(1)).getAbsolutePath(), 2);
        addOrEdit.uploadDocumnet(new File(fileNamesInput.get(2)).getAbsolutePath(), 3);

        addOrEdit.addOrEditForm(data, 'a');
        addOrEdit.clickOnAddOrUpdateButton();

        String alert = home.getMessageAlert();
        String recordm = home.getRecordMessage();
        List<String> carDetails = home.getAllValuesAtRecord(1);      
        int allShownRecords = home.numberOfRecords();
        int allRows = home.numerOfAllRows();

        Assertions.assertTrue(editButtonText.equals("Add")); 
        Assertions.assertEquals("All Available Vehicles", driver.getTitle());
        Assertions.assertEquals(homePageUrlSuccessfulAdd, driver.getCurrentUrl());
        Assertions.assertEquals("Number of records found : 1", recordm);

        home.clickOnViewButtonAtRecord(1);

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table[name = 'docViewTable'] tr:nth-of-type(3)")));
        List<WebElement> docTableRows = driver.findElements(By.cssSelector("table[name = 'docViewTable'] tr"));
        List<WebElement> tds = docTableRows.stream()
                .map(tr -> tr.findElement(By.cssSelector("td[colspan = '2']")))
                .collect(Collectors.toList());
        List<String> fileNmaes = tds.stream().map(f -> f.getText()).collect(Collectors.toList());

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.modal-header h5")));
        WebElement headerModal = driver.findElement(By.cssSelector("div.modal-header h5"));

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("closeModal")));
        WebElement modalCloseButton = driver.findElement(By.id("closeModal"));
        modalCloseButton.click();

        data.add(0, String.valueOf(1));

        Assertions.assertEquals(1, allShownRecords);
        Assertions.assertEquals(2, allRows);
        Assertions.assertEquals("Car Id: 1 has successfully added", alert);
        Assertions.assertEquals(data, carDetails);
        Assertions.assertEquals(fileNamesInput, fileNmaes);
        Assertions.assertEquals("Documents uploaded for Car Id : 1", headerModal.getText());

        data.remove(0);
        data.set(0, "Mercedes-Benz");
        data.set(1, "CLK-Class");
        data.set(2, String.valueOf(2020));
        data.set(3, LocalDate.of(2000, Month.JANUARY, 1).toString());
        data.set(4, String.valueOf(30000d));

        fileNamesInput.remove(1);
        fileNamesInput.add("file_example_PPT_1MB.ppt");

        home.clickOnEditButtonAtRecord(1);

        String editPageURL = "http://localhost:8086/cars/allcars/updatepage?id=1&carFormstate=true";

        WebDriverWait.until(ExpectedConditions.titleContains("Add Or Update Page"));

        editButtonText = addOrEdit.getAdd_UpdateButtonText();

        String editPageHead = addOrEdit.getEditPageHead();

        Assertions.assertEquals("Add Or Update Page", driver.getTitle());
        Assertions.assertEquals(editPageURL, driver.getCurrentUrl());
        Assertions.assertEquals("Edit details for car id = 1, Make = BMW, Model = Z4", editPageHead);
        Assertions.assertEquals("Update", editButtonText);

        addOrEdit.addOrEditForm(data, 'e');
        addOrEdit.deleteAdocument(2);

        String delMsg = addOrEdit.getEditAlertMessage();

        addOrEdit.uploadReplacementDocumnet(new File("file_example_PPT_1MB.ppt").getAbsolutePath(), 4, 2);
        addOrEdit.clickOnAddOrUpdateButton();

        alert = home.getMessageAlert();
        recordm = home.getRecordMessage();
        allShownRecords = home.numberOfRecords();
        allRows = home.numerOfAllRows();
        carDetails = home.getAllValuesAtRecord(1);
        homePageUrlSuccessfulAdd = """
                                 http://localhost:8086/cars/home/showallcars?addOrEditMsg=Car%20Id:%201%20%20has%20successfully%20updated""";

        Assertions.assertEquals("document 1.5-MB.pdf deleted successfully", delMsg);
        Assertions.assertEquals("All Available Vehicles", driver.getTitle());
        Assertions.assertEquals(homePageUrlSuccessfulAdd, driver.getCurrentUrl());
        Assertions.assertEquals("Number of records found : 1", recordm);

        home.clickOnViewButtonAtRecord(1);

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table[name = 'docViewTable'] tr:nth-of-type(3)")));
        docTableRows = driver.findElements(By.cssSelector("table[name = 'docViewTable'] tr"));
        tds = docTableRows.stream()
                .map(tr -> tr.findElement(By.cssSelector("td[colspan = '2']")))
                .collect(Collectors.toList());

        fileNmaes = tds.stream().map(f -> f.getText()).collect(Collectors.toList());

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.modal-header h5")));
        headerModal = driver.findElement(By.cssSelector("div.modal-header h5"));

        data.add(0, String.valueOf(1));

        Assertions.assertEquals(1, allShownRecords);
        Assertions.assertEquals(2, allRows);
        Assertions.assertEquals("Car Id: 1 has successfully updated", alert);
        Assertions.assertEquals(data, carDetails);
        Assertions.assertEquals(fileNamesInput, fileNmaes);
        Assertions.assertEquals("Documents uploaded for Car Id : 1", headerModal.getText());

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("closeModal")));
        modalCloseButton = driver.findElement(By.id("closeModal"));
        modalCloseButton.click();

        home.clickOnDeleteButtonAtRecord(1);
        home.handleBrowserAlert("accept", driver);

        alert = home.getMessageAlert();
        allRows = home.numerOfAllRows();
        allShownRecords = home.numberOfRecords();

        recordm = home.getRecordMessage();

        Assertions.assertEquals("Car Id: 1 has successfully deleted", alert);
        Assertions.assertEquals(0, allShownRecords);
        Assertions.assertEquals(1, allRows);
        Assertions.assertEquals("Number of records found : 0", recordm);

    }

}
