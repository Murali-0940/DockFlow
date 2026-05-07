package testclasses;

import com.listeners.AllureListener;
import com.pageclasses.CompLoginPage;
import com.pageclasses.Homepage;
import com.pageclasses.UserLoginPage;

import base.Basepage;
import io.qameta.allure.*;
import utils.ConfigReader;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Epic("Alfadock Pack Automation")
@Feature("Homepage Features")
@Listeners(AllureListener.class)
public class HomepageTest extends Basepage {

    public UserLoginPage userlogin;
    public CompLoginPage complogin;
    public Homepage homepage;

    @Test(priority = 1, description = "CompanyLogin")
    @Description("verify company login")
    public void companyLogin() {
        complogin = new CompLoginPage(page);
        complogin.login(ConfigReader.get("lang"),
                ConfigReader.get("comp.username"),
                ConfigReader.get("comp.password"));
    }

    @Test(priority = 2, description = "userLogin")
    @Description("verify user login")
    public void userLogin() {
        UserLoginPage userlogin = new UserLoginPage(page);
        userlogin.usernameLogin(ConfigReader.get("user.username"));
        userlogin.userpassword(ConfigReader.get("user.password"));
        userlogin.clickUserLoginButton();
    }

    @Test(priority = 3, description = "Verify Homepage Icons")
    @Description("Verify that all required icons and links are visible on the main dashboard.")
    public void verifyHomepage() {
        homepage = new Homepage(page);
        homepage.homepageiconcheck(); // 🔥 clean usage
    }

    @Test(priority = 4, description = "Search pdf in searchbar")
    @Description("Verify that the search bar is working and the result is loading.")
    public void verifyfileSearch() {
        homepage = new Homepage(page);
        homepage.searchfileinsearchbar(".pdf");
    }

    @Test(priority = 5, description = "Search a3dasm in searchbar")
    @Description("Verify that the search bar is working and the result is loading.")
    public void verifya3dfileSearch() {
        homepage = new Homepage(page);
        homepage.searchfileinsearchbar(".a3dasm");
    }

    @Test(priority = 6, description = "Search a3dprt in searchbar")
    @Description("Verify that the search bar is working and the result is loading.")
    public void verifya3dprtfileSearch() {
        homepage = new Homepage(page);
        homepage.searchfileinsearchbar(".a3dprt");
    }

    @Test(priority = 7, description = "Search jpg in searchbar")
    @Description("Verify that the search bar is working and the result is loading.")
    public void verifyjpgfileSearch() {
        homepage = new Homepage(page);
        homepage.searchfileinsearchbar(".jpg");
    }

    @Test(priority = 7, description = "Search tiff in searchbar")
    @Description("Verify that the search bar is working and the result is loading.")
    public void verifytiffileSearch() {
        homepage = new Homepage(page);
        homepage.searchfileinsearchbar(".tiff");
    }

    @Test(priority = 8, description = "Search dwg in searchbar")
    @Description("Verify that the search bar is working and the result is loading.")
    public void verifydwgfileSearch() {
        homepage = new Homepage(page);
        homepage.searchfileinsearchbar(".dwg");
    }

    @Test(priority = 9, description = "Search dxf in searchbar")
    @Description("Verify that the search bar is working and the result is loading.")
    public void verifydxfleSearch() {
        homepage = new Homepage(page);
        homepage.searchfileinsearchbar(".dxf");
    }

    @Test(priority = 10, description = "Search pptx in searchbar")
    @Description("Verify that the search bar is working and the result is loading.")
    public void verifypptxfileSearch() {
        homepage = new Homepage(page);
        homepage.searchfileinsearchbar(".pptx");
    }

    @Test(priority = 11, description = "Search xlsx in searchbar")
    @Description("Verify that the search bar is working and the result is loading.")
    public void verifyxlsxfileSearch() {
        homepage = new Homepage(page);
        homepage.searchfileinsearchbar(".xlsx");
    }

    @Test(priority = 12, description = "Search docx in searchbar")
    @Description("Verify that the search bar is working and the result is loading.")
    public void verifydocxfileSearch() {
        homepage = new Homepage(page);
        homepage.searchfileinsearchbar(".docx");
    }

    @Test(priority = 13, description = "Search csv in searchbar")
    @Description("Verify that the search bar is working and the result is loading.")
    public void verifycsvfileSearch() {
        homepage = new Homepage(page);
        homepage.searchfileinsearchbar(".csv");
    }

    @Test(priority = 14, description = "Filename Search in searchdropdown")
    @Description("Verify that the attribute search is working and the result is loading.")
    public void verifyFilenameSearch() {
        homepage = new Homepage(page);
        homepage.filenamesearch("20260317163148.pdf");
    }

    @Test(priority = 15, description = "attributes search in searchdropdown")
    @Description("Verify that the attribute search is working and the result is loading.")
    public void attributesearch() {
        homepage = new Homepage(page);
        homepage.searchUsingFilter("20260317163148.pdf", "Attributes");
    }

    @Test(priority = 16, description = "content search in searchdropdown")
    @Description("Verify that the content search is working and the result is loading.")
    public void contentserch() {
        homepage = new Homepage(page);
        homepage.searchUsingFilter("test", "Content");
    }

}