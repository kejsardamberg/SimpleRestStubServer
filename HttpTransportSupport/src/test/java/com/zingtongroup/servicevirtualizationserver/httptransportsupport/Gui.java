package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

class Gui {

    private WebDriver driver;
    private int port;

    Gui(WebDriver driver, int port){
        this.port = port;
        this.driver = driver;
    }

    void openAdminGui(){
        driver.get("http://127.0.0.1:" + port + "/admin");
    }

    void openHelpByInfoButton(){
        driver.findElement(infoButton).click();
    }

    void verifyHelpIsVisible(boolean expectedDisplay){
        Assert.assertTrue(expectedDisplay == driver.findElement(helpSection).isDisplayed());
    }

    void clickHelpSection(){
        driver.findElement(helpSection).click();
    }

    void clickCloseHelpButton(){
        driver.findElement(closeButton).click();
    }
    void verifyHelpSectionIsVisible(){
        verifyHelpIsVisible(true);
    }

    void verifyHelpSectionIsNotVisible(){
        verifyHelpIsVisible(false);
    }

    static By infoButton = By.id("infoButton");
    static By helpSection = By.id("help");
    static By responseTable = By.id("registeredResponsesTable");
    static By addButton = By.id("addPreparedResponse");
    static By addSection = By.id("new");
    static By closeButton = By.id("closeHelpButton");

    @SuppressWarnings("SpellCheckingInspection")
    static class Add {
        static By closeAdditionButton = By.id("cancelAddition");
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
    }
}
