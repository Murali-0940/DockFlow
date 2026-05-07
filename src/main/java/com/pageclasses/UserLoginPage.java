package com.pageclasses;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import base.Basepage;

public class UserLoginPage extends Basepage {

    public UserLoginPage(Page page) {
    }

    // Corrected locators for the User Login page
    private static final String USERNAME_FIELD = "#username";
    private static final String PASSWORD_FIELD = "#password";
    private static final String LOGIN_BUTTON = "#login";

    @Step("Enter username: {user}")
    public void usernameLogin(String user) {
        waitForVisible(USERNAME_FIELD);
        page.locator(USERNAME_FIELD).clear();
        fill(USERNAME_FIELD, user);
        Allure.step("Entered username : " + user);
        System.out.println("Entered username : " + user);
    }

    @Step("Enter Password: {pass}")
    public void userpassword(String pass) {
        waitForVisible(PASSWORD_FIELD);
        page.locator(PASSWORD_FIELD).clear();
        fill(PASSWORD_FIELD, pass);
        Allure.step("Entered password : " + pass);
        System.out.println("Entered password : " + pass);
    }

    @Step("User Login Button Click")
    public void clickUserLoginButton() {
        waitForVisible(LOGIN_BUTTON);
        click(LOGIN_BUTTON);
        page.waitForLoadState();
        Allure.step("User Login Button Clicked");
        System.out.println("User Login Button Clicked : " + page.url());
    }

    public Homepage userlogin(String user, String pass) {
        usernameLogin(user);
        userpassword(pass);
        clickUserLoginButton();
        attachScreenshot("After User Login");
        return new Homepage(page);
    }
}
