// ===============================
// Homepage.java
// ===============================

package com.pageclasses;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import base.Basepage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class Homepage extends Basepage {

        public WebDriver driver;

        public Homepage(Page page) {
                Homepage.page = page;
        }

        // ==========================================
        // Locators
        // ==========================================

        private static final String HYPERLINK = "xpath=//h4[text()='HyperLink']/preceding::input[contains(@src,'hyperLink')]";

        private static final String DRAWINGMGMT = "xpath=//h4[text()='Drawing Management']/preceding::img[contains(@src,'Digital')]";

        private static final String SOFTWARELIB = "xpath=//h4[text()='Software Library']/preceding::input[contains(@src,'Gaia')]";

        private static final String ALFAOFFICE = "xpath=//h4[text()='alfa Office']/preceding::input[contains(@src,'alfa Office')]";

        private static final String ALFACALENDER = "xpath=//h4[text()='alfaCalendar']/preceding::input[contains(@src,'calender')]";

        private static final String SETTINGS = "xpath=//h4[text()='Settings']/preceding::input[contains(@src,'Setting')]";

        private static final String SEARCHBAR = "input[placeholder='Search']";

        private static final String SEARCHICON = "xpath=(//img[contains(@src,'ai-search')])[1]";

        private static final String LOGOUTICON = "div[class*='user_login'] i[class*='sign-out']";

        private static final String ALFADOCKLOGO = "img[src*='logo']";

        private static final String SEARCHDROPDOWN = "xpath=//input[@placeholder='Search']/following::button[@icon='fa-caret-down'][1]";

        // ==========================================
        // Homepage Icon Validation
        // ==========================================

        @Step("Verify Homepage Icons")
        public void verifyHomepageIcons() {

                page.waitForLoadState(LoadState.DOMCONTENTLOADED);

                Map<String, String> homepageIcons = new LinkedHashMap<>();

                homepageIcons.put("HyperLink", HYPERLINK);
                homepageIcons.put("Drawing Management", DRAWINGMGMT);
                homepageIcons.put("Software Library", SOFTWARELIB);
                homepageIcons.put("alfa Office", ALFAOFFICE);
                homepageIcons.put("alfaCalendar", ALFACALENDER);
                homepageIcons.put("Settings", SETTINGS);
                homepageIcons.put("Search Bar", SEARCHBAR);
                homepageIcons.put("Search Icon", SEARCHICON);
                homepageIcons.put("Logout Icon", LOGOUTICON);
                homepageIcons.put("alfaDOCK Logo", ALFADOCKLOGO);

                for (Map.Entry<String, String> entry : homepageIcons.entrySet()) {

                        String elementName = entry.getKey();
                        String locatorValue = entry.getValue();

                        Locator element = page.locator(locatorValue);

                        try {

                                element.waitFor(new Locator.WaitForOptions()
                                                .setState(WaitForSelectorState.VISIBLE)
                                                .setTimeout(10000));

                                assertThat(element).isVisible();

                                Allure.step("Verified : " + elementName);

                                System.out.println("Verified : " + elementName);

                        } catch (Exception e) {

                                Allure.step("Failed : " + elementName);

                                System.out.println("Failed : " + elementName);

                                throw new RuntimeException(
                                                "Homepage element not visible : " + elementName,
                                                e);
                        }
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

                Allure.step("clicked enter button.");

                page.waitForLoadState();

                Page viewerPage = openFirstSearchResult();

                validateViewerPage(viewerPage);

                closeViewerAndReturn(viewerPage);

                alfadocklogo();
        }

        private static final String FILENAME_FILTER = "//label[normalize-space()='Filename']";
        private static final String ATTRIBUTES_FILTER = "//label[normalize-space()='Attributes']";
        private static final String CONTENT_FILTER = "//label[normalize-space()='Content']";

        private boolean isSearchFilterEnabled(String filterLabel) {

                Locator label = page.locator(filterLabel);

                label.waitFor();

                String inputId = (String) label.evaluate("element => element.getAttribute('for')");

                if (inputId == null || inputId.isBlank()) {
                        return label.locator("xpath=..").locator("svg").isVisible();
                }

                return (boolean) page.locator("#" + inputId).evaluate("element => element.checked");
        }

        private void clickSearchFilter(String filterLabel) {

                Locator label = page.locator(filterLabel);

                label.waitFor();

                String inputId = (String) label.evaluate("element => element.getAttribute('for')");

                if (inputId == null || inputId.isBlank()) {
                        label.evaluate("element => element.click()");
                        return;
                }

                page.locator("#" + inputId).evaluate("element => element.click()");
        }

        public void selectSearchFilter(String filterName) {

                switch (filterName.toLowerCase()) {

                        case "filename":

                                // Enable Filename if not enabled
                                if (!isSearchFilterEnabled(FILENAME_FILTER)) {
                                        clickSearchFilter(FILENAME_FILTER);
                                        Allure.step("Filename checkbox enabled");
                                }

                                // Disable Attributes if enabled
                                if (isSearchFilterEnabled(ATTRIBUTES_FILTER)) {
                                        clickSearchFilter(ATTRIBUTES_FILTER);
                                        Allure.step("Attributes checkbox disabled");
                                }

                                // Disable Content if enabled
                                if (isSearchFilterEnabled(CONTENT_FILTER)) {
                                        clickSearchFilter(CONTENT_FILTER);
                                        Allure.step("Content checkbox disabled");
                                }

                                break;

                        case "attributes":

                                // Enable Attributes
                                if (!isSearchFilterEnabled(ATTRIBUTES_FILTER)) {
                                        clickSearchFilter(ATTRIBUTES_FILTER);
                                        Allure.step("Attributes checkbox enabled");
                                }

                                // Disable Filename
                                if (isSearchFilterEnabled(FILENAME_FILTER)) {
                                        clickSearchFilter(FILENAME_FILTER);
                                        Allure.step("Filename checkbox disabled");
                                }

                                // Disable Content
                                if (isSearchFilterEnabled(CONTENT_FILTER)) {
                                        clickSearchFilter(CONTENT_FILTER);
                                        Allure.step("Content checkbox disabled");
                                }

                                break;

                        case "content":

                                // Enable Content
                                if (!isSearchFilterEnabled(CONTENT_FILTER)) {
                                        clickSearchFilter(CONTENT_FILTER);
                                        Allure.step("Content checkbox enabled");
                                }

                                // Disable Filename
                                if (isSearchFilterEnabled(FILENAME_FILTER)) {
                                        clickSearchFilter(FILENAME_FILTER);
                                        Allure.step("Filename checkbox disabled");
                                }

                                // Disable Attributes
                                if (isSearchFilterEnabled(ATTRIBUTES_FILTER)) {
                                        clickSearchFilter(ATTRIBUTES_FILTER);
                                        Allure.step("Attributes checkbox disabled");
                                }

                                break;

                        default:
                                throw new IllegalArgumentException("Invalid filter name: " + filterName);
                }
        }

        private void clickSearchIcon() {

                Locator searchIcon = page.locator(SEARCHICON).first();

                searchIcon.waitFor();

                searchIcon.scrollIntoViewIfNeeded();

                searchIcon.evaluate("element => (element.closest('button, a, [role=\"button\"]') || element).click()");

                Allure.step("Clicked search icon");
        }

        private void openSearchDropdown() {

                Locator searchDropdown = page.locator(SEARCHDROPDOWN).first();

                searchDropdown.waitFor();

                searchDropdown.evaluate("element => element.click()");

                Allure.step("Opened search dropdown");
        }

        @Step("Filename Search")
        public void filenamesearch(String fileName) {

                searchfileinsearchbar(fileName);

                // Dropdown
                page.waitForTimeout(2000);

                openSearchDropdown();

                page.waitForTimeout(2000);

                selectSearchFilter("filename");

                // Search icon
                clickSearchIcon();

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

                openSearchDropdown();

                page.waitForTimeout(2000);

                // ==========================================
                // Step 3 - Select Filter
                // ==========================================

                selectSearchFilter(filterName);

                page.waitForTimeout(1000);

                // ==========================================
                // Step 4 - Click Search Icon
                // ==========================================

                clickSearchIcon();

                page.waitForLoadState(LoadState.NETWORKIDLE);

                // ==========================================
                // Step 5 - Open Search Result
                // ==========================================

                Page viewerPage = openFirstSearchResult(30000);

                validateViewerPage(viewerPage);

                closeViewerAndReturn(viewerPage);

                // ==========================================
                // Step 6 - Navigate Homepage
                // ==========================================

                alfadocklogo();
        }

        @Step("Search File Using Filter")
        public void searchUsingFileInSearchDropdown(String fileName) {
                // Click search bar
                page.locator(SEARCHBAR).click();

                // Enter filename
                page.locator(SEARCHBAR).fill(fileName);

                Allure.step("Entered search text : " + fileName);

                // Open Dropdown
                openSearchDropdown();

                page.waitForTimeout(2000);

                // Select File or Folder
                selectFileCheckboxInSearchDropdown();

                page.waitForTimeout(3000);

                // Click Search Icon
                clickSearchIcon();

                page.waitForLoadState();

                // Open Search Result
                Page viewerPage = openFirstSearchResult();

                validateViewerPage(viewerPage);

                closeViewerAndReturn(viewerPage);

                // Navigate Homepage
                alfadocklogo();

        }

        @Step("Search folder Using Filter")
        public void searchUsingFolderInSearchDropdown(String fileName) {
                // Click search bar
                page.locator(SEARCHBAR).click();

                // Enter filename
                page.locator(SEARCHBAR).fill(fileName);

                Allure.step("Entered search text : " + fileName);

                // Open Dropdown
                openSearchDropdown();

                page.waitForTimeout(2000);

                // Select File or Folder
                selectFolderCheckboxInSearchDropdown();

                page.waitForTimeout(3000);

                // Click Search Icon
                clickSearchIcon();

                page.waitForLoadState();

                // Open Search Result
                openFirstFolder();

                Page viewerPage = openFirstSearchResult();
                validateViewerPage(viewerPage);
                closeViewerAndReturn(viewerPage);
                alfadocklogo();

        }

        public void selectFileCheckboxInSearchDropdown() {

                Locator filecheckbox = page.locator("xpath=//label[text()='FileType']/following::label[text()='File']");

                Allure.step("File checkbox is visible in search dropdown");
                System.out.println("File checkbox is visible in search dropdown");

                String classes = filecheckbox.getAttribute("class");
                System.out.println("File checkbox classes: " + classes);

                if (classes == null || !classes.contains("ui-label-active")) {

                        page.waitForTimeout(3000);

                        filecheckbox.evaluate("element => element.click()");

                        Allure.step("File checkbox selected in search dropdown");
                        System.out.println("File checkbox selected in search dropdown");

                } else {

                        Allure.step("File checkbox already selected");
                        System.out.println("File checkbox already selected");
                }
        }

        public void selectFolderCheckboxInSearchDropdown() {

                Locator folderCheckbox = page
                                .locator("xpath=//label[text()='FileType']/following::label[text()='Folder']");

                Allure.step("Folder checkbox is visible in search dropdown");
                System.out.println("Folder checkbox is visible in search dropdown");
                String classes = folderCheckbox.getAttribute("class");

                if (classes == null || !classes.contains("ui-label-active")) {

                        page.waitForTimeout(3000);

                        folderCheckbox.evaluate("element => element.click()");

                        Allure.step("Folder checkbox selected in search dropdown");
                        System.out.println("Folder checkbox selected in search dropdown");

                } else {

                        Allure.step("Folder checkbox already selected");
                        System.out.println("Folder checkbox already selected");
                }

        }

        public void rootFolderNavigation(String filename) {

                searchFilename(filename);
                selectFileCheckboxInSearchDropdown();
                clickSearchIcon();
                page.waitForLoadState(LoadState.NETWORKIDLE);
                selectFirstSearchResult();
                rootIcon();
                page.waitForLoadState(LoadState.NETWORKIDLE);
                page.waitForTimeout(5000);

        }
}
