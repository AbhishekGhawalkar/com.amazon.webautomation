package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriverBase {
	public static Workbook workbook;
	public static Sheet sheet;
	public static String testScenarioName;
	public static HashMap<String, Integer> testRowMap = new HashMap<>();
	public static final String masterUIAuto = "Master UI Automation";
	public static final String masterUIAutoSuite = masterUIAuto + " Suite";
	public static final String masterUIAutoReport = masterUIAuto + " Report";
	public static final String masterUIAutoStarts = " - " + masterUIAuto + " Testing Starts - ";
	public static final String masterUIAutoEnd = " - " + masterUIAuto + " Testing ends - ";
	public static final String asteriskSeparator = "*";
	public static final String dashSeparator = "-";
	public static final String plsLookOnTest = " - Please take a look on test results below - ";
	public static int stdWaitTime = 40;
	public static File scrDirectory;
	public static WebDriver driver;
	public static Robot robot;
	public static Actions action;
	public static WebDriverWait wait;

	public void browserOpen() throws AWTException {
		try {
			Dimension dimension = new Dimension(1920, 1080);
			String platform = getProperty("platformName").toLowerCase();
			if (platform.equals("firefox")) {
				WebDriverManager.firefoxdriver().setup();
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("browser.download.manager.closeWhenDone", true);
				profile.setPreference("pdfjs.disabled", true);
				driver = new FirefoxDriver();
				writeToConsole("INFO", "Firefox browser invoked successfully");
			} else if (platform.equals("chrome")) {
//				WebDriverManager.chromedriver().clearDriverCache();
				WebDriverManager.chromedriver().setup();
				ChromeOptions options = new ChromeOptions();
				if (Boolean.parseBoolean(getProperty("headless"))) {
					options.addArguments("--headless");
					writeToConsole("INFO", "Headless mode : enabled");
				} else {
					writeToConsole("INFO", "Headless mode : disabled");
				}
				options.addArguments("test-type", "disable-infobars", "--remote-allow-origins=*");
				options.setCapability(ChromeOptions.CAPABILITY, options);
				driver = new ChromeDriver(options);
				writeToConsole("INFO", "Chrome browser invoked successfully");
			}
			if (!(driver == null)) {
				driver.manage().window().setSize(dimension);
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(stdWaitTime));
				wait = new WebDriverWait(driver, Duration.ofSeconds(stdWaitTime));
				action = new Actions(driver);
				robot = new Robot();
				waitTime(2);
			} else {
				writeToConsole("WARN", "driver is null");
			}
		} catch (Exception e) {
			writeToConsole("ERROR", "Unable to open browser : " + e.toString());
		}
	}

	public void browserClose() {
		try {
			driver.manage().deleteAllCookies();
			try {
				driver.quit();
				writeToConsole("INFO", "Closing browser.......");
			} catch (Exception e) {
				writeToConsole("ERROR", "Error occured while closing browser");
			}
		} catch (Exception e) {
			writeToConsole("ERROR", "Error occured while deleting cookies " + e.toString());
		}
	}

	public String getProperty(String key) {
		String value = null;
		try {
			FileReader reader = new FileReader("./src/main/java/resources/Data.properties");
			Properties props = new Properties();
			props.load(reader);
			value = props.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	public void writeToConsole(String type, String message) {
		String t = type.toUpperCase();
		if (t.equalsIgnoreCase("INFO")) {
			System.out.println(message); // For console report
		} else if (t.equalsIgnoreCase("WARN")) {
			System.err.println(message);
		} else if (t.equalsIgnoreCase("SKIP")) {
			System.err.println(message);
		} else if (t.equalsIgnoreCase("FAIL")) {
			System.err.println(message);
		} else {
			System.err.println("Invalid log Type :" + type + ". Unable to log the message.");
		}
	}

	public String retrieve(String label) throws Exception {
		try {
			String actTestName = null;
			String actLabelName = null;
			String value = null;
			int rowCount = testRowMap.get(testScenarioName);
			int colsCount = sheet.getRow(rowCount - 1).getLastCellNum();
			for (int colCount = 1; colsCount <= colsCount; colCount++) {
				actLabelName = sheet.getRow(rowCount - 1).getCell(colCount).getStringCellValue();
				if (actLabelName.equals(label)) {
					Cell cell = sheet.getRow(rowCount).getCell(colCount);
					if (cell.getCellType() == CellType.STRING) {
						value = cell.getStringCellValue();
						writeToConsole("INFO", "Successfully retrived value for label : " + label + " = " + value);
						return value;
					} else if (cell.getCellType() == CellType.NUMERIC) {
						value = String.valueOf((int) cell.getNumericCellValue());
						writeToConsole("INFO", "Successfully retrived value for label : " + label + " = " + value);
						return value;
					} else if (cell.getCellType() == CellType.BOOLEAN) {
						value = String.valueOf(cell.getBooleanCellValue());
						writeToConsole("INFO", "Successfully retrived value for label : " + label + " = " + value);
						return value;
					}
				}
			}
			if (actLabelName == null) {
				writeToConsole("WARN",
						"Unable to find this label in excel : " + label + " For test : " + testScenarioName);
			}
			if (actTestName == null) {
				writeToConsole("WARN", "Unable to find this test in excel : " + testScenarioName);
			}
		} catch (Exception e) {
			writeToConsole("FAIL", "Unable to find value for :" + label + " for test :" + testScenarioName + " Error : "
					+ e.toString());
		}
		return null;
	}

	public void waitTime(int waittime) {
		writeToConsole("INFO", "Waiting for " + waittime + " seconds...");
		try {
			Thread.sleep(waittime * 1000L);
		} catch (InterruptedException e) {
			writeToConsole("ERROR", "Thread.sleep operation failed, during waitTime function call");
		}
	}

	public void transferControlToWindow(int index, boolean closeWindow) {
		Set<String> AllHandles = driver.getWindowHandles();
		ArrayList<String> HandlesToList = new ArrayList<String>();
		HandlesToList.addAll(AllHandles);
		int windows = HandlesToList.size();
		try {
			if (windows >= 1) {
				driver.switchTo().window(HandlesToList.get(index - 1));
				if (closeWindow) {
					driver.close();
				}
			}
		} catch (Exception e) {
			writeToConsole("FAIL", "The given window \"" + index + "\" is failed to open : " + e.toString());
		}
	}

	public void transferControlToProductPageWindow(String productName, boolean closeWindow) {
		Set<String> AllHandles = driver.getWindowHandles();
		ArrayList<String> HandlesToList = new ArrayList<String>();
		HandlesToList.addAll(AllHandles);
		int windows = HandlesToList.size();
		try {
			if (windows >= 1) {
				for (String handle : HandlesToList) {
					driver.switchTo().window(handle);
					if (driver.getTitle().contains(productName)) {
						if (closeWindow) {
							driver.close();
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			writeToConsole("FAIL",
					"The given product page window \"" + productName + "\" is failed to open : " + e.toString());
		}
	}

	public float removeSymbolsAndParsePrice(String price) {
		String cleanedPrice = price.replaceAll("[^0-9.]", "");
		return Float.parseFloat(cleanedPrice);
	}

	public String getText(WebElement webElement) {
		String[] attributes = { "textContent", "value" };
		String text = webElement.getText();
		for (String attribute : attributes) {
			if (text == null | text.equals("")) {
				text = webElement.getAttribute(attribute);
			} else {
				break;
			}
		}
		return text;
	}

	public void waitForPageToLoad() {
		try {
			boolean flag = false;
			JavascriptExecutor js = (JavascriptExecutor) driver;
			while (!flag) {
				String status = js.executeScript("return document.readyState").toString();
				if (status.equals("complete")) {
					Thread.sleep(2000);
					flag = true;
				}
			}
		} catch (Exception e) {
			writeToConsole("FAIL", "An exception occurred waitForPageToLoad() : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public boolean isElementPresent(WebElement webElement) {
		try {
			webElement.isDisplayed();
			return true;
		} catch (NoSuchElementException e) {
			writeToConsole("WARN", "Element is not present : " + e.getMessage());
		}
		return false;
	}

}
