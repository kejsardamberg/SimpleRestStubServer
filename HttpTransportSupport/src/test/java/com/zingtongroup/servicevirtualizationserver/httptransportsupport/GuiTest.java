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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;

public class GuiTest extends ServerTestBase {

    WebDriver driver;

    static By infoButton = By.id("infoButton");
    static By helpSection = By.id("help");
    static By closeButton = By.id("closeHelpButton");
    static By addButton = By.id("addPreparedResponse");
    static By addSection = By.id("new");
    static By closeAdditionButton = By.id("cancelAddition");
    static By responseTable = By.id("registeredResponsesTable");
    static By headerNameField = By.id("responseheadername");
    static By headerValueField = By.id("responseheadervalue");
    static By addHeaderButton = By.id("addheaderbutton");
    static By responseCodeField = By.id("responsecode");
    static By bodyContentTypeDropDown = By.id("bodycontenttypedropdown");
    static By bodyContentFilePathField = By.id("bodycontentfilepath");
    static By bodyContentInputArea = By.id("responsebodycontent");
    static By filterTypeDropdown = By.id("triggertype");
    static By filterField1 = By.id("field1");
    static By filterField2 = By.id("field2");
    static By submitNewPreparedButton = By.id("addfilter");
    static By addFilterButton = By.id("addRequestFilter");

    static void extractFile(String fileName) throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        File outFile = new File(fileName);
        if(outFile.exists()) return;
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        OutputStream outStream = new FileOutputStream(outFile);
        outStream.write(buffer);
    }

    @BeforeClass
    public static void extractFirefoxDriver() throws IOException {
        extractFile("geckodriver.exe");
        extractFile("helloworldcontent.txt");
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
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(responseTable).findElement(By.tagName("button"))));
        driver.findElement(responseTable).findElement(By.tagName("button")).click();
        driver.switchTo().alert().accept();
        Thread.sleep(100);
        Assert.assertTrue(RegisteredPreparedHttpResponses.getInstance().registeredResponses.size() == count - 1);
    }

    @Test
    public void deleteButtonShouldDeleteResponsesRegisteredFromGui() throws InterruptedException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        driver.get("http://127.0.0.1:" + PORT + "/admin");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.findElement(addButton).click();
        Assert.assertTrue(RegisteredPreparedHttpResponses.getInstance().registeredResponses.size() == 0);
        driver.findElement(submitNewPreparedButton).click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(responseTable).findElement(By.tagName("button"))));
        Assert.assertTrue(String.valueOf(RegisteredPreparedHttpResponses.getInstance().registeredResponses.size()), RegisteredPreparedHttpResponses.getInstance().registeredResponses.size() == 1);
        driver.findElement(responseTable).findElement(By.tagName("button")).click(); //DELETE button
        driver.switchTo().alert().accept(); //Confirm dialog
        Thread.sleep(100);
        Assert.assertTrue(RegisteredPreparedHttpResponses.getInstance().registeredResponses.size() == 0);
    }

    @Test
    @Ignore
    public void addEmptyHeaderShouldMarkRed() throws InterruptedException {
        driver.get("http://127.0.0.1:" + PORT + "/admin");

        driver.findElement(addButton).click();
        Assert.assertFalse(driver.findElement(headerNameField).getCssValue("background-color").equals("red"));
        driver.findElement(addHeaderButton).click();
        Assert.assertTrue("Actual: '" + driver.findElement(headerNameField).getCssValue("background-color") + "'", driver.findElement(headerNameField).getCssValue("background-color").equals("red"));
        Thread.sleep(400);
        Assert.assertFalse(driver.findElement(headerNameField).getCssValue("background-color").equals("red"));
    }

    @Test
    public void registeringFileBasedResponseBodyShouldWorkFromGui() throws FileNotFoundException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        driver.get("http://127.0.0.1:" + PORT + "/admin");
        driver.findElement(addButton).click();
        Select dropdown = new Select(driver.findElement(bodyContentTypeDropDown));
        dropdown.selectByValue("file");
        driver.findElement(bodyContentFilePathField).sendKeys("helloworldcontent.txt");
        driver.findElement(submitNewPreparedButton).click();

        String response = getAndGetResponse("/safd").body;
        Assert.assertTrue("Hollow world".equals(response));
    }

    @Test
    public void registerPortShouldWork() throws FileNotFoundException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        driver.get("http://127.0.0.1:" + PORT + "/admin");
        driver.findElement(addButton).click();
        driver.findElement(responseCodeField).clear();
        driver.findElement(responseCodeField).sendKeys("404");
        driver.findElement(submitNewPreparedButton).click();
        Assert.assertNotNull(driver.findElement(responseTable).findElement(By.xpath("//td[text()='404']")));
        HttpResponse response = getAndGetResponse("/sfs");
        Assert.assertNotNull(response);
        Assert.assertTrue(response.responseCode == 404);
        Assert.assertTrue(response.body == null);
    }
}
