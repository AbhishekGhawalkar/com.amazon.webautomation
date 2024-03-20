package pageObjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utilities.DriverBase;

public class Page {
	public static WebDriver ldriver;
	private static DriverBase webDB = new DriverBase();
	public HashMap<String, Float> productNamePriceMap = new HashMap<>();
	public String itemName;
	public float itemPrice;
	public float subTotal = 0;

	public Page(WebDriver driver) {
		ldriver = driver;
		PageFactory.initElements(driver, this);
	}

	// Locators
	@FindBy(xpath = "//input[@id='twotabsearchtextbox']")
	WebElement searchFieldEle;

	@FindBy(xpath = "(//span[input[@id='add-to-cart-button']])[last()]")
	WebElement addToCartBtnEle;

	@FindBy(xpath = "(//span[@class='a-button-inner']//span[normalize-space()='Skip']/preceding-sibling::input)[1]")
	WebElement sideSheetSkipBtnEle;

	@FindBy(xpath = "//span[@id='productTitle']")
	WebElement productTitleEle;

	@FindBy(xpath = "//div[@id='attach-added-to-cart-message']//h4[@class='a-alert-heading'] | //h1[@class='a-size-medium-plus a-color-base sw-atc-text a-text-bold']")
	WebElement addToCartMsgEle;

	@FindBy(xpath = "//span[@class='a-price aok-align-center reinventPricePriceToPayMargin priceToPay']//span[@class='a-price-whole']")
	WebElement productPriceOnProductPageTxtEle;

	@FindBy(xpath = "//a[@id='nav-cart']")
	WebElement cartEle;

	@FindBy(xpath = "//h1[normalize-space()='Shopping Cart']")
	WebElement cartPageHeaderTxt;

	@FindBy(xpath = "//span[@id='sc-subtotal-amount-activecart']/span")
	WebElement cartSubTotalTxtEle;

	@FindBy(xpath = "//div[@class='sc-item-content-group']//span[contains(@class,'a-truncate-full')]")
	List<WebElement> itemNamesFromCartTxtEle;

	@FindBy(xpath = "//div[@data-component-type='s-search-result']//h2//span")
	List<WebElement> searchResultListEle;

	@FindBy(xpath = "//div[@data-name='Active Items']/div[@data-asin]")
	List<WebElement> productListCartEle;

	// Methods
	public void searchAProduct(String itemName) {
		try {
			searchFieldEle.clear();
			searchFieldEle.sendKeys(itemName);
		} catch (Exception e) {
			webDB.writeToConsole("FAIL", "Failed to : " + e.getStackTrace()[0].getMethodName());
			e.printStackTrace();
		}
	}

	public boolean verifySearchResultListAppears() {
		try {
			if (!searchResultListEle.isEmpty()) {
				webDB.writeToConsole("INFO", "Search results appears for the product");
				return true;
			} else {
				webDB.writeToConsole("WARN", "Search results does not appears for the product");
			}
		} catch (Exception e) {
			webDB.writeToConsole("FAIL", "Failed to : " + e.getStackTrace()[0].getMethodName());
			e.printStackTrace();
		}
		return false;
	}

	public void pressEnter() {
		try {
			DriverBase.action.sendKeys(Keys.ENTER).build().perform();
			webDB.waitTime(3);
		} catch (Exception e) {
			webDB.writeToConsole("FAIL", "Failed to : " + e.getStackTrace()[0].getMethodName());
			e.printStackTrace();
		}
	}

	public void selectItemFromResultList(int productIndex) {
		try {
			if (!searchResultListEle.isEmpty()) {
				WebElement itemEle = searchResultListEle.get(productIndex - 1);
				itemName = itemEle.getText();
				DriverBase.action.moveToElement(itemEle);
				itemEle.click();
				webDB.waitTime(5);
			} else {
				webDB.writeToConsole("WARN", "Search result is empty");
			}
		} catch (Exception e) {
			webDB.writeToConsole("FAIL", "Failed to : " + e.getStackTrace()[0].getMethodName());
			e.printStackTrace();
		}
	}

	public boolean verifyNavigatedToProductPageOfSelectedItem(String productName) {
		try {
			webDB.transferControlToProductPageWindow(productName, false);
			return true;
		} catch (Exception e) {
			webDB.writeToConsole("FAIL", "Failed to : " + e.getStackTrace()[0].getMethodName());
			e.printStackTrace();
		}
		return false;
	}

