package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

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
    Gui gui;

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
        gui = new Gui(driver, PORT);
    }

    @After
    public void tearDown(){
        driver.quit();
    }

    @Test
    public void accessGui() throws IOException {
        gui.openAdminGui();
        Assert.assertEquals("Service Virtualization mock admin", driver.getTitle());
    }

    @Test
    public void helpGuiOpenAndClose() throws IOException {
        gui.openAdminGui();
        gui.verifyHelpSectionIsNotVisible();
        gui.openHelpByInfoButton();
        gui.verifyHelpSectionIsVisible();
        gui.clickHelpSection();
        gui.verifyHelpSectionIsNotVisible();
        gui.openHelpByInfoButton();
        gui.verifyHelpSectionIsVisible();
        gui.clickCloseHelpButton();
        gui.verifyHelpSectionIsNotVisible();
    }

    @Test
    public void pressAddPreparedResponseShouldOpenAdditionGui(){
        gui.openAdminGui();
        Assert.assertTrue(driver.findElement(gui.addButton).isDisplayed());
        Assert.assertFalse(driver.findElement(gui.addSection).isDisplayed());
        driver.findElement(gui.addButton).click();
        Assert.assertTrue(driver.findElement(gui.addSection).isDisplayed());
        driver.findElement(Gui.Add.closeAdditionButton).click();
        Assert.assertFalse(driver.findElement(gui.addSection).isDisplayed());
    }

    @Test
    public void listOfRegisteredShouldShowValues(){
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        HttpResponseToSend response = new HttpResponseToSend(202, "Oh yes");
        response.headers.add(new HttpHeader("CustomHeaderName", "CustomHeaderValue"));
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(response, new EndpointUrlFilter("/dummy")));
        driver.get("http://127.0.0.1:" + PORT + "/admin");
        Assert.assertNotNull(driver.findElement(gui.responseTable).findElement(By.xpath("//td[contains(text(),'/dummy')]")));
        Assert.assertNotNull(driver.findElement(gui.responseTable).findElement(By.xpath("//td[contains(text(),'direct')]")));
        Assert.assertNotNull(driver.findElement(gui.responseTable).findElement(By.xpath("//td[contains(text(),'Oh yes')]")));
        Assert.assertNotNull(driver.findElement(gui.responseTable).findElement(By.xpath("//td[contains(text(),'/dummy')]")));
        Assert.assertNotNull(driver.findElement(gui.responseTable).findElement(By.xpath("//td[contains(text(),'CustomHeaderName')]")));
        Assert.assertNotNull(driver.findElement(gui.responseTable).findElement(By.xpath("//td[contains(text(),'CustomHeaderValue')]")));
        Assert.assertNotNull(driver.findElement(gui.responseTable).findElement(By.xpath("//td[text()='202']")));
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
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(gui.responseTable).findElement(By.tagName("button"))));
        driver.findElement(gui.responseTable).findElement(By.tagName("button")).click();
        driver.switchTo().alert().accept();
        Thread.sleep(100);
        Assert.assertTrue(RegisteredPreparedHttpResponses.getInstance().registeredResponses.size() == count - 1);
    }

    @Test
    public void deleteButtonShouldDeleteResponsesRegisteredFromGui() throws InterruptedException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        driver.get("http://127.0.0.1:" + PORT + "/admin");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.findElement(gui.addButton).click();
        Assert.assertTrue(RegisteredPreparedHttpResponses.getInstance().registeredResponses.size() == 0);
        driver.findElement(Gui.Add.submitNewPreparedButton).click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(gui.responseTable).findElement(By.tagName("button"))));
        Assert.assertTrue(String.valueOf(RegisteredPreparedHttpResponses.getInstance().registeredResponses.size()), RegisteredPreparedHttpResponses.getInstance().registeredResponses.size() == 1);
        driver.findElement(gui.responseTable).findElement(By.tagName("button")).click(); //DELETE button
        driver.switchTo().alert().accept(); //Confirm dialog
        Thread.sleep(100);
        Assert.assertTrue(RegisteredPreparedHttpResponses.getInstance().registeredResponses.size() == 0);
    }

    @Test
    @Ignore
    public void addEmptyHeaderShouldMarkRed() throws InterruptedException {
        driver.get("http://127.0.0.1:" + PORT + "/admin");

        driver.findElement(gui.addButton).click();
        Assert.assertFalse(driver.findElement(Gui.Add.headerNameField).getCssValue("background-color").equals("red"));
        driver.findElement(Gui.Add.addHeaderButton).click();
        Assert.assertTrue("Actual: '" + driver.findElement(Gui.Add.headerNameField).getCssValue("background-color") + "'", driver.findElement(Gui.Add.headerNameField).getCssValue("background-color").equals("red"));
        Thread.sleep(400);
        Assert.assertFalse(driver.findElement(Gui.Add.headerNameField).getCssValue("background-color").equals("red"));
    }

    @Test
    public void registeringFileBasedResponseBodyShouldWorkFromGui() throws FileNotFoundException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        driver.get("http://127.0.0.1:" + PORT + "/admin");
        driver.findElement(gui.addButton).click();
        Select dropdown = new Select(driver.findElement(Gui.Add.bodyContentTypeDropDown));
        dropdown.selectByValue("file");
        driver.findElement(Gui.Add.bodyContentFilePathField).sendKeys("helloworldcontent.txt");
        driver.findElement(Gui.Add.submitNewPreparedButton).click();

        String response = getAndGetResponse("/safd").body;
        Assert.assertTrue("Hollow world".equals(response));
    }

    @Test
    public void registerPortShouldWork() throws FileNotFoundException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        driver.get("http://127.0.0.1:" + PORT + "/admin");
        driver.findElement(gui.addButton).click();
        driver.findElement(Gui.Add.responseCodeField).clear();
        driver.findElement(Gui.Add.responseCodeField).sendKeys("404");
        driver.findElement(Gui.Add.submitNewPreparedButton).click();
        Assert.assertNotNull(driver.findElement(gui.responseTable).findElement(By.xpath("//td[text()='404']")));
        HttpResponse response = getAndGetResponse("/sfs");
        Assert.assertNotNull(response);
        Assert.assertTrue(response.responseCode == 404);
        Assert.assertTrue(response.body == null);
    }
}
