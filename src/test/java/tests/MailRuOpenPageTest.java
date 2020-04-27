package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MailRuOpenPageTest {

    public static void main(String[] args) throws InterruptedException {

        String sendToAddress = "lena.khalopitsa@gmail.com";
        String emailSubject = getRandomString(9);
        String emailBody = getRandomString(50);

        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.get("https://mail.ru/");

        By loginSelector = By.id("mailbox:login");
        WebElement loginBtn = driver.findElement(loginSelector);
        loginBtn.sendKeys("at_cdp9");

        By saveAuthSelector = By.id("mailbox:saveauth");
        WebElement saveAuthCheckBox = driver.findElement(saveAuthSelector);
        saveAuthCheckBox.click();

        By enterPassBtnSelector = By.xpath("//*[@id=\"mailbox:submit\"]/input");
        WebElement enterPassBtn = driver.findElement(enterPassBtnSelector);
        enterPassBtn.click();

        By passwordSelector = By.id("mailbox:password");
        WebElement passwordBtn = driver.findElement(passwordSelector);
        passwordBtn.sendKeys("45admin72");
        enterPassBtn.click();

        By userNameSelector = By.id("PH_user-email");
        WebElement userName = new WebDriverWait(driver, 5).
                until(ExpectedConditions.visibilityOfElementLocated(userNameSelector));
        Assert.assertTrue( userName.getText().contains("at_cdp9"), "Wrong user login name!");

        By composeBtnSelector = By.xpath("//*[@class=\"compose-button__wrapper\"]");
        WebElement composeBtn = driver.findElement(composeBtnSelector);
        composeBtn.click();

        By sendToSelector = By.xpath("//input[@class=\"container--H9L5q size_s_compressed--2c-eV\"]");
        WebElement sendTo = new WebDriverWait(driver, 5).
                until(ExpectedConditions.visibilityOfElementLocated(sendToSelector));
        sendTo.sendKeys(sendToAddress);

        By subjectSelector = By.name("Subject");
        WebElement subject = driver.findElement(subjectSelector);
        subject.sendKeys(emailSubject);

        By bodySelector = By.xpath("//div[@contenteditable=\"true\" and @role=\"textbox\"]/div");
        WebElement body = driver.findElement(bodySelector);
        body.sendKeys(emailBody);

        By saveBtnSelector = By.xpath("//*[@title=\"Сохранить\"]");
        WebElement saveBtn = driver.findElement(saveBtnSelector);
        saveBtn.click();

        By closeBtnSelector = By.xpath("//*[@title=\"Закрыть\"]");
        WebElement closeBtn = new WebDriverWait(driver, 5).
                until(ExpectedConditions.visibilityOfElementLocated(closeBtnSelector));
        closeBtn.click();

        By draftsBtnSelector = By.xpath("//*[@href=\"/drafts/\"]");
        WebElement draftsBtn = new WebDriverWait(driver, 5).
                until(ExpectedConditions.visibilityOfElementLocated(draftsBtnSelector));
        draftsBtn.click();
        Thread.sleep(3000);

        By draftEmailsSelector = By.xpath("//*[@class=\"ll-sj__normal\"]");
        List<WebElement> draftEmails = driver.findElements(draftEmailsSelector);
        for (int i = 0; i < draftEmails.size(); i++)
            {
                if(draftEmails.get(i).getText().contains(emailSubject)) {
                    draftEmails.get(i).click();
                    break;
                }
            }

        By sendToInDraftSelector = By.xpath("//*[@class=\"text--1tHKB\"]");
        WebElement sendToInDraft = new WebDriverWait(driver, 5).
                until(ExpectedConditions.visibilityOfElementLocated(sendToInDraftSelector));
        Assert.assertTrue(sendToInDraft.getText().contains(sendToAddress), "Wrong address!");

        By bodyInDraftSelector = By.xpath("//div[@class=\"js-helper js-readmsg-msg\"]/descendant::div");
        List<WebElement> bodyInDraft = new WebDriverWait(driver, 5).
                until(ExpectedConditions.visibilityOfAllElementsLocatedBy(bodyInDraftSelector));

        Assert.assertTrue(equalText(bodyInDraft, emailBody), "Email body is not the same!");

        By sendBtnSelector = By.xpath("//*[@title=\"Отправить\"]");
        WebElement sendBtn = driver.findElement(sendBtnSelector);
        sendBtn.click();
        Thread.sleep(2000);

        By closeAfterSendBtnSelector = By.xpath("//*[@title=\"Закрыть\"]");
        WebElement closeAfterSendBtn = driver.findElement(closeAfterSendBtnSelector);
        closeAfterSendBtn.click();
        Thread.sleep(2000);

        By draftEmailsAfterSendSelector = By.xpath("//*[@class=\"ll-sj__normal\"]");
        List<WebElement> draftEmailsAfterSend = driver.findElements(draftEmailsAfterSendSelector);
        Assert.assertFalse(equalText(draftEmailsAfterSend, emailSubject), "Email is NOT sent!");

        By sendFolderBtnSelector = By.xpath("//*[@title=\"Отправленные\"]");
        WebElement sendFolderBtn = driver.findElement(sendFolderBtnSelector);
        sendFolderBtn.click();
        Thread.sleep(2000);

        By sendEmailsSelector = By.xpath("//*[@class=\"ll-sj__normal\"]");
        List<WebElement> sendEmails = driver.findElements(sendEmailsSelector);
        Assert.assertTrue(equalText(sendEmails,emailSubject), "Subject is wrong!");

        By exitBtnSelector = By.id("PH_logoutLink");
        WebElement exitBtn = driver.findElement(exitBtnSelector);
        exitBtn.click();

        driver.quit();
    }

    private static String getRandomString (int length){
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmopqrstuywxyz";
        Random rnd = new Random();
        String str = "";
        for (int i = 0; i < length; i++) {
            str = str + AB.charAt(rnd.nextInt(AB.length()));
        }
        return str;
    }

    private static boolean equalText(List<WebElement> webElements, String text) {
        boolean equalText = false;
        for (int i = 0; i < webElements.size(); i++)
        {
                if(webElements.get(i).getText().contains(text)) {
                    equalText = true;
                     break;
            }
        }

        return equalText;
    }


}
