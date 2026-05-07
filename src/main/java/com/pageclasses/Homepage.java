// ===============================
// Homepage.java
// ===============================

package com.pageclasses;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import base.Basepage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class Homepage extends Basepage {

    public Homepage(Page page) {
    }

    // Locators
    private static final String HYPERLINK = "//h4[text()='HyperLink']/preceding::input[contains(@src,'hyperLink')]";

    private static final String DRAWINGMGMT = "//h4[text()='Drawing Management']/preceding::img[contains(@src,'Digital')]";

    private static final String SOFTWARELIB = "//h4[text()='Software Library']/preceding::input[contains(@src,'Gaia')]";

    private static final String ALFAOFFICE = "//h4[text()='alfa Office']/preceding::input[contains(@src,'alfa Office')]";

    private static final String ALFACALENDER = "//h4[text()='alfaCalendar']/preceding::input[contains(@src,'calender')]";

    private static final String SETTINGS = "//h4[text()='Settings']/preceding::input[contains(@src,'Setting')]";

    private static final String SEARCHBAR = "//input[@placeholder='Search']";

    private static final String SEARCHICON = "//img[contains(@src,'ai-search.png')]/..";

    private static final String LOGOUTICON = "div[class*='user_login'] i[class*='sign-out']";

    private static final String ALFADOCKLOGO = "//img[contains(@src,'logo')]";

    // ==========================================
    // Homepage Icon Validation
    // ==========================================

    @Step("Verify Homepage Icons")
    public void homepageiconcheck() {

        page.waitForLoadState();

        String[] locators = {
                HYPERLINK,
                DRAWINGMGMT,
                SOFTWARELIB,
                ALFAOFFICE,
                ALFACALENDER,
                SETTINGS,
                SEARCHBAR,
                SEARCHICON,
                LOGOUTICON,
                ALFADOCKLOGO
        };

        for (String locator : locators) {

            assertThat(page.locator(locator)).isVisible();

            Allure.step("Verified icon : " + locator);
        }
    }

    // ==========================================
    // File Search
    // ==========================================

    @Step("Search File")
    public void searchfileinsearchbar(String fileName) {

        Locator searchBar = page.locator(SEARCHBAR);

        searchBar.waitFor();

        // Remove readonly
        searchBar.evaluate("element => element.removeAttribute('readonly')");

        // Click
        searchBar.evaluate("element => element.click()");

        // Fill value
        searchBar.fill(fileName);

        Allure.step("Entered file name : " + fileName);

        // Search
        searchBar.press("Enter");

        Allure.step("clicked enter button");

        page.waitForLoadState();

        Page viewerPage = openFirstSearchResult();

        validateViewerPage(viewerPage);

        closeViewerAndReturn(viewerPage);

        alfadocklogo();
    }

    Locator fileNameChkBox = page.locator("text=Filename");
    Locator attributesChkBox = page.locator("text=Attributes");
    Locator contentChkBox = page.locator("text=Content");

    public void selectSearchFilter(String filterName) {

        switch (filterName.toLowerCase()) {

            case "filename":

                // Enable Filename if not enabled
                if (!fileNameChkBox.locator("svg").isVisible()) {
                    fileNameChkBox.click();
                    Allure.step("Filename checkbox enabled");
                }

                // Disable Attributes if enabled
                if (attributesChkBox.locator("svg").isVisible()) {
                    attributesChkBox.click();
                    Allure.step("Attributes checkbox disabled");
                }

                // Disable Content if enabled
                if (contentChkBox.locator("svg").isVisible()) {
                    contentChkBox.click();
                    Allure.step("Content checkbox disabled");
                }

                break;

            case "attributes":

                // Enable Attributes
                if (!attributesChkBox.locator("svg").isVisible()) {
                    attributesChkBox.click();
                    Allure.step("Attributes checkbox enabled");
                }

                // Disable Filename
                if (fileNameChkBox.locator("svg").isVisible()) {
                    fileNameChkBox.click();
                    Allure.step("Filename checkbox disabled");
                }

                // Disable Content
                if (contentChkBox.locator("svg").isVisible()) {
                    contentChkBox.click();
                    Allure.step("Content checkbox disabled");
                }

                break;

            case "content":

                // Enable Content
                if (!contentChkBox.locator("svg").isVisible()) {
                    contentChkBox.click();
                    Allure.step("Content checkbox enabled");
                }

                // Disable Filename
                if (fileNameChkBox.locator("svg").isVisible()) {
                    fileNameChkBox.click();
                    Allure.step("Filename checkbox disabled");
                }

                // Disable Attributes
                if (attributesChkBox.locator("svg").isVisible()) {
                    attributesChkBox.click();
                    Allure.step("Attributes checkbox disabled");
                }

                break;

            default:
                throw new IllegalArgumentException("Invalid filter name: " + filterName);
        }
    }

    @Step("Filename Search")
    public void filenamesearch(String fileName) {

        Locator searchBar = page.locator(SEARCHBAR);

        searchBar.waitFor();

        // Click
        searchBar.click();

        page.waitForTimeout(2000);

        // Fill
        searchBar.fill(fileName);

        Allure.step("Entered attribute search text : " + fileName);

        // Dropdown
        Locator searchBarDropdown = page
                .locator("//button[@icon='fa-caret-down']//span[contains(@class,'fa-caret-down')]");

        searchBarDropdown.dispatchEvent("click");

        page.waitForTimeout(2000);

        selectSearchFilter("filename");

        // Search icon
        Locator searchIcon = page.locator(SEARCHICON);

        searchIcon.waitFor();

        searchIcon.click();

        Allure.step("Clicked search icon");

        page.waitForLoadState();

        Page viewerPage = openFirstSearchResult();

        validateViewerPage(viewerPage);

        closeViewerAndReturn(viewerPage);

        alfadocklogo();
    }

    @Step("Search File Using Filter")
    public void searchUsingFilter(String fileName, String filterName) {

        // ==========================================
        // Step 1 - Search Bar
        // ==========================================

        Locator searchBar = page.locator(SEARCHBAR);

        searchBar.waitFor();

        // Click search bar
        searchBar.click();

        page.waitForTimeout(2000);

        // Enter filename
        searchBar.fill(fileName);

        Allure.step("Entered search text : " + fileName);

        // ==========================================
        // Step 2 - Open Dropdown
        // ==========================================

        Locator searchBarDropdown = page.locator("//span[normalize-space()='ui-btn']");

        searchBarDropdown.evaluate("element => element.click()");

        Allure.step("Opened search dropdown");

        page.waitForTimeout(2000);

        // ==========================================
        // Step 3 - Select Filter
        // ==========================================

        selectSearchFilter(filterName);

        page.waitForTimeout(3000);

        // ==========================================
        // Step 4 - Click Search Icon
        // ==========================================

        Locator searchIcon = page.locator(SEARCHICON);

        searchIcon.evaluate("element => element.click()");

        Allure.step("Clicked search icon");

        page.waitForLoadState();

        // ==========================================
        // Step 5 - Open Search Result
        // ==========================================

        Page viewerPage = openFirstSearchResult();

        validateViewerPage(viewerPage);

        closeViewerAndReturn(viewerPage);

        // ==========================================
        // Step 6 - Navigate Homepage
        // ==========================================

        alfadocklogo();
    }

}