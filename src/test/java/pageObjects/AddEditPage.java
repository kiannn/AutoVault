package pageObjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddEditPage {

    @FindBy(id = "uploadmore")
    WebElement uploadMoreFile;

    @FindBy(css = "table#tableAdd input[name = 'filee']")
    List<WebElement> filesInput;

    @FindBy(id = "makelist")
    WebElement make;

    @FindBy(id = "modellist")
    WebElement model;

    @FindBy(id = "powerTrain")
    WebElement pt;

    @FindBy(id = "year")
    WebElement year;

    @FindBy(id = "price")
    WebElement price;

    @FindBy(id = "datePurchased")
    WebElement purchDate;

    @FindBy(id = "condn")
    WebElement condition;

    @FindBy(id = "horsePower")
    WebElement hp;

    @FindBy(id = "addbutton")
    WebElement addButton;

    @FindBy(css = "table#tableEdit input[value = 'delete']")
    List<WebElement> docDeleteButton;

    @FindBy(css = "table#tableEdit input[name = 'filee']")
    List<WebElement> replacementDocfiles;

    @FindBy(css = "span#msg")
    WebElement docDeletMsgElement;

    @FindBy(css = "table#tableAdd tr")
    List<WebElement> fillUploadRows;

    @FindBy(id = "addbutton")
    WebElement edit_add_Button ;
     
    WebDriver driver;

    WebDriverWait WebDriverWait;

    public AddEditPage(WebDriver driver, WebDriverWait WebDriverWait) {

        PageFactory.initElements(driver, this);
        this.WebDriverWait = WebDriverWait;
        this.driver = driver;
        System.out.println("pageObjects.AddEditPage.<init>()\n"+docDeleteButton+"\n"+replacementDocfiles+"\n"+docDeletMsgElement);
    }

    public void clickOnUploadMore(int i) {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("uploadmore")));
       
        for (byte b = 1; b <= i; b++) { 

            uploadMoreFile.click();
        }
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)"); //Scroll to the buttom of the page

    }

    public void uploadDocumnet(String absolutePath, int i) {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table#tableAdd tr:nth-of-type(" + i + ") input[name = 'filee']")));
        filesInput.get(i - 1).sendKeys(absolutePath);

    }

    public void clickOnAddOrUpdateButton() {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addbutton")));
        addButton.click();

    }

    public void addAMake(String makeName) {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("makelist")));
        make.sendKeys(makeName);
    }

    public void addOrEditForm(List<String> data, char action) {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("makelist")));
        this.make.sendKeys(data.get(0));

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modellist")));
        this.model.sendKeys(data.get(1));

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("powerTrain")));
        this.pt.sendKeys(data.get(5));

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("year")));
        this.year.sendKeys(data.get(2));

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("price")));
        if (action == 'e') {
            this.price.clear();
        }
        this.price.sendKeys(data.get(4));

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("datePurchased")));
        if (action == 'e') {
            this.purchDate.clear();
        }
        this.purchDate.sendKeys(data.get(3));

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("condn")));
        this.condition.sendKeys(data.get(6));

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("horsePower")));
        if (action == 'e') {
            this.hp.clear();
        }
        this.hp.sendKeys(data.get(7));

    }

    public void deleteAdocument(int i) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table#tableEdit tr:nth-of-type(" + i + ") input[value = 'delete']")));
        docDeleteButton.get(i - 1).click();
    }

    public void uploadReplacementDocumnet(String replacement, int i, int f) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table#tableEdit tr:nth-of-type(" + i + ") input[name = 'filee']")));
        replacementDocfiles.get(i - f - 1).sendKeys(replacement);

    }

    public String getEditPageHead() {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pageHeade")));
        WebElement editPageHead = driver.findElement(By.id("pageHeade"));
        String editPageHeadText = editPageHead.getText();

        return editPageHeadText;
    }

    public String getEditAlertMessage() {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span#msg")));

        return docDeletMsgElement.getText();
    }

    public String getErrorMsgOnLargFileAtRow(int i) {

        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table#tableAdd tr:nth-of-type(" + i + ") div[name = 'largeUploadError']")));//element position number in nth-of-type(2) is 1-based
        WebElement findElement1 = fillUploadRows.get(i - 1);//driver.findElement(By.cssSelector("table#tableAdd tr:nth-of-type(2) div[name = 'largeUploadError']"));
        findElement1.findElement(By.cssSelector("div[name = 'largeUploadError']"));

        return findElement1.getText();
    }

    public String getAdd_UpdateButtonText() {
        WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addbutton")));
        String editButtonText = edit_add_Button.getAttribute("value");
        
        return editButtonText;
    }
}
