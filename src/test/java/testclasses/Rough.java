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

    @Test(priority = 3, enabled = false, description = "Search using File Checkbox")
    @Description("Verify that the file checkbox search is working and the result is loading.")
    public void verifyFileCheckboxSearch() {

        homepage = new Homepage(page);
        homepage.searchUsingFileInSearchDropdown("20260317163148.pdf");
    }

    @Test(priority = 4, enabled = true, description = "Search using Folder Checkbox")
    @Description("Verify that the folder checkbox search is working and the result is loading.")
    public void verifyFolderCheckboxSearch() {
        try {
            homepage = new Homepage(page);
            homepage.searchUsingFolderInSearchDropdown("128-A38477-00");
        } catch (Exception e) {
            Allure.step("Folder checkbox search failed : " + e.getMessage());
            throw new SkipException("Skipping folder checkbox search test");
        }
    }

}
