Feature: Login
Description: The purpose of this feature is to test login fumctionality

Background: I visit homepahge of amazon ecommerce application
	Given I open amazon.com "https://www.amazon.in/"

@Adding_a_single_item_in_cart_and_verifying_sub_total
Scenario Outline: Adding a single item in cart and verifying sub total
  When I type a "<Item name>" in the search field
  And I Press Enter
  Then I should see a list of products displayed
  When I select the <nth> item from the list
  Then I should be directed to the product page for the selected item
  When I click on Add to cart button
  Then The item should be successfully added to the cart
  And I close the product page and return to main tab
  When I open the cart
  Then I should verify products name and products total added to cart as per product page
	Examples:
	| Item name | nth |
	| Monitor | 1 |
	| Laptop | 1 |
	
	
@Adding_multiple_items_in_cart_and_verifying_sub_total
Scenario Outline: Adding a single item in cart and verifying sub total
  When I type a "<Item name1>" in the search field
  And I Press Enter
  Then I should see a list of products displayed
  When I select the <nth> item from the list
  Then I should be directed to the product page for the selected item
  When I click on Add to cart button
  Then The item should be successfully added to the cart
  And I close the product page and return to main tab
  When I type a "<Item name2>" in the search field
  And I Press Enter
  Then I should see a list of products displayed
  When I select the <nth> item from the list
  Then I should be directed to the product page for the selected item
  When I click on Add to cart button
  Then The item should be successfully added to the cart
  And I close the product page and return to main tab
  When I open the cart
  Then I should verify products name and products total added to cart as per product page
	Examples:
	| Item name1 | Item name2 | nth | 
	| Headphones | Keyboard | 1 |
	
	
	

