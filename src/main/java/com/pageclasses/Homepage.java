// ===============================
// Homepage.java
// ===============================

package com.pageclasses;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import base.Basepage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.openqa.selenium.WebDriver;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class Homepage extends Basepage {

        public WebDriver driver;

        public Homepage(Page page) {
                Homepage.page = page;
        }

        // Locators
        private static final String HYPERLINK = "//h4[text()='HyperLink']/preceding::input[contains(@src,'hyperLink')]";

        private static final String DRAWINGMGMT = "//h4[text()='Drawing Management']/preceding::img[contains(@src,'Digital')]";

        private static final String SOFTWARELIB = "//h4[text()='Software Library']/preceding::input[contains(@src,'Gaia')]";

        private static final String ALFAOFFICE = "//h4[text()='alfa Office']/preceding::input[contains(@src,'alfa Office')]";

        private static final String ALFACALENDER = "//h4[text()='alfaCalendar']/preceding::input[contains(@src,'calender')]";

        private static final String SETTINGS = "//h4[text()='Settings']/preceding::input[contains(@src,'Setting')]";

        private static final String SEARCHBAR = "//input[@placeholder='Search']";

        private static final String SEARCHDROPDOWN = SEARCHBAR + "/following::button[@icon='fa-caret-down'][1]";

        private static final String SEARCHICON = SEARCHBAR + "/following::img[contains(@src,'ai-search')][1]";

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

                Locator searchBar = page.locator(SEARCHBAR);

                searchBar.waitFor();

                // Click
                searchBar.click();

                page.waitForTimeout(2000);

                // Fill
                searchBar.fill(fileName);

                Allure.step("Entered attribute search text : " + fileName);

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
                Page viewerPage = openFirstSearchResult();

                validateViewerPage(viewerPage);

                closeViewerAndReturn(viewerPage);

                // Navigate Homepage
                alfadocklogo();

        }

        String filecheckbox = "//p-checkbox[@value='File']//div[contains(@class,'ui-chkbox-box')]";

        private void selectFileCheckboxInSearchDropdown() {

                Locator checkbox = page.locator(filecheckbox);

                checkbox.waitFor();

                String classes = checkbox.getAttribute("class");

                if (!classes.contains("ui-state-active")) {

                        checkbox.click();

                        Allure.step("File checkbox selected in search dropdown");

                } else {

                        Allure.step("File checkbox already selected");
                }
        }

        String foldercheckbox = "//p-checkbox[@value='Folder']//div[contains(@class,'ui-chkbox-box')]";

        private void selectFolderCheckboxInSearchDropdown() {

                Locator checkbox = page.locator(foldercheckbox);

                checkbox.waitFor();

                String classes = checkbox.getAttribute("class");

                if (!classes.contains("ui-state-active")) {

                        checkbox.click();

                        Allure.step("Folder checkbox selected in search dropdown");

                } else {

                        Allure.step("Folder checkbox already selected");
                }
        }
}
