package base;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import utils.ConfigReader;

public class Basepage {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext context;
    protected static Page page;

    @BeforeSuite
    public void setup() {

        System.out.println("🚀 SETUP STARTED");

        playwright = Playwright.create();

        String browsername = ConfigReader.get("browser");
        boolean isheadless = ConfigReader.getBoolean("headless");

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(isheadless)
                .setArgs(Arrays.asList("--start-maximized"));

        switch (browsername.toLowerCase()) {

            case "chromium":
                browser = playwright.chromium().launch(options);
                break;

            case "firefox":
                browser = playwright.firefox().launch(options);
                break;

            case "webkit":
                browser = playwright.webkit().launch(options);
                break;

            default:
                throw new RuntimeException("❌ Invalid browser: " + browsername);
        }

        context = browser.newContext(
                new Browser.NewContextOptions().setViewportSize(null));

        page = context.newPage();

        page.navigate(ConfigReader.get("base.url"));

        page.waitForLoadState();
    }

    @AfterSuite
    public void teardown() {

        System.out.println("🧹 Closing browser");

        if (browser != null)
            browser.close();

        if (playwright != null)
            playwright.close();
    }

    public Page getPage() {
        return page;
    }

    // ==========================================
    // Reusable Methods
    // ==========================================

    @Step("Click on element: {selector}")
    public void click(String selector) {
        page.locator(selector).click();
    }

    @Step("Fill text '{text}'")
    public void fill(String selector, String text) {
        page.locator(selector).fill(text);
    }

    @Step("Wait for element")
    public void waitForVisible(String selector) {
        page.locator(selector).waitFor();
    }

    @Step("Press key")
    public void pressKey(String selector, String key) {
        page.locator(selector).press(key);
    }

    // ==========================================
    // Screenshot Utility
    // ==========================================

    @Step("Attach screenshot")
    public void attachScreenshot(Page targetPage, String name) {

        byte[] screenshot = targetPage.screenshot();

        InputStream is = new ByteArrayInputStream(screenshot);

        Allure.addAttachment(name, "image/png", is, "png");
    }

    @Step("Attach screenshot")
    public void attachScreenshot(String name) {

        attachScreenshot(page, name);
    }

    // ==========================================
    // Open First Search Result
    // ==========================================

    public Page openFirstSearchResult() {

        page.waitForTimeout(2000);

        Locator firstResult = page.locator("(//div[@class='imageDiv'])[1]");

        Allure.step("first file search result located");
        System.out.println("first file search result located");

        // Check for results with a short timeout before committing
        try {
            firstResult.waitFor(
                    new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(10000));
        } catch (TimeoutError e) {
            attachScreenshot("No Search Results");
            Allure.step("⚠️ No search results found on page: " + page.url());
            System.out.println("⚠️ No search results found : " + page.url());
            return null;
        }

        attachScreenshot("Search Results");

        Allure.step("Search results loaded successfully");

        System.out.println(
                "Search results loaded successfully : " + page.url());

        Page viewerPage = page.waitForPopup(() -> firstResult.dblclick());

        viewerPage.waitForLoadState(LoadState.NETWORKIDLE);

        Allure.step("Opened first search result");

        System.out.println(
                "Opened first search result : " + viewerPage.url());

        return viewerPage;
    }

    public Page openFirstSearchResult(int timeout) {

        Locator firstResult = page.locator("(//div[@class='imageDiv'])[1]");

        // Check for results with a short timeout before committing
        try {
            firstResult.waitFor(
                    new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(timeout));
        } catch (TimeoutError e) {
            attachScreenshot("No Search Results");
            Allure.step("⚠️ No search results found on page: " + page.url());
            System.out.println("⚠️ No search results found : " + page.url());
            return null;
        }

        attachScreenshot("Search Results");

        Allure.step("Search results loaded successfully");

        System.out.println(
                "Search results loaded successfully : " + page.url());

        Page viewerPage = page.waitForPopup(() -> firstResult.dblclick());

        viewerPage.waitForLoadState(LoadState.NETWORKIDLE);

        Allure.step("Opened first search result");

        System.out.println(
                "Opened first search result : " + viewerPage.url());

        return viewerPage;
    }

    public Page openFirstFolder() {

        Locator firstFolder = page.locator(".imageDivSmall").first();

        try {

            firstFolder.waitFor(
                    new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(10000));

            attachScreenshot("Folder Search Results");

            Allure.step("Folder search results loaded successfully");

            System.out.println(
                    "Folder search results loaded successfully : " + page.url());

            // Open folder
            Page folderPage = page.waitForPopup(() -> firstFolder.dblclick());

            folderPage.waitForLoadState(LoadState.NETWORKIDLE);

            Allure.step("Opened first folder");

            System.out.println(
                    "Opened first folder : " + folderPage.url());

            // Bring folder page to front
            folderPage.bringToFront();

            // Open first file inside folder
            Page viewerPage = openFirstSearchResult();

            // Return actual viewer page
            return viewerPage;

        } catch (TimeoutError e) {

            attachScreenshot("No Folder Results");

            Allure.step("⚠️ No folder results found on page: " + page.url());

            System.out.println("⚠️ No folder results found : " + page.url());

            return null;
        }
    }

    // ==========================================
    // Validate Viewer
    // ==========================================

    public void validateViewerPage(Page viewerPage) {

        viewerPage.waitForLoadState(LoadState.NETWORKIDLE);

        String currentUrl = viewerPage.url();

        if (currentUrl.contains("pdfviewer")) {

            Allure.step("PDF Viewer opened");

            System.out.println(
                    "PDF Viewer opened : " + currentUrl);

        } else if (currentUrl.contains("a3dviewer")) {

            Allure.step("A3D Viewer opened");

            System.out.println(
                    "A3D Viewer opened : " + currentUrl);

        } else if (currentUrl.contains("drawing")) {

            Allure.step("Drawing Viewer opened");

            System.out.println(
                    "Drawing Viewer opened : " + currentUrl);

        } else if (currentUrl.contains("csvviewer")) {

            Allure.step("CSV Viewer opened");

            System.out.println(
                    "CSV Viewer opened : " + currentUrl);

        } else {

            attachScreenshot(viewerPage, "Unsupported Viewer");

            viewerPage.close();

            throw new RuntimeException(
                    "Unsupported Viewer URL : " + currentUrl);
        }

        attachScreenshot(viewerPage, "Viewer Page");
    }

    // ==========================================
    // Close Viewer And Return
    // ==========================================

    public void closeViewerAndReturn(Page viewerPage) {

        viewerPage.waitForTimeout(3000);

        viewerPage.close();

        Allure.step("Closed Viewer Page");

        System.out.println("Closed Viewer Page");

        page.bringToFront();

        page.waitForTimeout(1000);
    }

    // ==========================================
    // Navigate To Homepage
    // ==========================================

    public void alfadocklogo() {

        String ALFADOCKLOGO = "xpath=//img[contains(@src,'logo')]";

        click(ALFADOCKLOGO);

        page.waitForLoadState();

        String currentPage = page.url();

        if (currentPage.contains("home")) {

            Allure.step("Homepage loaded successfully");

            System.out.println(
                    "Homepage loaded successfully : " + currentPage);

        } else {

            throw new RuntimeException(
                    "Homepage not loaded : " + currentPage);
        }
    }

    public void selectOption(String value) {

        String lang = "xpath=//select[@id='mySelect']";

        Locator langSelector = page.locator(lang);

        langSelector.selectOption(value);

        Allure.step("Selected option : " + value);

        System.out.println("Selected option : " + value);
    }

}