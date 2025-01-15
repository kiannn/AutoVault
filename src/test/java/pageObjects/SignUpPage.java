package pageObjects;

import java.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignUpPage {

    @FindBy(css = ("a"))
    WebElement a;

    @FindBy(id = "inputFirstName")
    WebElement firstName;

    @FindBy(id = "inputLastName")
    WebElement lastName;

    @FindBy(id = "inputdob")
    WebElement inputdob;

    @FindBy(id = "inputEmail")
    WebElement inputEmail;

    @FindBy(id = "inputUsername")
    WebElement inputUsername;

    @FindBy(id = "inputPassword")
    WebElement inputPassword;

    @FindBy(id = "inputconfirmPassword")
    WebElement inputconfirmPassword;

    @FindBy(css = "button")
    WebElement button;

    private final WebDriverWait WebDriverWait;

    public SignUpPage(WebDriver driver, WebDriverWait WebDriverWait) {

        PageFactory.initElements(driver, this);
        this.WebDriverWait = WebDriverWait;
    }

    public void performSignUp(String fname, String lname, String email, LocalDate dob, String username, String password) {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        firstName.sendKeys(fname);

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        lastName.sendKeys(lname);

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputdob")));
        inputdob.sendKeys(dob.toString());

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputEmail")));
        inputEmail.sendKeys(email);

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        inputUsername.sendKeys(username);

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        inputPassword.sendKeys(password);

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputconfirmPassword")));
        inputconfirmPassword.sendKeys(password);

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button")));
        button.click();
    }

}