	public void clickOnAddToCartOnProductPage(String productName) {
		try {
			String pageTitle = ldriver.getTitle();
			if (pageTitle.contains(productName)) {
				if (productTitleEle.getText().equals(productName)) {
					itemPrice = webDB.removeSymbolsAndParsePrice(productPriceOnProductPageTxtEle.getText());
					addToCartBtnEle.click();
					if (webDB.isElementPresent(sideSheetSkipBtnEle)) {
						sideSheetSkipBtnEle.click();
					}
					webDB.waitTime(3);
				} else {
					webDB.writeToConsole("WARN", "Product name on page doesn't match with the expected product name");
				}
			} else {
				webDB.writeToConsole("WARN", "Page title doesn't match with the expected product name");
			}
		} catch (Exception e) {
			webDB.writeToConsole("FAIL", "Failed to : " + e.getStackTrace()[0].getMethodName());
			e.printStackTrace();
		}
	}

	public boolean verifyItemAddedToCartMsg(String productName) {
		try {
			if (addToCartMsgEle.isDisplayed()) {
				String message = addToCartMsgEle.getText();
				if (message.equals("Added to Cart")) {
					webDB.writeToConsole("INFO", "Item Added to the cart as expected");
					productNamePriceMap.put(productName, itemPrice);
					webDB.waitTime(1);
					return true;
				} else {
					webDB.writeToConsole("WARN", "Add to Cart message is not as expected");
				}
			} else {
				webDB.writeToConsole("WARN", "Added to cart message is not showing");
			}
		} catch (Exception e) {
			webDB.writeToConsole("FAIL", "Failed to : " + e.getStackTrace()[0].getMethodName());
			e.printStackTrace();
		}
		return false;
	}

	public void closeTheProductWindowAndReturnToMainWindow(String productName) {
		try {
			ldriver.close();
			webDB.transferControlToWindow(1, false);
		} catch (Exception e) {
			webDB.writeToConsole("FAIL", "Failed to : " + e.getStackTrace()[0].getMethodName());
			e.printStackTrace();
		}
	}

	public void openCart() {
		try {
			if (cartEle.isDisplayed()) {
				cartEle.click();
				webDB.waitTime(3);
				if (cartPageHeaderTxt.isDisplayed()) {
					webDB.writeToConsole("INFO", "Cart is opened");
				}
			} else {
				webDB.writeToConsole("WARN", "Cart button is not displayed");
			}
		} catch (Exception e) {
			webDB.writeToConsole("FAIL", "Failed to : " + e.getStackTrace()[0].getMethodName());
			e.printStackTrace();
		}
	}

	public void verifyProductNamesAndPriceOnCartPageWithProductPage() {
		HashMap<String, Float> productNamePriceMapAct = new HashMap<>();
		try {
			webDB.wait.until(ExpectedConditions.visibilityOfAllElements(productListCartEle));
			for (int i = 1; i <= productListCartEle.size(); i++) {
				String actProductName = webDB.getText(ldriver.findElement(By.xpath(
						"(//div[@data-name='Active Items']/div[@data-asin]//span[contains(@class,'a-truncate-full')])["
								+ i + "]")));
				float actProductPrice = webDB.removeSymbolsAndParsePrice(webDB.getText(ldriver.findElement(By.xpath(
						"(//div[@data-name='Active Items']/div[@data-asin]//span[@class='currencyINR']/parent::span)["
								+ i + "]"))));
				productNamePriceMapAct.put(actProductName, actProductPrice);
			}
			if (productNamePriceMapAct.equals(productNamePriceMap)) {
				webDB.writeToConsole("INFO", "Verified product names and prices added to cart");
				float actSubTotal = webDB.removeSymbolsAndParsePrice(webDB.getText(cartSubTotalTxtEle));
				float extpSubTotal = allAddedCartItemExpectedTotal();
				if (actSubTotal == extpSubTotal) {
					webDB.writeToConsole("INFO", "Verified sub total of items added to cart");
				} else {
					webDB.writeToConsole("WARN", "Sub total of items added to cart is not as expected");
					Assert.fail();
				}
			} else {
				webDB.writeToConsole("WARN", "Product names and prices are not as expected");
			}
		} catch (Exception e) {
			webDB.writeToConsole("FAIL", "Failed to : " + e.getStackTrace()[0].getMethodName());
			e.printStackTrace();
		}
	}

	public float allAddedCartItemExpectedTotal() {
		for (Map.Entry<String, Float> entry : productNamePriceMap.entrySet()) {
			subTotal += entry.getValue();
		}
		return subTotal;
	}

}
