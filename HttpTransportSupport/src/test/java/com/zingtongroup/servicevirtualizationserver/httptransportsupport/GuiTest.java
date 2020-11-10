package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.EndpointUrlFilter;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpHeader;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpResponseToSend;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.PreparedHttpResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.RegisteredPreparedHttpResponses;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;

public class GuiTest extends ServerTestBase {

    WebDriver driver;

    @BeforeClass
    public static void extractFirefoxDriver() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("geckodriver.exe");
        File outFile = new File("geckodriver.exe");
        if(outFile.exists()) return;

        byte[] buffer = new byte[is.available()];
        is.read(buffer);

        OutputStream outStream = new FileOutputStream(outFile);
        outStream.write(buffer);
    }

    @Before
    public void driverSetup(){
        try{
            System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
            driver = new FirefoxDriver();
        }catch (Exception e){
            Assume.assumeNotNull(e.toString(), driver );
        }
    }

    @After
    public void tearDown(){
        driver.quit();
    }

    @Test
    public void accessGui() throws IOException {
        driver.get("http://127.0.0.1:" + PORT + "/admin");
        Assert.assertEquals("Service Virtualization mock admin", driver.getTitle());
    }

    @Test
    public void helpGuiOpenAndClose() throws IOException {
        driver.get("http://127.0.0.1:" + PORT + "/admin");
        By infoButton = By.id("infoButton");
        By helpSection = By.id("help");
        By closeButton = By.id("closeHelpButton");

        Assert.assertFalse(driver.findElement(helpSection).isDisplayed());
        driver.findElement(infoButton).click();
        Assert.assertTrue(driver.findElement(helpSection).isDisplayed());
        driver.findElement(helpSection).click();
        Assert.assertFalse(driver.findElement(helpSection).isDisplayed());
        driver.findElement(infoButton).click();
        Assert.assertTrue(driver.findElement(helpSection).isDisplayed());
        driver.findElement(closeButton).click();
        Assert.assertFalse(driver.findElement(helpSection).isDisplayed());
    }

    @Test
    public void pressAddPreparedResponseShouldOpenAdditionGui(){
        driver.get("http://127.0.0.1:" + PORT + "/admin");
        By addButton = By.id("addPreparedResponse");
        By addSection = By.id("new");
        By closeAdditionButton = By.id("cancelAddition");

        Assert.assertTrue(driver.findElement(addButton).isDisplayed());
        Assert.assertFalse(driver.findElement(addSection).isDisplayed());
        driver.findElement(addButton).click();
        Assert.assertTrue(driver.findElement(addSection).isDisplayed());
        driver.findElement(closeAdditionButton).click();
        Assert.assertFalse(driver.findElement(addSection).isDisplayed());
    }

    @Test
    public void listOfRegisteredShouldShowValues(){
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        HttpResponseToSend response = new HttpResponseToSend(202, "Oh yes");
        response.headers.add(new HttpHeader("CustomHeaderName", "CustomHeaderValue"));
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(response, new EndpointUrlFilter("/dummy")));
        driver.get("http://127.0.0.1:" + PORT + "/admin");
        By responseTable = By.id("registeredResponsesTable");
        Assert.assertNotNull(driver.findElement(responseTable).findElement(By.xpath("//td[contains(text(),'/dummy')]")));
        Assert.assertNotNull(driver.findElement(responseTable).findElement(By.xpath("//td[contains(text(),'direct')]")));
        Assert.assertNotNull(driver.findElement(responseTable).findElement(By.xpath("//td[contains(text(),'Oh yes')]")));
        Assert.assertNotNull(driver.findElement(responseTable).findElement(By.xpath("//td[contains(text(),'/dummy')]")));
        Assert.assertNotNull(driver.findElement(responseTable).findElement(By.xpath("//td[contains(text(),'CustomHeaderName')]")));
        Assert.assertNotNull(driver.findElement(responseTable).findElement(By.xpath("//td[contains(text(),'CustomHeaderValue')]")));
        Assert.assertNotNull(driver.findElement(responseTable).findElement(By.xpath("//td[text()='202']")));
    }

    @Test
    public void deleteButtonShouldDelete() throws InterruptedException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        HttpResponseToSend response = new HttpResponseToSend(200, "Oh yes");
        response.headers.add(new HttpHeader("CustomHeaderName", "CustomHeaderValue"));
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(response, new EndpointUrlFilter("/dummy")));
        driver.get("http://127.0.0.1:" + PORT + "/admin");
        int count = RegisteredPreparedHttpResponses.getInstance().registeredResponses.size();
        WebDriverWait wait = new WebDriverWait(driver, 100);
        By responseTable = By.id("registeredResponsesTable");
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(responseTable).findElement(By.tagName("button"))));
        driver.findElement(responseTable).findElement(By.tagName("button")).click();
        driver.switchTo().alert().accept();
        Thread.sleep(100);
        Assert.assertTrue(RegisteredPreparedHttpResponses.getInstance().registeredResponses.size() == count - 1);
    }

    @Test
    @Ignore
    public void addEmptyHeaderShouldMarkRed() throws InterruptedException {
        driver.get("http://127.0.0.1:" + PORT + "/admin");
        By addButton = By.id("addPreparedResponse");
        By headerNameField = By.id("responseheadername");
        By headerValueField = By.id("responseheadervalue");
        By addHeaderButton = By.id("addheaderbutton");

        driver.findElement(addButton).click();
        Assert.assertFalse(driver.findElement(headerNameField).getCssValue("background-color").equals("red"));
        driver.findElement(addHeaderButton).click();
        Assert.assertTrue("Actual: '" + driver.findElement(headerNameField).getCssValue("background-color") + "'", driver.findElement(headerNameField).getCssValue("background-color").equals("red"));
        Thread.sleep(400);
        Assert.assertFalse(driver.findElement(headerNameField).getCssValue("background-color").equals("red"));
    }
}
