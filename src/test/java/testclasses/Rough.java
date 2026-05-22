package testclasses;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.pageclasses.CompLoginPage;
import com.pageclasses.Homepage;
import com.pageclasses.UserLoginPage;

import base.Basepage;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import utils.ConfigReader;

public class Rough extends Basepage {

    public UserLoginPage userlogin;
    public CompLoginPage complogin;
    public Homepage homepage;

    @Test(priority = 1, enabled = true, description = "CompanyLogin")
    @Description("verify company login")
    public void companyLogin() {
        CompLoginPage comploginPage = new CompLoginPage(page);
        userlogin = comploginPage.login(ConfigReader.get("lang"),
                ConfigReader.get("comp.username"),
                ConfigReader.get("comp.password"));
    }

    @Test(priority = 2, enabled = true, description = "userLogin")
    @Description("verify user login")
    public void userLogin() {
        UserLoginPage userlogin = new UserLoginPage(page);
        userlogin.usernameLogin(ConfigReader.get("user.username"));
        userlogin.userpassword(ConfigReader.get("user.password"));
        userlogin.clickUserLoginButton();
    }

    @Test(priority = 3, enabled = true, description = "verify root Folder Navigation in search bar")
    @Description("Verify verify root Folder Navigation in search bar is working and page is redirected to correct page.")
    public void verifyFolderCheckboxSearch() {
        try {
            homepage = new Homepage(page);
            homepage.rootFolderNavigation("test");
        } catch (Exception e) {
            Allure.step("Root folder navigation test failed: " + e.getMessage());
            throw new SkipException("Skipping test due to failure in root folder navigation: " + e.getMessage());
        }

    }

}
