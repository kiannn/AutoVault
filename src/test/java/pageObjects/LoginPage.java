package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    @FindBy(css = "a")
    WebElement a;

    @FindBy(id = "inputUsername")
    WebElement inputUsername;

    @FindBy(id = "inputPassword")
    WebElement inputPassword;

    @FindBy(css = "button")
    WebElement button;

    private final WebDriverWait WebDriverWait;

    public LoginPage(WebDriver driver, WebDriverWait WebDriverWait) {
        PageFactory.initElements(driver, this);

        this.WebDriverWait = WebDriverWait;
    }

    public void headToSigeUp() {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a")));
        a.click();
    }

    public void performLogIn(String username, String password) {

        WebDriverWait.until(ExpectedConditions.titleContains("Login-Page"));
        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        inputUsername.sendKeys(username);

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        inputPassword.sendKeys(password);

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button")));
        button.click();

    }

}
