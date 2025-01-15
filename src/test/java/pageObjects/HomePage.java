/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pageObjects;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    @FindBy(css = "ul.navbar-nav a[href*='addpage']")
    WebElement addVehicle;

    @FindBy(id = "msg")
    WebElement allert;

    @FindBy(css = "span#recordNumber")
    WebElement redord;

    @FindBy(css = "table#mytable tr")
    List<WebElement> allRows;

    @FindBy(css = "table#mytable tbody tr")
    List<WebElement> allRecords;

    @FindBy(css = "table#mytable th")
    List<WebElement> headres;

    @FindBy(css = "table#mytable td#lastColumnView")
    List<WebElement> viewdocsButton;

    @FindBy(css = "table#mytable input[value='Edit']")
    List<WebElement> editButton;

    @FindBy(css = "table#mytable input[value='Delete'")
    List<WebElement> allDeletes;

    WebDriverWait WebDriverWait;

    public HomePage(WebDriver driver, WebDriverWait WebDriverWait) {

        PageFactory.initElements(driver, this);
        this.WebDriverWait = WebDriverWait;

    }

    public void clickOnAddVehicle() {
        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.navbar-nav a[href*='addpage']")));
        addVehicle.click();
    }

    public String getMessageAlert() {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("msg")));

        return allert.getText();
    }

    public String getRecordMessage() {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span#recordNumber")));

        return redord.getText();
    }

    public List<String> getAllValuesAtRecord(int i) {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table#mytable tbody")));

        List<WebElement> tds = allRecords.get(i - 1).findElements(By.cssSelector("td"));
        List<String> carDetails = tds.stream()
                .filter(td -> !td.getText().isEmpty())
                .map(td -> td.getText())
                .collect(Collectors.toList());

        return carDetails;
    }

    public int numerOfAllRows() {

        return allRows.size();

    }

    public int numberOfRecords() {
        return allRecords.size();
    }

    public List<String> carDetailsHeader() {

        List<String> thCollect = headres.stream()
                .filter(th -> !th.getText().isEmpty()) // The last column contains only a <input> tag, encloses no text inside, so getText() returns empty
                .map(th -> th.getText())
                .collect(Collectors.toList());

        return thCollect;
    }

    public void clickOnViewButtonAtRecord(int i) {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table#mytable input[value='view']:nth-of-type(" + i + ")")));
        //By.cssSelector("table#mytable td#lastColumnView:nth-of-type("+i+")") did not work! why ?!?
        // By.cssSelector("table#mytable tr:nth-of-type("+i+") td#lastColumnView input") worked fine

        viewdocsButton.get(i - 1).findElement(By.cssSelector("input")).click();
    }

    public void clickOnEditButtonAtRecord(int i) {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table#mytable input[value='Edit']:nth-of-type(" + i + ")")));

        editButton.get(i - 1).click();
    }

    public void clickOnDeleteButtonAtRecord(int i) {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(" table#mytable input[value='Delete']:nth-of-type(" + i + ")")));
        allDeletes.get(i - 1).click();
    }

    public void handleBrowserAlert(String action, WebDriver driver) {

        // Switch to the alert
        Alert confirmDialog = driver.switchTo().alert();

        // Press "OK"
        if (action.equals("accept")) {
            confirmDialog.accept();
        }
        if (action.equals("cancel"))
        {
            confirmDialog.dismiss();
        }

    }
}
