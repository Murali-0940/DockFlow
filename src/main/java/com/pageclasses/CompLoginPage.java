package com.pageclasses;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import base.Basepage;

public class CompLoginPage extends Basepage {

    public CompLoginPage(Page page) {
    }

    // Locators
    private static final String COMP_USER = "//input[@id='username']";
    private static final String COMP_PASS = "//input[@id='password']";
    private static final String COMP_LOGIN = "//button[@id='logmein']";

    @Step("Select language: {lang}")
    public void selectlang(String lang) {
        selectOption(lang);
    }

    @Step("Enter company username: {user}")
    public void enterCompUser(String user) {
        fill(COMP_USER, user);
        Allure.addAttachment("Entered Username", user);
        Allure.step("Entered Username : " + user);
        System.out.println("Entered Username : " + user);
    }

    @Step("Enter company password")
    public void enterCompPass(String pass) {
        fill(COMP_PASS, pass);
        Allure.addAttachment("Entered Password", "****");
        Allure.step("Entered Password : " + pass);
        System.out.println("Entered Password : " + pass);
    }

    @Step("Click company login button")
    public void clickCompLogin() {
        click(COMP_LOGIN);
        Allure.step("Company Login Button Clicked");
        System.out.println("Company Login Button Clicked : " + page.url());
    }

    @Step("Company Login with username: {user}")
    public UserLoginPage login(String lang, String user, String pass) {
        page.waitForLoadState();
        selectlang(lang);
        enterCompUser(user);
        enterCompPass(pass);
        clickCompLogin();
        waitForVisible("#login");
        attachScreenshot("After Company Login");
        return new UserLoginPage(page);
    }
}
