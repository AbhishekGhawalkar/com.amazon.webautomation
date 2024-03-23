package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.Page;
import utilities.DriverBase;

public class AddToCartSteps {
	private static DriverBase webDB = new DriverBase();
	public static Page page;

	@Given("I open amazon.com {string}")
	public void i_open_amazon_com(String url) throws Exception {
		try {
			DriverBase.driver.get(url);
		} catch (Exception e) {
			webDB.writeToConsole("FAIL", "Failed in step : " + e.getStackTrace()[0].getMethodName());
			e.printStackTrace();
		}
	}

	@When("I type a {string} in the search field")
	public void i_type_a_item_name_in_the_search_field_and_press_enter(String product) throws Exception {
		page.searchAProduct(product);
	}

	@And("I Press Enter")
	public void press_enter() {
		page.pressEnter();
	}

	@Then("I should see a list of products displayed")
	public void i_should_see_a_list_of_products_displayed() throws Exception {
		page.verifySearchResultListAppears();
	}

	@When("I select the {int} item from the list")
	public void i_select_the_item_from_the_list(Integer num) {
		page.selectItemFromResultList(num);
	}

	@Then("I should be directed to the product page for the selected item")
	public void i_should_be_directed_to_the_product_page_for_the_selected_item() {
		page.verifyNavigatedToProductPageOfSelectedItem(page.itemName);
	}

	@When("I click on Add to cart button")
	public void i_click_on_add_to_cart_button() {
		page.clickOnAddToCartOnProductPage(page.itemName);
	}

	@Then("The item should be successfully added to the cart")
	public void the_item_should_be_successfully_added_to_the_cart() {
		page.verifyItemAddedToCartMsg(page.itemName);
	}

	@Then("I close the product page and return to main tab")
	public void i_close_the_product_page_and_return_to_main_tab() {
		page.closeTheProductWindowAndReturnToMainWindow(page.itemName);
	}

	@When("I open the cart")
	public void i_open_the_cart() {
		page.openCart();
	}

	@Then("I should verify products name and products total added to cart as per product page")
	public void i_should_verify_products_name_and_products_total_added_to_cart_as_per_product_page() {
		page.verifyProductNamesAndPriceOnCartPageWithProductPage();
	}

}
