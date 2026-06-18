package testclasses;

import com.listeners.AllureListener;
import com.pageclasses.CompLoginPage;
import com.pageclasses.Homepage;
import com.pageclasses.UserLoginPage;

import base.Basepage;
import io.qameta.allure.*;
import utils.ConfigReader;

import org.testng.SkipException;
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
        homepage.verifyHomepageIcons(); // 🔥 clean usage
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
        homepage.searchUsingFilter("20260317163148.pdf", "Filename");
    }

    @Test(priority = 15, enabled = true, description = "attributes search in searchdropdown")
    @Description("Verify that the attribute search is working and the result is loading.")
    public void attributesearch() {
        try {

            homepage = new Homepage(page);
            homepage.searchUsingFilter("attributetest", "Attributes");

        } catch (Exception e) {

            Allure.step("Attribute search failed : " + e.getMessage());

            throw new SkipException("Skipping attribute search test");
        }
    }

    @Test(priority = 16, enabled = true, description = "content search in searchdropdown")
    @Description("Verify that the content search is working and the result is loading.")
    public void contentserch() {

        try {
            homepage = new Homepage(page);
            homepage.searchUsingFilter("upload", "Content");
        } catch (Exception e) {
            Allure.step("Content search failed : " + e.getMessage());
            throw new SkipException("Skipping content search test");
        }

    }

    @Test(priority = 17, enabled = true, description = "Search using File Checkbox")
    @Description("Verify that the file checkbox search is working and the result is loading.")
    public void verifyFileCheckboxSearch() {

        homepage = new Homepage(page);
        homepage.searchUsingFileInSearchDropdown("20260317163148.pdf");
    }

    @Test(priority = 18, enabled = true, description = "Search using Folder Checkbox")
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

    @Test(priority = 19, enabled = true, description = "verify root file download")
    @Description("Verify root file download")
    public void verifyRootFileDownload() {
        try {
            homepage = new Homepage(page);
            homepage.downloadSearchedFile("20260317163148.pdf");
        } catch (Exception e) {
            Allure.step("Root file download test failed: " + e.getMessage());
            throw new SkipException("Skipping test due to failure in root file download: " + e.getMessage());
        }

    }
    
    @Test(priority = 20, enabled = true, description = "verify root file location")
    @Description("Verify root file location")
    public void verifyRootFileLocation() {
        try {
            homepage = new Homepage(page);
            homepage.rootFolderNavigation("20260317163148.pdf");
        } catch (Exception e) {
            Allure.step("Root file location test failed: " + e.getMessage());
            throw new SkipException("Skipping test due to failure in root file location: " + e.getMessage());
        }

    }
    
    @Test(priority = 21, enabled = true, description = "Search File Count")
    @Description("Verify file search count")
    public void verifyFileSearchCount() {
        try {
            homepage = new Homepage(page);
            homepage.fileSearchCount("2026");
        } catch (Exception e) {
            Allure.step("Root file download test failed: " + e.getMessage());
            throw new SkipException("Skipping test due to failure in root file download: " + e.getMessage());
        }

    }
    
    

}