package testRunner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = {".//Features"},
		glue = "stepDefinitions",
		tags ="@Adding_a_single_item_in_cart_and_verifying_sub_total or @Adding_two_items_in_cart_and_verifying_sub_total",
		dryRun = false,
		monochrome = true,
		plugin = { "pretty","html:./test-output/ExecutionReport.html" })

public class TestRunner {

}
